package com.epam.course.java.se;

public class MainExc {
    private static Runnable newExceptionGenerator(String name) {
        return () -> {
            throw new RuntimeException(name);
        };
    }

    public static void main(String[] args) {
        final Thread thread1 = new Thread(newExceptionGenerator("thread 1"));
        thread1.setName("thread1");

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.out.println("Uncaught exception default handler in thread " + t + ":" + e);
        });

        final Thread thread2 = new Thread(newExceptionGenerator("thread 2"));
        thread2.setName("thread2");

        final ThreadGroup threadGroup = new ThreadGroup("Thread Group 1") {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("Uncaught exception in thread " + t + ":" + e);
            }
        };
        final Thread thread3 = new Thread(threadGroup, newExceptionGenerator("thread 3"));
        thread3.setName("thread3");

        thread1.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("Uncaught exception handler in thread " + t + ":" + e);
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
