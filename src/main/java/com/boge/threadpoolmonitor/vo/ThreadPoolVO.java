package com.boge.threadpoolmonitor.vo;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程池访问对象，封装线程池各种参数
 */
public class ThreadPoolVO {


    private static Field CTL_FIELD;
    private static Field WORKER_FIELD;
    private static Field MAINLOCK_FIELD;
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    private static int runStateOf(int c)     { return c & ~CAPACITY; }
    private static int workerCountOf(int c)  { return c & CAPACITY; }

    static {
        try {
            CTL_FIELD = ThreadPoolExecutor.class.getDeclaredField("ctl");
            WORKER_FIELD = ThreadPoolExecutor.class.getDeclaredField("workers");
            MAINLOCK_FIELD = ThreadPoolExecutor.class.getDeclaredField("mainLock");
            CTL_FIELD.setAccessible(true);
            WORKER_FIELD.setAccessible(true);
            MAINLOCK_FIELD.setAccessible(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private ThreadPoolExecutor threadPoolExecutor;

    private ReentrantLock mainLock;

    /**
     * ctl
     */
    private AtomicInteger ctl;

    private HashSet<String> workerThreadNames = new HashSet<>();

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
        this.threadPoolExecutor = threadPoolExecutor;
        try {
            this.mainLock = (ReentrantLock) MAINLOCK_FIELD.get(threadPoolExecutor);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.allowCoreThreadTimeOut = threadPoolExecutor.allowsCoreThreadTimeOut();
        this.coreSize = threadPoolExecutor.getCorePoolSize();
        this.maxSize = threadPoolExecutor.getMaximumPoolSize();
        this.keepAliveTime = threadPoolExecutor.getKeepAliveTime(unit);
    }

    public void refresh(){
        try {
            ctl = (AtomicInteger) CTL_FIELD.get(threadPoolExecutor);
            workCount = workerCountOf(ctl.get());
            mainLock.lock();
            HashSet workers = (HashSet) WORKER_FIELD.get(threadPoolExecutor);
            workerThreadNames.clear();
            for(Object worker:workers){
                Field field = worker.getClass().getDeclaredField("thread");
                field.setAccessible(true);
                Thread thread = (Thread) field.get(worker);
                workerThreadNames.add(thread.getName());
            }
            mainLock.unlock();
            status = runStateOf(ctl.get());
            completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
            queue = threadPoolExecutor.getQueue();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(mainLock.isLocked()){
                mainLock.unlock();
            }
        }
    }

    public AtomicInteger getCtl() {
        return ctl;
    }

    public HashSet<String> getWorkerThreadNames() {
        return workerThreadNames;
    }

    public Integer getWorkCount() {
        return workCount;
    }

    public Integer getStatus() {
        return status;
    }

    public Long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public Boolean getAllowCoreThreadTimeOut() {
        return allowCoreThreadTimeOut;
    }

    public Integer getCoreSize() {
        return coreSize;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public Long getKeepAliveTime() {
        return keepAliveTime;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public BlockingQueue<Runnable> getQueue() {
        return queue;
    }
}
