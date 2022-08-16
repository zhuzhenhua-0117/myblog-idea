package com.smallhua.org.bussiness.shardingJDBC.dao;

import com.smallhua.org.bussiness.shardingJDBC.model.TOrder;

import java.util.List;

public interface TOrderMapper {
    int insertSelective(TOrder record);

    List<TOrder> queryList();

    int updateByPrimaryKeySelective(TOrder record);

    int delete(Integer id);
}