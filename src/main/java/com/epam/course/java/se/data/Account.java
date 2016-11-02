package com.epam.course.java.se.data;

import java.util.concurrent.atomic.AtomicInteger;

public class Account {
    private long balance;
    private final long limit;
//    private final Object mutex = new Object();
    private static final AtomicInteger gen = new AtomicInteger(0);
    private final int id = gen.incrementAndGet();

    public Account(int balance) {
        this(balance, Long.MAX_VALUE);
    }

    public Account(int balance, long limit) {
        this.balance = balance;
        this.limit = limit;
    }

    public long getBalance() {
        return balance;
    }

    public int getId() {
        return id;
    }

    public /*synchronized*/ boolean deposit(int ammount) {
        synchronized (this) {
            long local = this.balance;
            if (limit - ammount < local) {
                return false;
            }

            this.balance = local + ammount;
            return true;
        }
    }

    public synchronized boolean withdraw(int ammount) {
//        synchronized (mutex) {
        long local = this.balance;
        if (local < ammount) {
            return false;
        }

        this.balance = local - ammount;
        return true;
//        }
    }
}
