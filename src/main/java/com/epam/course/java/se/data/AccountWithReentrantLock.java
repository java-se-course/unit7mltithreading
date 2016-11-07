package com.epam.course.java.se.data;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AccountWithReentrantLock implements Account {
    private long balance;
    private long limit;

    final Lock lock = new ReentrantLock();

    public AccountWithReentrantLock(int balance) {
        this(balance, Long.MAX_VALUE);
    }

    public AccountWithReentrantLock(long balance, long limit) {
        this.balance = balance;
        this.limit = limit;
    }

    public long getBalance() {
        return balance;
    }

    public long getLimit() {
        return limit;
    }

    public AccountWithReentrantLock copyWithIncreasedLimit(int add) {
        lock.lock();
        final AccountWithReentrantLock result =
                new AccountWithReentrantLock(balance, limit + add);
        lock.unlock();
        return result;
    }

    public boolean setLimit(long limit) {
        lock.lock();
        try {
            if (limit < balance) {
                return false;
            }
            this.limit = limit;
            return true;
        } finally {
            lock.unlock();
        }
    }

    public boolean deposit(int ammount) {
        lock.lock();
        try {
            long local = this.balance;
            if (limit - ammount < local) {
                return false;
            }

            this.balance = local + ammount;
            return true;
        } finally {
            lock.unlock();
        }
    }

    public boolean withdraw(int ammount) {
        lock.lock();
        try {
            long local = this.balance;
            if (local < ammount) {
                return false;
            }

            this.balance = local - ammount;
            return true;
        } finally {
            lock.unlock();
        }
    }
}
