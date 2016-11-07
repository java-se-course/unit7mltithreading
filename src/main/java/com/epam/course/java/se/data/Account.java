package com.epam.course.java.se.data;

/**
 * @author Simon Popugaev
 */
public interface Account {
    long getBalance();

    boolean deposit(int ammount);

    boolean withdraw(int ammount);
}
