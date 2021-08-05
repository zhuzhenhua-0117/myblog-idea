package com.smallhua.org.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 〈一句话功能简述〉<br>
 * 〈打印水〉
 *
 * @author ZZH
 * @create 2021/8/3
 * @since 1.0.0
 */
public class PrintH2O {
    public static void main(String[] args) {
        Runnable printH = () -> {
            System.out.print("H");
        };
        Runnable printO = () -> {
            System.out.print("O");
        };

        H2O h2O = new H2O();

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 20; i++) {
            executorService.execute(() -> {
                try {
                    h2O.produceH(printH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                try {
                    h2O.produceO(printO);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
class H2O{
    private volatile int hNnm = 0;
    private volatile int oNum = 0;
    private ReentrantLock lock = new ReentrantLock();
    private final Condition hCondition = lock.newCondition();
    private final Condition oCondition = lock.newCondition();

    public void produceH(Runnable printH) throws InterruptedException {
        try {
            lock.lock();
            while (hNnm == 2) {
                hCondition.await();
            }
            hNnm++;
            printH.run();
            if (hNnm == 2 && oNum == 1){
                hNnm = 0;
                oNum = 0;
                System.out.print("\n");
                oCondition.signalAll();
            }
        }finally {
            lock.unlock();
        }
    }

    public void produceO(Runnable printO) throws InterruptedException {
        lock.lock();
        try {
            while (oNum == 1) {
                oCondition.await();
            }
            oNum++;
            printO.run();
            if (hNnm == 2 && oNum == 1){
                hNnm = 0;
                oNum = 0;
                System.out.print("\n");
                hCondition.signalAll();
            }
        }finally {
            lock.unlock();
        }
    }
}