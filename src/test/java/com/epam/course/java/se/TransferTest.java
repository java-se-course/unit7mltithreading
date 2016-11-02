package com.epam.course.java.se;

import com.epam.course.java.se.data.Account;
import org.junit.Test;

public class TransferTest {
    private void transfer(Account from, Account to, int amount) {
        final Account ac1;
        final Account ac2;

        if (from.getId() < to.getId()) {
            ac1 = from;
            ac2 = to;
        } else {
            ac1 = to;
            ac2 = from;
        }

        synchronized (ac1) {
            synchronized (ac2) {
                if (from.withdraw(amount)) {
                    if (!to.deposit(amount)) {
                        from.deposit(amount);
                    }
                }
            }
        }
    }

    @Test
    public void test1() throws InterruptedException {
        final Account account1 = new Account(50000, 100000);
        final Account account2 = new Account(50000, 100000);

        final Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    transfer(account1, account2, 2);
                }
            }
        });

        final Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20000; i++) {
                    transfer(account2, account1, 1);
                }
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(account1.getBalance());
        System.out.println(account2.getBalance());
    }
}
