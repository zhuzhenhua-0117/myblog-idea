package com.smallhua.org.bussiness.shardingJDBC.service;

import com.smallhua.org.bussiness.shardingJDBC.dao.TOrderMapper;
import com.smallhua.org.bussiness.shardingJDBC.model.TOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈订单service〉
 *
 * @author ZZH
 * @create 2022/8/15
 * @since 1.0.0
 */
@Service
public class OrderService {

    @Autowired
    private TOrderMapper orderMapper;


    public void save(TOrder tOrder) {
        orderMapper.insertSelective(tOrder);
    }

    public int update(TOrder tOrder) {
        return orderMapper.updateByPrimaryKeySelective(tOrder);
    }

    public List<TOrder> getList() {
        return orderMapper.queryList();
    }


    public int delete(Integer id) {
        return orderMapper.delete(id);
    }
}