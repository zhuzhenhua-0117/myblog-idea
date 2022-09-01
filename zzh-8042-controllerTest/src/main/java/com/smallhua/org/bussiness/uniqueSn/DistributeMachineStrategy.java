package com.smallhua.org.bussiness.uniqueSn;

public interface DistributeMachineStrategy<T> {

    void doProcess(T t);
}
