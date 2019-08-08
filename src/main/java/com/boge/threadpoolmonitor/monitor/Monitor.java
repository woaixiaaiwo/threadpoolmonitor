package com.boge.threadpoolmonitor.monitor;

import com.boge.threadpoolmonitor.ThreadPoolContainer;
import com.boge.threadpoolmonitor.factory.JobFactory;
import com.boge.threadpoolmonitor.threadpool.MonitorThreadPoolExecutor;
import com.boge.threadpoolmonitor.util.CommonUtil;
import com.boge.threadpoolmonitor.vo.Job;
import com.boge.threadpoolmonitor.vo.MonitorResponseVO;
import com.boge.threadpoolmonitor.vo.ThreadPoolVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashSet;
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
        logger.info("Monitor超时监控已启动...");
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

    public MonitorResponseVO monite(){
        this.visitTimeMillis = System.currentTimeMillis();
        threadPoolVO.refresh();
        return null;
    }


   private void printState(ThreadPoolVO threadPoolVO){
       HashSet<String> workerThreadNames = threadPoolVO.getWorkerThreadNames();
       BlockingQueue<Runnable> queue = threadPoolVO.getQueue();
       System.out.println("----------------------------------------------------------------------");
       System.out.println("活动线程情况："+printThreadContent(workerThreadNames));
       System.out.println("活动线程个数："+threadPoolVO.getWorkCount());
       System.out.println("线程池状态："+threadPoolVO.getStatus());
       System.out.println("队列情况："+printQueue(queue));
       System.out.println("队列任务个数："+queue.size());
       System.out.println("----------------------------------------------------------------------");
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
