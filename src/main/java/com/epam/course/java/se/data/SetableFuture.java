package com.epam.course.java.se.data;

import java.util.Objects;

public class SetableFuture<T> {
    private T value;
    private final Object mutex = new Object();

    public T get() throws InterruptedException {
        synchronized (mutex) {
            while (value == null) {
                mutex.wait();
            }

            return value;
        }
    }

    public void set(T value) {
        synchronized (mutex) {
            if (this.value != null) {
                return;
            }
            this.value = Objects.requireNonNull(value);
            mutex.notifyAll();
        }
    }
}
