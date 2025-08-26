package com.icetea.MonStu.async;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class BoundedQueueTaskExecutor extends ThreadPoolTaskExecutor {
    @Override
    protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
        // 공정 모드(true)로 만들면 FIFO 보장(약간의 처리량 손실 가능)
//        return new ArrayBlockingQueue<>(queueCapacity, false);
        return new SynchronousQueue<>();
    }
}
