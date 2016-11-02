package com.epam.course.java.se.data;

public class StaticAccount {
    private static long balance;

    private StaticAccount() {

    }

    public static long getBalance() {
        return balance;
    }

    public static /*synchronized*/ void deposit(int ammount) {
        synchronized (StaticAccount.class) {
            long local = balance;
            balance = local + ammount;
        }
    }

    public static synchronized void withdraw(int ammount) {
//        synchronized (StaticAccount.class) {
        long local = balance;
        balance = local - ammount;
//        }
    }
}
