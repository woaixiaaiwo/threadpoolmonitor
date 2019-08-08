package com.boge.threadpoolmonitor.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtil {

    private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    public static void sleep(Long time){
        try{
            Thread.sleep(time);
        }catch (Exception e){
            logger.error("线程"+Thread.currentThread().getName()+"被中断!");
        }
    }


}
