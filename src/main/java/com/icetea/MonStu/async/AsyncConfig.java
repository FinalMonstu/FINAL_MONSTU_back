package com.icetea.MonStu.async;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.*;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "ioExecutor", destroyMethod = "shutdown")
    public ExecutorService ioExecutor(TaskDecorator mdcDecorator) {
        ExecutorService base = Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual().name("vt-io-", 1).factory()
        );
//        ExecutorService withSecurity = new DelegatingSecurityContextExecutorService(base);

        return new TaskDecoratingExecutorService(base, mdcDecorator);
    }

    @Bean(name = "cpuExecutor")
    public ThreadPoolTaskExecutor cpuExecutor(TaskDecorator mdc) {
        int cores = Runtime.getRuntime().availableProcessors();
        int workers = Math.max(1, cores - 1);
        var ex = new BoundedQueueTaskExecutor();
        ex.setCorePoolSize(workers);
        ex.setMaxPoolSize(workers);
        ex.setQueueCapacity(0);   //SynchronousQueue
        ex.setThreadNamePrefix("cpu-");

        ex.setWaitForTasksToCompleteOnShutdown(true);
        ex.setAwaitTerminationSeconds(10);

        ex.setTaskDecorator(mdc);
        ex.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        ex.initialize();
        return ex;
    }

    @Bean
    public TaskDecorator mdcTaskDecorator() {
        return task -> {
            var captured = MDC.getCopyOfContextMap();
            return () -> {
                var prev = MDC.getCopyOfContextMap();
                if (captured != null) MDC.setContextMap(captured); else MDC.clear();
                try { task.run(); }
                finally {
                    if (prev != null) MDC.setContextMap(prev); else MDC.clear();
                }
            };
        };
    }

    /** TaskDecorator를 모든 작업에 적용하는 ExecutorService 래퍼 */
    static final class TaskDecoratingExecutorService extends AbstractExecutorService {
        private final ExecutorService delegate; private final TaskDecorator decorator;
        TaskDecoratingExecutorService(ExecutorService d, TaskDecorator t){ this.delegate=d; this.decorator=t; }
        @Override public void execute(Runnable c){ delegate.execute(decorator.decorate(c)); }
        @Override public <T> Future<T> submit(Callable<T> t){ var f=new FutureTask<>(t); delegate.execute(decorator.decorate(f)); return f; }
        @Override public Future<?> submit(Runnable t){ var f=new FutureTask<>(t,null); delegate.execute(decorator.decorate(f)); return f; }
        @Override public <T> Future<T> submit(Runnable t, T r){ var f=new FutureTask<>(t,r); delegate.execute(decorator.decorate(f)); return f; }
        @Override public void shutdown(){ delegate.shutdown(); }
        @Override public List<Runnable> shutdownNow(){ return delegate.shutdownNow(); }
        @Override public boolean isShutdown(){ return delegate.isShutdown(); }
        @Override public boolean isTerminated(){ return delegate.isTerminated(); }
        @Override public boolean awaitTermination(long to, TimeUnit u) throws InterruptedException { return delegate.awaitTermination(to,u); }
    }
}
