package net.formula97.stepscopedtasklettestex.service;

import org.springframework.stereotype.Service;

@Service
public class Some1Service {
    public void doSomething1() {
        // 何か処理する
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
