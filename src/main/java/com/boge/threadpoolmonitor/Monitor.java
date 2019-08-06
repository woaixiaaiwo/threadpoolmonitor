package com.boge.threadpoolmonitor;

import com.boge.threadpoolmonitor.vo.Job;
import com.boge.threadpoolmonitor.vo.ThreadPoolVO;

import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class Monitor{

   public void monite(ThreadPoolExecutor threadPoolExecutor){

       ThreadPoolVO threadPoolVO = new ThreadPoolVO(threadPoolExecutor);


   }


   private void printState(ThreadPoolVO threadPoolVO){
       System.out.println(String.format("活动线程情况：【%s】，活动线程个数：【%d】，状态：【%d】，队列情况：【%s】,队列任务个数：【%d】"));
   }

   private String printThreadContent(HashSet<String> threadNames){
       return "";
   }

    private String printQueue(HashSet<String> threadNames){
        return "";
    }

    private static String printQueueNode(BlockingQueue<Runnable> blockingQueue){
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
