package com.icetea.MonStu.shared.config;

import com.icetea.MonStu.shared.async.BoundedQueueTaskExecutor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.concurrent.*;

@Configuration
@EnableAsync
public class AsyncConfig {


    @Bean(name = "ioExecutor")
    public AsyncTaskExecutor ioExecutor(TaskDecorator taskDecorator) {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("vt-io-");
        executor.setVirtualThreads(true);
        executor.setTaskDecorator(taskDecorator);
        return executor;
    }

    @Bean(name = "cpuExecutor")
    public ThreadPoolTaskExecutor cpuExecutor(TaskDecorator mdc) {
        int cores = Runtime.getRuntime().availableProcessors();
        int workers = Math.max(1, cores - 1);  // CPU 코어가 1개일 경우 에러 방지를 위해 최소 1 보장
        var ex = new BoundedQueueTaskExecutor();
        ex.setCorePoolSize(workers);
        ex.setMaxPoolSize(workers);
        ex.setQueueCapacity(0);   //SynchronousQueue
        ex.setThreadNamePrefix("cpu-");

        ex.setWaitForTasksToCompleteOnShutdown(true);
        ex.setAwaitTerminationSeconds(10);

        ex.setTaskDecorator(mdc);
        ex.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        ex.initialize();
        return ex;
    }


    // 통합 데코레이터 (MDC + Security)
    @Bean
    public TaskDecorator contextPropagatingTaskDecorator() {
        return runnable -> {
            // 캡처 (메인 스레드)
            Map<String, String> mdcContext = MDC.getCopyOfContextMap();
            SecurityContext securityContext = SecurityContextHolder.getContext();

            return () -> {
                // 복구 (비동기 스레드)
                if (mdcContext != null) MDC.setContextMap(mdcContext);
                else MDC.clear();

                if (securityContext != null) {
                    SecurityContextHolder.setContext(securityContext);
                }

                try {
                    runnable.run();
                } finally {
                    // 정리 (스레드 풀 재사용 시 오염 방지)
                    MDC.clear();
                    SecurityContextHolder.clearContext();
                }
            };
        };
    }

    /** TaskDecorator를 모든 작업에 적용하는 ExecutorService 래퍼 */
//    static final class TaskDecoratingExecutorService extends AbstractExecutorService {
//        private final ExecutorService delegate; private final TaskDecorator decorator;
//        TaskDecoratingExecutorService(ExecutorService d, TaskDecorator t){ this.delegate=d; this.decorator=t; }
//        @Override public void execute(Runnable c){ delegate.execute(decorator.decorate(c)); }
//        @Override public <T> Future<T> submit(Callable<T> t){ var f=new FutureTask<>(t); delegate.execute(decorator.decorate(f)); return f; }
//        @Override public Future<?> submit(Runnable t){ var f=new FutureTask<>(t,null); delegate.execute(decorator.decorate(f)); return f; }
//        @Override public <T> Future<T> submit(Runnable t, T r){ var f=new FutureTask<>(t,r); delegate.execute(decorator.decorate(f)); return f; }
//        @Override public void shutdown(){ delegate.shutdown(); }
//        @Override public List<Runnable> shutdownNow(){ return delegate.shutdownNow(); }
//        @Override public boolean isShutdown(){ return delegate.isShutdown(); }
//        @Override public boolean isTerminated(){ return delegate.isTerminated(); }
//        @Override public boolean awaitTermination(long to, TimeUnit u) throws InterruptedException { return delegate.awaitTermination(to,u); }
//    }
}
