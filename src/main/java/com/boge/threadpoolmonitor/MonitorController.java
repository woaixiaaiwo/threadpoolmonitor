package com.boge.threadpoolmonitor;

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

    @PostMapping("/createMonitor")
    public void createMonitor(@RequestBody CreateMonitorParam createMonitorParam){
        threadPoolContainer.createMonitor(createMonitorParam.getRequestId(),createMonitorParam.getThreadPoolParam());
    }

    @PostMapping("/submitJob")
    public void createMonitor(@RequestParam("requestId")String requestId,@RequestParam("executeTime")Long executeTime){
        threadPoolContainer.submitJob(requestId,executeTime);
    }

    @GetMapping("/monite/{requestId}")
    public void createMonitor(@PathVariable("requestId")String requestId){
        threadPoolContainer.monite(requestId);
    }

}
