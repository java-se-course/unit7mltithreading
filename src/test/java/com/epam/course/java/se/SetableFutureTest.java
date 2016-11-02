package com.epam.course.java.se;

import com.epam.course.java.se.data.SetableFuture;
import org.junit.Test;

public class SetableFutureTest {
    @Test
    public void test1() throws InterruptedException {
        final SetableFuture<String> setableFuture = new SetableFuture<>();

        Thread setter = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setableFuture.set("Result!");
            }
        });

        Thread getter1 = new Thread(createGetter(setableFuture, "getter1"));
        Thread getter2 = new Thread(createGetter(setableFuture, "getter2"));

        getter1.start();
        getter2.start();
        setter.start();

        getter1.join();
        getter2.join();
        setter.join();
    }

    private Runnable createGetter(final SetableFuture<String> setableFuture, String name) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(name + ": " + setableFuture.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
