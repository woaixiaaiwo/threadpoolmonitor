package com.boge.threadpoolmonitor.monitor;

import com.boge.threadpoolmonitor.ThreadPoolContainer;
import com.boge.threadpoolmonitor.factory.JobFactory;
import com.boge.threadpoolmonitor.threadpool.MonitorRejectedExecutionHandler;
import com.boge.threadpoolmonitor.threadpool.MonitorThreadPoolExecutor;
import com.boge.threadpoolmonitor.util.CommonUtil;
import com.boge.threadpoolmonitor.vo.Job;
import com.boge.threadpoolmonitor.vo.MonitorResponseVO;
import com.boge.threadpoolmonitor.vo.ThreadPoolVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;

public class Monitor{

    private static Logger logger = LoggerFactory.getLogger(Monitor.class);

   private MonitorContext monitorContext;

   private ThreadPoolExecutor threadPoolExecutor;

   private ThreadPoolVO threadPoolVO;

   private volatile Long visitTimeMillis;

   private Long monitorTimeout;

   private Integer status;

   private String name;

   private String monitorId;

   private final static Integer RUNNING = 1;
   private final static Integer TIMEOUT = -1;

    public Integer getStatus() {
        return status;
    }

    public Monitor(String monitorId, ThreadPoolExecutor threadPoolExecutor,Long monitorTimeout){
       this.monitorContext = new MonitorContext();
       this.threadPoolExecutor = threadPoolExecutor;
       this.visitTimeMillis = System.currentTimeMillis();
       this.threadPoolVO = new ThreadPoolVO(threadPoolExecutor);
       this.monitorTimeout = monitorTimeout;
       this.status = RUNNING;
       this.monitorId = monitorId;
       this.name = "monitor-"+monitorId;
       Thread checkMonitor = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    logger.info("执行Monitor：{}监控...",name);
                    Long current = System.currentTimeMillis();
                    if(current - Monitor.this.visitTimeMillis >= monitorTimeout*1000){
                        threadPoolExecutor.shutdownNow();
                        Monitor.this.status = TIMEOUT;
                        ThreadPoolContainer.removeMonitor(monitorId);
                        logger.warn("【monitor:{}超时，已关闭】",name);
                        return;
                    }
                    CommonUtil.sleep(monitorTimeout*1000);
                }
            }
        });
        checkMonitor.setName("checkMonitor-"+name);
        checkMonitor.start();
        logger.info("Monitor：{}超时监控已启动...",name);
    }

    public void submitJob(Long executeTime){
        Job job = JobFactory.getJob(executeTime,this.monitorContext,this.monitorId);
        logger.info("新任务:【{}】被提交到monitor:{}",job,name);
        this.threadPoolExecutor.execute(job);
    }

    public void moniteInternal(){
       this.visitTimeMillis = System.currentTimeMillis();
       threadPoolVO.refresh();
       printState(threadPoolVO);
    }

    public String moniteString(){
        this.visitTimeMillis = System.currentTimeMillis();
        threadPoolVO.refresh();
        return printState(threadPoolVO);
    }


    public MonitorResponseVO monite(){
        this.visitTimeMillis = System.currentTimeMillis();
        threadPoolVO.refresh();
        MonitorResponseVO monitorResponseVO = new MonitorResponseVO();
        monitorResponseVO.setThreadJobsList(getThreadJobs(threadPoolVO.getWorkerThreadNames()));
        monitorResponseVO.setThreadCount(threadPoolVO.getWorkCount());
        monitorResponseVO.setThreadPoolStatus(threadPoolVO.getStatus());
        monitorResponseVO.setQueueJobList(getJobs(threadPoolVO.getQueue()));
        monitorResponseVO.setRejectJobList(((MonitorRejectedExecutionHandler)threadPoolExecutor.getRejectedExecutionHandler()).getRejectJobs());
        return monitorResponseVO;
    }


   private String printState(ThreadPoolVO threadPoolVO){
       HashSet<String> workerThreadNames = threadPoolVO.getWorkerThreadNames();
       BlockingQueue<Runnable> queue = threadPoolVO.getQueue();
       StringBuilder stringBuilder = new StringBuilder();
       stringBuilder.append("----------------------------------------------------------------------\n");
       stringBuilder.append("活动线程情况："+printThreadContent(workerThreadNames)+"\n");
       stringBuilder.append("活动线程个数："+threadPoolVO.getWorkCount()+"\n");
       stringBuilder.append("线程池状态："+threadPoolVO.getStatus()+"\n");
       stringBuilder.append("队列情况："+printQueue(queue)+"\n");
       stringBuilder.append("队列任务个数："+queue.size()+"\n");
       stringBuilder.append("拒绝任务："+((MonitorRejectedExecutionHandler)threadPoolExecutor.getRejectedExecutionHandler()).getRejectJobs()+"\n");
       stringBuilder.append("----------------------------------------------------------------------\n");
       System.out.println(stringBuilder);
       return stringBuilder.toString();
   }

   private String printThreadContent(HashSet<String> threadNames){
       StringBuilder stringBuilder = new StringBuilder();
       for(String threadName:threadNames){
           Job job = monitorContext.getJobName(threadName);
           if(job != null){
               stringBuilder.append("{"+threadName+":【"+job.getName()+":"+job.getExecuteTime()+"】}   ");
           }else {
               stringBuilder.append("{"+threadName+":【无任务】}");
           }
       }
       return stringBuilder.toString();
   }

    private List<MonitorResponseVO.ThreadJobs> getThreadJobs(HashSet<String> threadNames){
        List<MonitorResponseVO.ThreadJobs> result = new ArrayList<>();
        for(String threadName:threadNames){
            MonitorResponseVO.ThreadJobs threadJobs = new MonitorResponseVO.ThreadJobs();
            Job job = monitorContext.getJobName(threadName);
            threadJobs.setThreadName(threadName);
            if(job != null){
                threadJobs.setJob(job);
            }else {
                threadJobs.setJob(null);
            }
            result.add(threadJobs);
        }
        return result;
    }

    private List<Job> getJobs(BlockingQueue<Runnable> blockingQueue){
        List<Job> jobList = new ArrayList<>();
        for(Runnable runnable:blockingQueue){
            Job job = (Job)runnable;
            jobList.add(job);
        }
        return jobList;
    }

    private static String printQueue(BlockingQueue<Runnable> blockingQueue){
        StringBuilder stringBuilder = new StringBuilder();
        int i=0;
        for(Runnable runnable:blockingQueue){
            Job job = (Job)runnable;
            stringBuilder.append("【"+job.getName()+":"+job.getExecuteTime()+"】");
            if(i++ < blockingQueue.size()-1){
                stringBuilder.append("->");
            }
        }
        return stringBuilder.toString();
    }

    //todo:后续可改成居中操作
    private static String printQueueNode(Job job){
        StringBuilder stringBuilder = new StringBuilder();
        String total = "|----------------------|";
        int totalLength = total.length();
        stringBuilder.append(total+"\n");

        String jobName = "|   jobName:"+job.getName();
        String executeTimeName = "|   executeTime:"+job.getExecuteTime();
        stringBuilder.append(jobName);
        for(int i=0;i<totalLength-jobName.length()-1;i++){
            stringBuilder.append(" ");
        }
        stringBuilder.append("|\n");
        stringBuilder.append(executeTimeName);
        for(int i=0;i<totalLength-executeTimeName.length()-1;i++){
            stringBuilder.append(" ");
        }
        stringBuilder.append("|\n");
        stringBuilder.append(total+"\n");
        return stringBuilder.toString();
    }
}
