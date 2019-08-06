package com.boge.threadpoolmonitor.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class MonitorThreadFactory implements ThreadFactory {

    private final static String THREAD_PREFIX = "thread-";

    private final static AtomicLong ID = new AtomicLong(1);

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(THREAD_PREFIX+ID.getAndAdd(1));
        return thread;
    }
}
