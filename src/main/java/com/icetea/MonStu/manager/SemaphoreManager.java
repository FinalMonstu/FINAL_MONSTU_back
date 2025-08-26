package com.icetea.MonStu.manager;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class SemaphoreManager {

    public static <T> T withPermit(Supplier<T> work,Semaphore permits) {
        return withPermit(work, permits, 1, TimeUnit.SECONDS);
    }

    public static <T> T withPermit(Supplier<T> work,Semaphore permits,int wait,TimeUnit timeUnit) {
        boolean acquired = false;
        try {
            acquired = permits.tryAcquire(wait, timeUnit);
            if (!acquired) throw new RejectedExecutionException("Busy");
            return work.get();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(ie);
        } finally {
            if (acquired) permits.release();
        }
    }



}
