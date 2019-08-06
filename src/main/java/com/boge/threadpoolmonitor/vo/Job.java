package com.boge.threadpoolmonitor.vo;

public class Job implements Runnable{

    private Long executeTime;

    private String name;

    public Job(Long executeTime, String name) {
        this.executeTime = executeTime;
        this.name = name;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();

        System.out.println("任务:" + name + "由线程:"+threadName+"执行!");
        while(executeTime > 0) {
            System.out.println("任务:" + name + "执行进度:" + executeTime--);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Long getExecuteTime() {
        return executeTime;
    }

    public String getName() {
        return name;
    }
}
