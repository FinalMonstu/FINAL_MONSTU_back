package com.icetea.MonStu.shared.async;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

// 상속 금지 (final)
public final class SemaphoreUtils {

    private SemaphoreUtils() { }

    // 반환값이 있는 경우 (Supplier<T>)
    public static <T> T withPermit(Supplier<T> work,Semaphore permits) {
        return withPermit(work, permits, 1, TimeUnit.SECONDS);
    }

    public static <T> T withPermit(Supplier<T> work,Semaphore permits,long timeout,TimeUnit timeUnit) {
        boolean acquired = false;
        try {
            acquired = permits.tryAcquire(timeout, timeUnit);
            if (!acquired) throw new RejectedExecutionException("Traffic limit exceeded (Busy)");
            return work.get();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt(); // 스레드 플래스 활성화-상위 메소드에 전달하기 위해 ( 예외에 의해 플래그가 켜지면 JVM이 자동으로 플래그 내림)
            throw new RuntimeException(ie);
        } finally {
            if (acquired) permits.release();
        }
    }


    // 반환값이 없는 경우 (Runnable)
    public static void runWithPermit(Runnable task, Semaphore permits) {
        runWithPermit(task, permits, 3, TimeUnit.SECONDS);
    }

    public static void runWithPermit(Runnable task, Semaphore permits, long timeout, TimeUnit timeUnit) {
        withPermit(() -> {
            task.run();
            return null; // Void 처리
        }, permits, timeout, timeUnit);
    }


}
