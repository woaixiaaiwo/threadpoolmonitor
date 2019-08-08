package com.boge.threadpoolmonitor.vo;

/**
 * 线程池参数
 */
public class ThreadPoolParam {

    private Integer coreSize;

    private Integer maxSize;

    private Long keepAliveTime;

    private Integer queueCapacity;

    private Boolean allowCoreThreadTimeOut;

    public ThreadPoolParam(){}

    public ThreadPoolParam(Integer coreSize, Integer maxSize, Long keepAliveTime, Integer queueCapacity) {
        this(coreSize,maxSize,keepAliveTime,queueCapacity,false);
    }

    public ThreadPoolParam(Integer coreSize, Integer maxSize, Long keepAliveTime, Integer queueCapacity, Boolean allowCoreThreadTimeOut) {
        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.keepAliveTime = keepAliveTime;
        this.queueCapacity = queueCapacity;
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }

    public Integer getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(Integer coreSize) {
        this.coreSize = coreSize;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public Long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(Long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public Integer getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(Integer queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public Boolean getAllowCoreThreadTimeOut() {
        return allowCoreThreadTimeOut;
    }

    public void setAllowCoreThreadTimeOut(Boolean allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }

    @Override
    public String toString() {
        return "ThreadPoolParam{" +
                "coreSize=" + coreSize +
                ", maxSize=" + maxSize +
                ", keepAliveTime=" + keepAliveTime +
                ", queueCapacity=" + queueCapacity +
                ", allowCoreThreadTimeOut=" + allowCoreThreadTimeOut +
                '}';
    }
}
