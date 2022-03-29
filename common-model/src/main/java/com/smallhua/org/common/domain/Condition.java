package com.smallhua.org.common.domain;

import lombok.Data;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈条件封住〉
 *
 * @author ZZH
 * @create 2021/5/19
 * @since 1.0.0
 */
@Data
public class Condition {

    private String fieldName;

    private String op;

    private List<Object> values;

}