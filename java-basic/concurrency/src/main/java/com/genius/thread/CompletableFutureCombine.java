package com.genius.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.genius.thread.ThreadExample.f;
import static com.genius.thread.ThreadExample.g;

public class CompletableFutureCombine {

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		int x = 1337;
		CompletableFuture<Integer> a = new CompletableFuture<>();
		CompletableFuture<Integer> b = new CompletableFuture<>();
		CompletableFuture<Integer> c = a.thenCombine(b, (y, z) -> y + z);
		executorService.submit(() -> a.complete(f(x)));
		executorService.submit(() -> b.complete(g(x)));
		System.out.println(c.get());
		executorService.shutdown();
	}
}
