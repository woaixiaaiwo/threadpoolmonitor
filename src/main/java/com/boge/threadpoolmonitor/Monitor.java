package com.boge.threadpoolmonitor;

import com.boge.threadpoolmonitor.factory.JobFactory;
import com.boge.threadpoolmonitor.factory.MonitorThreadFactory;
import com.boge.threadpoolmonitor.vo.Job;
import com.boge.threadpoolmonitor.vo.ThreadPoolVO;

import java.util.HashSet;
import java.util.concurrent.*;

public class Monitor{

   public void monite(ThreadPoolExecutor threadPoolExecutor){
       ThreadPoolVO threadPoolVO = new ThreadPoolVO(threadPoolExecutor);
       while(true){
           threadPoolVO.refresh();
           printState(threadPoolVO);
           try {
               Thread.sleep(1000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
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
       MonitorContext monitorContext = MonitorContext.getInstance();
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

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new MonitorThreadPoolExecutor(1,2,5000, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(3));
        threadPoolExecutor.execute(JobFactory.getJob(5L));
        threadPoolExecutor.execute(JobFactory.getJob(5L));
        threadPoolExecutor.execute(JobFactory.getJob(5L));
        threadPoolExecutor.execute(JobFactory.getJob(5L));
        threadPoolExecutor.execute(JobFactory.getJob(5L));
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        new Monitor().monite(threadPoolExecutor);
   }
}
