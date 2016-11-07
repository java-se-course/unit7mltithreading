package com.epam.course.java.se;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExeciutorsTest {
    private Runnable createTask(int i) {
        return () -> {
            try {
                System.out.println("Start " + i);
                Thread.sleep(i*5);
                System.out.println("End " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }

    private Thread createThread(List<Runnable> runnables) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " start");
                for (Runnable runnable : runnables) {
                    runnable.run();
                }
                System.out.println(Thread.currentThread().getName() + " end");
            }
        });
    }
    // обедающие мудрецы

    @Test
    public void test1() throws InterruptedException {
        final List<Runnable> tasks = generateTasks();

        final Thread thread1 = createThread(tasks.subList(0, 25));
        final Thread thread2 = createThread(tasks.subList(25, 50));
        final Thread thread3 = createThread(tasks.subList(50, 75));
        final Thread thread4 = createThread(tasks.subList(75, 100));

        final long start = System.currentTimeMillis();

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();

        System.out.println(System.currentTimeMillis() - start);
    }

    private List<Runnable> generateTasks() {
        final List<Runnable> tasks = new ArrayList<>(100);

        for (int i = 0; i < 100; i++) {
            tasks.add(i, createTask(i));
        }
        return tasks;
    }

    @Test
    public void test2() throws InterruptedException {
        final ExecutorService pool = Executors.newWorkStealingPool(4);
        final List<Runnable> runnables = generateTasks();

        final long start = System.currentTimeMillis();

        for (Runnable runnable : runnables) {
            pool.submit(runnable);
        }

        pool.shutdown();
        pool.awaitTermination(20, TimeUnit.SECONDS);

        System.out.println(System.currentTimeMillis() - start);
    }

    private Callable<Integer> createCallable(int i) {
        return new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                try {
                    System.out.println("Start " + i);
                    Thread.sleep(i * 5);
                    System.out.println("End " + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return i;
            }
        };
    }

    private List<Callable<Integer>> generateCallables() {
        final List<Callable<Integer>> tasks = new ArrayList<>(100);

        for (int i = 0; i < 100; i++) {
            tasks.add(i, createCallable(i));
        }
        return tasks;
    }
    @Test
    public void test3() throws Exception {
        final List<Callable<Integer>> callables = generateCallables();

        long sum = 0;

        for (Callable<Integer> callable : callables) {
            sum += callable.call();
        }

        System.out.println(sum);
    }

    @Test
    public void test4() throws ExecutionException, InterruptedException {
        final List<Callable<Integer>> callables = generateCallables();
        final ExecutorService pool = Executors.newWorkStealingPool(4);

        final List<Future<Integer>> results = new ArrayList<>(callables.size());

        for (Callable<Integer> callable : callables) {
            final Future<Integer> future = pool.submit(callable);
            results.add(future);
        }

        long sum = 0;

        for (Future<Integer> result : results) {
            sum += result.get();
        }

        System.out.println(sum);
    }

    @Test
    public void exceptions() {
        final ExecutorService pool = Executors.newWorkStealingPool(4);
        final Future<Integer> future = pool.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                throw new FileNotFoundException("Checked exception");
            }
        });

        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("Got exception: " + e);
            System.out.println("Cause: " + e.getCause());
            e.printStackTrace();
        }
    }

    @Test
    public void cancel() throws ExecutionException, InterruptedException {
        final ExecutorService pool = Executors.newSingleThreadExecutor();
        final Future<String> future = pool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                int result = 0;
                for (int i = 0; i < 100; i++) {
                    result += i;
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        return "Cancelled!";
                    }
                }
                return "Result: " + result;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                future.cancel(true);
            }
        }).start();

        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void cancel2() throws InterruptedException, ExecutionException {
        final ExecutorService pool = Executors.newSingleThreadExecutor();
        final Future<String> future = pool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                int result = 0;
                for (int i = 0; i < 100; i++) {
                    result += i;
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        System.out.println("Cancelled!");
                        return "Cancelled!";
                    }
                }

                return "Result: " + result;
            }
        });

        TimeUnit.MILLISECONDS.sleep(10);
        future.cancel(true);


        System.out.println(future.isDone());
        System.out.println(future.isCancelled());

        System.out.println(future.get());

    }
}
