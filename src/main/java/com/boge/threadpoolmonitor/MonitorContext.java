package com.boge.threadpoolmonitor;

import com.boge.threadpoolmonitor.vo.Job;

import java.util.concurrent.ConcurrentHashMap;

public class MonitorContext {

    /**
     * 线程-任务映射关系
     */
    private static ConcurrentHashMap<String, Job> THREAD_JOB_MAPPING;

    public static void addMapping(String threadName,Job job){
        THREAD_JOB_MAPPING.put(threadName,job);
    }

    public static Job getJobName(String threadName){
       return THREAD_JOB_MAPPING.get(threadName);
    }

}
