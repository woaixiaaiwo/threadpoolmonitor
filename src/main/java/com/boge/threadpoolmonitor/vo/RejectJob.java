package com.boge.threadpoolmonitor.vo;


public class RejectJob {

    private Job job;

    private String reason;

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "RejectJob{" +
                "job=" + job +
                ", reason='" + reason + '\'' +
                '}';
    }
}
