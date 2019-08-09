package com.boge.threadpoolmonitor;

import com.boge.threadpoolmonitor.vo.MonitorResponseVO;
import com.boge.threadpoolmonitor.vo.ThreadPoolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    private ThreadPoolContainer threadPoolContainer;

    public static class CreateMonitorParam{

        private String requestId;

        private ThreadPoolParam threadPoolParam;

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public ThreadPoolParam getThreadPoolParam() {
            return threadPoolParam;
        }

        public void setThreadPoolParam(ThreadPoolParam threadPoolParam) {
            this.threadPoolParam = threadPoolParam;
        }

        @Override
        public String toString() {
            return "CreateMonitorParam{" +
                    "requestId='" + requestId + '\'' +
                    ", threadPoolParam=" + threadPoolParam +
                    '}';
        }
    }

    @PostMapping("/createMonitorCustom")
    public void createMonitor(@RequestBody CreateMonitorParam createMonitorParam){
        threadPoolContainer.createMonitor(createMonitorParam.getRequestId(),createMonitorParam.getThreadPoolParam());
    }

    @PostMapping("/createMonitor")
    public void createMonitor(@RequestParam("requestId")String requestId){
        ThreadPoolParam threadPoolParam = new ThreadPoolParam(1,10,5000L,10);
        threadPoolContainer.createMonitor(requestId,threadPoolParam);
    }

    @PostMapping("/submitJobCustom")
    public void submitJob(@RequestParam("requestId")String requestId,@RequestParam("executeTime")Long executeTime){
        threadPoolContainer.submitJob(requestId,executeTime);
    }

    @PostMapping("/submitJob")
    public void submitJob(@RequestParam("requestId")String requestId){
        threadPoolContainer.submitJob(requestId,10L);
    }

    @GetMapping("/monite/{requestId}")
    public MonitorResponseVO monite(@PathVariable("requestId")String requestId){
        return threadPoolContainer.monite(requestId);
    }

    @GetMapping("/moniteString/{requestId}")
    public String moniteString(@PathVariable("requestId")String requestId){
        return threadPoolContainer.moniteString(requestId);
    }
}
