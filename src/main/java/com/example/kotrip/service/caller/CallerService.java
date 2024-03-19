package com.example.kotrip.service.caller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CallerService {
    private final AsyncService asyncService;

    public void callAsync(){
        log.info("[Async Method 정상호출]");
        asyncService.asyncReceiver1();
        asyncService.asyncReceiver2();
    }
}
