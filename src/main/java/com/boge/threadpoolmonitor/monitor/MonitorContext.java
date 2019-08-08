package com.boge.threadpoolmonitor.monitor;

import com.boge.threadpoolmonitor.vo.Job;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MonitorContext {

    /**
     * 线程-任务映射关系
     */
    private HashMap<String, Job> THREAD_JOB_MAPPING = new HashMap<>();

    /*private MonitorContext(){}

    private static class MonitorContextProvider{
        private static MonitorContext monitorContext = new MonitorContext();
    }

    public static MonitorContext getInstance(){
        return MonitorContextProvider.monitorContext;
    }*/

    public synchronized void addMapping(String threadName,Job job){
        THREAD_JOB_MAPPING.put(threadName,job);
    }

    public Job getJobName(String threadName){
       return THREAD_JOB_MAPPING.get(threadName);
    }

    public void removeJob(Job job){
        synchronized(this){
            String removeKey = "";
            for(Map.Entry<String,Job> entry:THREAD_JOB_MAPPING.entrySet()){
                if(entry.getValue() == job){
                    removeKey = entry.getKey();
                }
            }
            THREAD_JOB_MAPPING.remove(removeKey);
        }
    }

}
