package com.epam.course.java.se;

public class Main {
    public static void main(String[] args) {
        final Thread thread1 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    System.out.println("Thread; i = " + i);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };


        final Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i1 = 0; i1 < 10; i1++) {
                    System.out.println("Runnable; i = " + i1);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

//        thread1.setDaemon(true);
        thread2.setDaemon(true);

        thread1.start();
        thread2.start();

//        try {
//            thread1.join();
//            thread2.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        for (int i = 0; i < 10; i++) {
            System.out.println("Main; i = " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
