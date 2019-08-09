package com.boge.threadpoolmonitor.vo;


import java.util.List;

public class MonitorResponseVO {

    private List<ThreadJobs> threadJobsList;

    private Integer threadCount;

    private String threadPoolStatus;

    private List<Job> queueJobList;

    private List<RejectJob> rejectJobList;

    public List<ThreadJobs> getThreadJobsList() {
        return threadJobsList;
    }

    public void setThreadJobsList(List<ThreadJobs> threadJobsList) {
        this.threadJobsList = threadJobsList;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

    public String getThreadPoolStatus() {
        return threadPoolStatus;
    }

    public void setThreadPoolStatus(String threadPoolStatus) {
        this.threadPoolStatus = threadPoolStatus;
    }

    public List<Job> getQueueJobList() {
        return queueJobList;
    }

    public void setQueueJobList(List<Job> queueJobList) {
        this.queueJobList = queueJobList;
    }

    public List<RejectJob> getRejectJobList() {
        return rejectJobList;
    }

    public void setRejectJobList(List<RejectJob> rejectJobList) {
        this.rejectJobList = rejectJobList;
    }

    public static class ThreadJobs{

        private String threadName;

        private Job job;

        public String getThreadName() {
            return threadName;
        }

        public void setThreadName(String threadName) {
            this.threadName = threadName;
        }

        public Job getJob() {
            return job;
        }

        public void setJob(Job job) {
            this.job = job;
        }
    }



}
