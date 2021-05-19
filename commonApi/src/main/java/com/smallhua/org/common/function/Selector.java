package com.smallhua.org.common.function;

import java.util.List;

@FunctionalInterface
public interface Selector<T> {
    List<T> select();
}
