package com.boge.threadpoolmonitor.threadpool;

import com.boge.threadpoolmonitor.vo.Job;
import com.boge.threadpoolmonitor.vo.RejectJob;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class MonitorRejectedExecutionHandler implements RejectedExecutionHandler {

    private List<RejectJob> rejectJobs = new ArrayList<>();

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        Job job = (Job)r;
        RejectJob rejectJob = new RejectJob();
        rejectJob.setReason(getRejectReason(executor));
        rejectJob.setJob(job);
        rejectJobs.add(rejectJob);
        System.out.println("任务:"+job.getName()+"被拒绝!");
    }

    private String getRejectReason(ThreadPoolExecutor executor){
        if(executor.isTerminating() || executor.isTerminated() || executor.isShutdown()){
            return "线程池已关闭！";
        }
        return "线程数已达最大值，且队列已满！";
    }

    public List<RejectJob> getRejectJobs() {
        return rejectJobs;
    }
}
