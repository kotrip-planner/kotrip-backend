package com.example.kotrip.service.caller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncService {

    @Async("taskExecutor1")
    public void asyncReceiver1(){
        log.info("[asyncReceiver1()]");
        for (int i = 0; i < 5; i++) {
            log.info("====Thread Name : " + Thread.currentThread().getName());
        }
    }

    @Async("taskExecutor2")
    public void asyncReceiver2(){
        log.info("[asyncReceiver2()]");
        for (int i = 0; i < 5; i++) {
            log.info("====Thread Name : " + Thread.currentThread().getName());
        }
    }
}
