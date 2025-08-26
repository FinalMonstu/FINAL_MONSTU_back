package com.icetea.MonStu.async;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class AsyncManager {

    public static <T> CompletableFuture<Optional<T>> firstNonEmpty(CompletableFuture<Optional<T>>... cfs) {
        if (cfs == null || cfs.length == 0) return CompletableFuture.completedFuture(Optional.empty());

        CompletableFuture<Optional<T>> result = new CompletableFuture<>();
        AtomicInteger remaining = new AtomicInteger(cfs.length);

        for (CompletableFuture<Optional<T>> cf : cfs) {
            cf.handle((opt, ex) -> {
                if (ex == null && opt != null && opt.isPresent()) {
                    result.complete(opt);
                }
                if (remaining.decrementAndGet() == 0 && !result.isDone()) {
                    result.complete(Optional.empty());
                }
                return null;
            });
        }
        return result;
    }


    public static <T> CompletableFuture<Optional<T>> firstNonEmptyCancelOthers(CompletableFuture<Optional<T>>... cfs) {
        CompletableFuture<Optional<T>> r = firstNonEmpty(cfs);
        r.thenAccept(opt -> {
            if (opt.isPresent()) {
                for (CompletableFuture<Optional<T>> cf : cfs) {
                    cf.cancel(true);
                }
            }
        });
        return r;
    }
}
