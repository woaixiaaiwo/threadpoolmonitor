package com.boge.threadpoolmonitor;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池访问对象，封装线程池各种参数
 */
public class ThreadPoolVO {

    /**
     * ctl
     */
    private AtomicInteger ctl;

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
    private Integer completedTaskCount;

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

    private TimeUnit unit;

    private BlockingQueue<Runnable> queue;

    private RejectedExecutionHandler rejectedExecutionHandler;


    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    private static int runStateOf(int c)     { return c & ~CAPACITY; }
    private static int workerCountOf(int c)  { return c & CAPACITY; }

    public static void main(String[] args) throws Exception{
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2,
                1L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(3));
        threadPoolExecutor.allowsCoreThreadTimeOut();
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    System.out.println("job1 done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    System.out.println("job2 done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    System.out.println("job3 done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    System.out.println("job4 done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    System.out.println("job5 done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        while(true){
            MetaObject metaObject = SystemMetaObject.forObject(threadPoolExecutor);
            AtomicInteger ctl = (AtomicInteger) metaObject.getValue("ctl");
            Thread.sleep(100);
            System.out.println(workerCountOf(ctl.get()));
        }

    }

}
