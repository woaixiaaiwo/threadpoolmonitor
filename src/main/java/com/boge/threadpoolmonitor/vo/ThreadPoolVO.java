package com.boge.threadpoolmonitor.vo;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池访问对象，封装线程池各种参数
 */
public class ThreadPoolVO {


    private static Field CTL_FIELD;
    private static Field WORKER_FIELD;
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    private static int runStateOf(int c)     { return c & ~CAPACITY; }
    private static int workerCountOf(int c)  { return c & CAPACITY; }

    static {
        try {
            CTL_FIELD = ThreadPoolExecutor.class.getDeclaredField("ctl");
            WORKER_FIELD = ThreadPoolExecutor.class.getDeclaredField("workers");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * ctl
     */
    private AtomicInteger ctl;

    private HashSet<String> workerThreadNames;

    /**
     * 线程数量
     */
    private Integer workCount;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 完成任务数
     */
    private Long completedTaskCount;

    /**
     * 是否允许核心线程回收
     */
    private Boolean allowCoreThreadTimeOut;


    /**
     * 线程池参数
     */
    private Integer coreSize;

    private Integer maxSize;

    private Long keepAliveTime;

    private TimeUnit unit = TimeUnit.MILLISECONDS;

    private BlockingQueue<Runnable> queue;

    public ThreadPoolVO(ThreadPoolExecutor threadPoolExecutor){
        refresh(threadPoolExecutor);
        allowCoreThreadTimeOut = threadPoolExecutor.allowsCoreThreadTimeOut();
        coreSize = threadPoolExecutor.getCorePoolSize();
        maxSize = threadPoolExecutor.getMaximumPoolSize();
        keepAliveTime = threadPoolExecutor.getKeepAliveTime(unit);
    }

    public void refresh(ThreadPoolExecutor threadPoolExecutor){
        try {
            ctl = (AtomicInteger) CTL_FIELD.get(threadPoolExecutor);
            workCount = workerCountOf(ctl.get());
            HashSet workers = (HashSet) WORKER_FIELD.get(threadPoolExecutor);
            for(Object worker:workers){
                Field field = worker.getClass().getDeclaredField("thread");
                Thread thread = (Thread) field.get(worker);
                workerThreadNames.add(thread.getName());
            }
            status = runStateOf(ctl.get());
            completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
            queue = threadPoolExecutor.getQueue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
