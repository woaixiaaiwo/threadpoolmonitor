package com.boge.threadpoolmonitor.vo;

import com.boge.threadpoolmonitor.monitor.MonitorContext;
import com.boge.threadpoolmonitor.util.CommonUtil;

public class Job implements Runnable{

    private Long executeTime;

    private String name;

    private MonitorContext monitorContext;

    public Job(Long executeTime, String name,MonitorContext monitorContext) {
        this.executeTime = executeTime;
        this.name = name;
        this.monitorContext = monitorContext;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println("【任务:" + name + "由线程:"+threadName+"执行!】");
        monitorContext.addMapping(threadName,this);
        while(executeTime > 0) {
            //System.out.println("任务:" + name + "执行进度:" + executeTime--);
            executeTime--;
            CommonUtil.sleep(1000L);
        }
        //任务完成，移除
        monitorContext.removeJob(this);
    }

    @Override
    public String toString() {
        return "Job{" +
                "executeTime=" + executeTime +
                ", name='" + name + '\'' +
                '}';
    }

    public Long getExecuteTime() {
        return executeTime;
    }

    public String getName() {
        return name;
    }
}
