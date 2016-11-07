package com.epam.course.java.se.data;

import lombok.Data;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CarStation {
    private final AtomicInteger gen = new AtomicInteger(0);

    private final Set<Car> cars = new HashSet<>();

//    private final Lock lock = new ReentrantLock();

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    @Data
    public static class Car {
        private final int id;
    }

    public Car createCar() {
        return new Car(gen.getAndIncrement());
    }

    public void addCar(Car car) {
//        lock.lock();
        rwLock.writeLock().lock();
        cars.add(car);
        rwLock.writeLock().unlock();
//        lock.unlock();
    }

    public String info() {
//        lock.lock();
        rwLock.readLock().lock();
        final StringJoiner sj = new StringJoiner("\n", "Car info:\n", "\n END");

        for (Car car : cars) {
            sj.add(car.toString());
        }

        rwLock.readLock().unlock();
//        lock.unlock();
        return sj.toString();
    }
}
