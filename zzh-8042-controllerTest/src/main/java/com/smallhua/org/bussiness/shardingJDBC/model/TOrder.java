package com.smallhua.org.bussiness.shardingJDBC.model;

import java.io.Serializable;
import lombok.Data;

/**
 * t_order2021
 * @author 
 */
@Data
public class TOrder implements Serializable {
    private Long id;

    private Integer userId;

    private Integer orderId;

    private String cloumn;

    private String dayDate;

    private static final long serialVersionUID = 1L;
}