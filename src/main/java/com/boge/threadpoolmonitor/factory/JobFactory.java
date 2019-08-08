package com.boge.threadpoolmonitor.factory;

import com.boge.threadpoolmonitor.monitor.MonitorContext;
import com.boge.threadpoolmonitor.vo.Job;

import java.util.concurrent.atomic.AtomicLong;

public class JobFactory {

    private final static String JOB_PREFIX = "job-";

    private final static AtomicLong ID = new AtomicLong(1);

    public static Job getJob(Long executeTime, MonitorContext monitorContext,String requestId){
        return new Job(executeTime,JOB_PREFIX+requestId+"-"+ID.addAndGet(1),monitorContext);
    }
}
