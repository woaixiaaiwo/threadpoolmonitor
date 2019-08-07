package com.boge.threadpoolmonitor;

import com.boge.threadpoolmonitor.vo.ThreadPoolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程池容器
 */
@Component
public class ThreadPoolContainer {

    @Value("${monitorTimeout:5}")
    private Integer monitorTimeout;

    private class Key{
        private String requestId;

        private Date date = new Date();

        private Key(String requestId){
            this.requestId = requestId;
        }
    }

    private final ConcurrentHashMap<Key,Monitor> TOTAL_MONITORS = new ConcurrentHashMap<>();

    public void createMonitor(String requestId, ThreadPoolParam threadPoolParam){

    }

}
