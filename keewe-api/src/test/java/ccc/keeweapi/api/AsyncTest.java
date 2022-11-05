package ccc.keeweapi.api;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class AsyncTest {

    @Test
    void test() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> cFuture = new CompletableFuture<>();

        Executors.newSingleThreadExecutor().submit(() -> {
            cFuture.complete(1);
        });

        System.out.println(cFuture.get());
    }
}
