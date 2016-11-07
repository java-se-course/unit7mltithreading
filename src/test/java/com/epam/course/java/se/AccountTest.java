package com.epam.course.java.se;

import com.epam.course.java.se.data.Account;
import com.epam.course.java.se.data.AccountImpl;
import com.epam.course.java.se.data.AccountWithReentrantLock;
import org.junit.Test;

public class AccountTest {

    // T1               | T2
    // a1
    // a2
    // volatile write x |
    // a3               | volatile read x
    //                  | a2_1


    // T1                            | T2
    // a1
    // a2
    // acquire(m): synchronized(m) { |
    // a3                            |
    // a4
    // release(m): }              -> | acquire(m): synchronized(m) {
    //                               | a2_1
    //                               | a2_2
    //                               | release(m): }



    // T1                        | T2 (t)         | T3 (or T1)
    // release(t): t.start()  -> | acquire: run { |
    //                           | a1             |
    //                           | a2             |
    //                           | release: }  -> | acquire(t): t.join()




    private Runnable deposit500(Account account) {
        return new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50000; i++) {
//                    synchronized (account) {
                    account.deposit(1);
//                    }
                }
            }
        };
    }

    private Runnable withdraw1000(Account account) {
        return new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20000; i++) {
                    account.withdraw(5);
                }
            }
        };
    }

    @Test
    public void sync() {
        final Account account = new AccountImpl(1500);

        final Runnable deposit500 = deposit500(account);
        final Runnable withdraw1000 = withdraw1000(account);

        System.out.println(account.getBalance());

        deposit500.run();
        System.out.println(account.getBalance());

        withdraw1000.run();
        System.out.println(account.getBalance());
    }

    @Test
    public void async() throws InterruptedException {
        final Account account = new AccountImpl(150000);

        final Runnable deposit500 = deposit500(account);
        final Runnable withdraw1000 = withdraw1000(account);

        final Thread threadDeposit = new Thread(deposit500);
        final Thread threadWithdraw = new Thread(withdraw1000);

        System.out.println(account.getBalance());

        threadDeposit.start();
        threadWithdraw.start();
        Thread.yield();

        threadDeposit.join();
        threadWithdraw.join();

        System.out.println(account.getBalance());
    }

    @Test
    public void asyncWithLock() throws InterruptedException {
        final Account account = new AccountWithReentrantLock(150000);

        final Runnable deposit500 = deposit500(account);
        final Runnable withdraw1000 = withdraw1000(account);

        final Thread threadDeposit = new Thread(deposit500);
        final Thread threadWithdraw = new Thread(withdraw1000);

        System.out.println(account.getBalance());

        threadDeposit.start();
        threadWithdraw.start();
        Thread.yield();

        threadDeposit.join();
        threadWithdraw.join();

        System.out.println(account.getBalance());
    }
}

