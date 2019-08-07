package com.boge.threadpoolmonitor.threadpool;

import com.boge.threadpoolmonitor.vo.Job;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class MonitorRejectedExecutionHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        Job job = (Job)r;
        System.out.println("任务:"+job.getName()+"被拒绝!");
    }
}
