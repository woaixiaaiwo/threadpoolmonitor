package com.boge.threadpoolmonitor;

import com.boge.threadpoolmonitor.monitor.Monitor;
import com.boge.threadpoolmonitor.threadpool.MonitorThreadPoolExecutor;
import com.boge.threadpoolmonitor.util.CommonUtil;
import com.boge.threadpoolmonitor.vo.ThreadPoolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程池容器
 */
@Component
public class ThreadPoolContainer {

    @Value("${monitorTimeout:5}")
    private Long monitorTimeout;

    private final static ConcurrentHashMap<String, Monitor> MONITORS = new ConcurrentHashMap<>();

    public boolean createMonitor(String requestId, ThreadPoolParam threadPoolParam){
        Monitor monitor = new Monitor(requestId,new MonitorThreadPoolExecutor(requestId,threadPoolParam),monitorTimeout);
        MONITORS.put(requestId,monitor);
        return true;
    }

    public void submitJob(String requestId,Long executeTime){
        Monitor monitor = MONITORS.get(requestId);
        if(monitor != null) {
            monitor.submitJob(executeTime);
        }
    }

    public void monite(String requestId){
        Monitor monitor = MONITORS.get(requestId);
        if(monitor != null){
            monitor.moniteInternal();
        }
    }

    public static void removeMonitor(String requestId){
        MONITORS.remove(requestId);
    }




    public static void main(String[] args) {
        String requestId = "123";
        ThreadPoolContainer threadPoolContainer = new ThreadPoolContainer();
        threadPoolContainer.createMonitor(requestId,new ThreadPoolParam(1,200,5000L,1));
        threadPoolContainer.submitJob(requestId,5L);
        threadPoolContainer.submitJob(requestId,5L);
        Random random = new Random();
        int i=0;
        while (true){
            if(i<3){
                threadPoolContainer.monite(requestId);
                System.out.println("开始监控");
            }

            if(random.nextBoolean()){
                threadPoolContainer.submitJob(requestId,5L);
                i++;
            }
            CommonUtil.sleep(1000L);
        }
    }

}
