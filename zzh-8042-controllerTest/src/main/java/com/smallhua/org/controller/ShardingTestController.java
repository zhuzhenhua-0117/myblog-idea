package com.smallhua.org.controller;

import com.smallhua.org.bussiness.shardingJDBC.model.TOrder;
import com.smallhua.org.bussiness.shardingJDBC.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈分库分表测试〉
 *
 * @author ZZH
 * @create 2022/8/15
 * @since 1.0.0
 */
@RestController
@RequestMapping("/fkfb")
@Slf4j
public class ShardingTestController {

    @Autowired
    private OrderService orderService;


    @PostMapping("/save")
    public String save(@RequestBody TOrder tOrder) {
        orderService.save(tOrder);
        return "success";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam(value = "id") Integer id) {
        orderService.delete(id);
        return "success";
    }

    @PostMapping("/update")
    public int update(@RequestBody TOrder tOrder) {
        return orderService.update(tOrder);
    }

    @GetMapping("/getList")
    public List<TOrder> getList() {
        return orderService.getList();
    }

}