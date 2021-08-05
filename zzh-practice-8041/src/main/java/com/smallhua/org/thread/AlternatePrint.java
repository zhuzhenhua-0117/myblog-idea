package com.smallhua.org.thread;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 〈一句话功能简述〉<br>
 * 〈交替打印〉
 *
 * @author ZZH
 * @create 2021/8/3
 * @since 1.0.0
 */
public class AlternatePrint {
    public static void main(String[] args) {
        Runnable printFoo = () -> {
            System.out.print("foo");
        };
        Runnable printBar = () -> {
            System.out.print("bar \n");
        };

//        FooBar fooBar = new FooBar(10);
        FooBar3 fooBar = new FooBar3(100);

        new Thread(() -> {
            try {
                fooBar.foo(printFoo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                fooBar.bar(printBar);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
//syncronized方式
class FooBar3{
    private int n;

    private volatile boolean flag = true;
    private final Object o = new Object();
    public FooBar3(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            synchronized (o) {
                while (!flag){
                    o.wait();
                }
                printFoo.run();
                flag = false;
                o.notifyAll();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            synchronized (o) {
                while (flag){
                    o.wait();
                }
                printBar.run();
                flag = true;
                o.notifyAll();
            }
        }
    }
}
//semaphore方式
class FooBar2{
    private int n;

    private Semaphore fooSemaphore = new Semaphore(1);
    private Semaphore barSemaphore = new Semaphore(0);

    public FooBar2(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {

        for (int i = 0; i < n; i++) {
            fooSemaphore.acquire();
            printFoo.run();
            barSemaphore.release();
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            barSemaphore.acquire();
            printBar.run();
            fooSemaphore.release();
        }
    }
}

//Reentrantlock + condition + 标识位
class FooBar1{
    private int n;

    private volatile boolean flag = true;
    ReentrantLock lock = new ReentrantLock();
    private final Condition fooCondition = lock.newCondition();
    private final Condition barCondition = lock.newCondition();

    public FooBar1(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {

        for (int i = 0; i < n; i++) {
           lock.lock();
           try {
              while (!flag){
                  fooCondition.await();
              }
              flag = false;
              printFoo.run();
              barCondition.signal();
           }finally {
               lock.unlock();
           }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            lock.lock();
            try {
                while (flag){
                    barCondition.await();
                }
                flag = true;
                printBar.run();
                fooCondition.signal();
            }finally {
                lock.unlock();
            }
        }
    }
}

//blockingqueue方式
class FooBar{
    private int n;
    private BlockingDeque<Integer> fooQueue = new LinkedBlockingDeque<>(1);
    private BlockingDeque<Integer> barQueue = new LinkedBlockingDeque<>(1);


    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            fooQueue.put(1);
            printFoo.run();
            barQueue.put(1);
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            barQueue.take();
            printBar.run();
            fooQueue.take();
        }
    }
}