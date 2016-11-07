package com.epam.course.java.se.data;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicAccount {
    private final AtomicInteger balance;

    private static int decrementAngGet(AtomicInteger atomicInteger, int delta) {
        int oldValue;
        int newValue;

        do {
            oldValue = atomicInteger.get();
            newValue = oldValue - delta;
        } while (!atomicInteger.compareAndSet(oldValue, newValue));
        // CAS
        // Compare And Swap
        return newValue;
    }

    public AtomicAccount(int balance) {
        this.balance = new AtomicInteger(balance);
    }

    public long getBalance() {
        return balance.get();
    }

    public void deposit(int ammount) {
        this.balance.addAndGet(ammount);
    }

    public void withdraw(int ammount) {
        this.balance.addAndGet(-ammount);
//        this.balance.getAndAdd(-ammount);
    }
}
