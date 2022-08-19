package com.smallhua.org.bussiness.remotedao;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author ZZH
 * @create 2022/8/18
 * @since 1.0.0
 */
@Data
public class SqlParam {

    private List<SqlCondition> conditions;

    // 排序条件
    private String orderBy;

    private int pageSize;

    private int pageNum;

    private int total;

    public SqlParam(){
        this(1, 2000, null);
    }

    public SqlParam(String orderBy){
        this(1, 2000, orderBy);
    }

    public SqlParam(int pageNum, int pageSize){
        this(pageNum, pageSize, null);
    }
    public SqlParam(int pageNum, int pageSize, String orderBy){
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.orderBy = orderBy;
    }

    public SqlParam addCondition(SqlCondition sqlCondition){
        if (CollectionUtils.isEmpty(conditions) ) conditions = Lists.newArrayList();
        if(conditions.contains(sqlCondition)) conditions.remove(sqlCondition);

        conditions.add(sqlCondition);

        return this;
    }

}