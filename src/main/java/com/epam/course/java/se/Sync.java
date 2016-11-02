package com.epam.course.java.se;

import lombok.Data;

public class Sync {
    // T1             | T2
    // a1
    // a2
    // a3             | a_2_0
    // release(x)     | acquire(x)
    // a4             | a_2_1
    // a5             | a_2_2

    public static volatile int i = 0;
    public static int x = 0;

    public void t1() {
        x = 0;
        x = 1;
        i = 1; // release(1)
//        x = 2;
    }

    // ?0?
    // 000
    // 001
    // 101
    // 100

    // ?1?
    // ?11 (2)
    public void t2() {
        System.out.println(x);
        System.out.println(i); // acquire(?)
        System.out.println(x);
    }

    @Data
    class DataClass {
        private int i;
        private String str;
    }

    private static volatile DataClass dc;

    public void t1_1() {
        while (true) {
            final DataClass dcLocal = new DataClass();
            dcLocal.setI(1);
            dcLocal.setStr("str");

            dc = dcLocal;

//            dcLocal.setI(2);
        }
    }

    // T1                     | T2
    // w(dcLocal.i, 1)
    // w(dcLocal.str, "str")
    // w(dc, dcLocal)
    //                        | r(dc)

    public void t2_1() {
        System.out.println(dc);
    }


    @Data
    class DataClass2 {
        private final int i;
        private final String str;
    }

    private static DataClass2 dc2;
    private static final Object flag = new Object();

    private DataClass2 slowOperation() {
        return new DataClass2(1, "str");
    }

    public void t1_2() {
        synchronized (flag) {
            if (dc2 == null) {
                dc2 = slowOperation();
            }
        }
    }

    public void t2_2() {
        final DataClass2 localDc2;
        synchronized (flag) {
            localDc2 = dc2;
        }
        System.out.println(localDc2);
    }

    public synchronized void test1() {

    }

    public void test2() {
        synchronized (this) {

        }

    }
}
