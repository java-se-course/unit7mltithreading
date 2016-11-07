package com.epam.course.java.se;

import lombok.Data;
import lombok.NonNull;
import org.junit.Test;

import java.util.StringJoiner;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public class SLListTest {
    @Data
    private static class SingleLinkedList<T> {
        @NonNull private final T head;
        private final SingleLinkedList<T> tail;
    }

    private final AtomicReference<SingleLinkedList<String>> strings = new AtomicReference<>();

    private void add(String string,
                     AtomicReference<SingleLinkedList<String>> strings) {
        SingleLinkedList<String> oldValue;
        SingleLinkedList<String> newValue;

        do {
            oldValue = strings.get();
            newValue = new SingleLinkedList<>(string, oldValue);
        } while (!strings.compareAndSet(oldValue, newValue));
    }

    private String join(SingleLinkedList<String> strings) {
        final StringJoiner joiner = new StringJoiner("\n", "Strings:\n", "\nEND");
        int i = 0;
        while (strings != null) {
            joiner.add(strings.head);
            strings = strings.tail;
            i+=1;
        }
        return joiner.toString() + "\n Count: " + i;
    }

    private Runnable fillStrings(String prefix,
                                 int count,
                                 AtomicReference<SingleLinkedList<String>> strings) {
        return new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < count; i++) {
                    add(prefix + i, strings);
                }
            }
        };
    }

    @Test
    public void test() throws InterruptedException {
        final Thread thread1 = new Thread(fillStrings("Thread 1: ", 10000, strings));
        final Thread thread2 = new Thread(fillStrings("Thread 2: ", 10000, strings));
        final Thread thread3 = new Thread(fillStrings("Thread 3: ", 10000, strings));
        final Thread thread4 = new Thread(fillStrings("Thread 4: ", 10000, strings));

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();

        System.out.println(join(strings.get()));

        final PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<>();
        queue.poll(1)
    }
}
