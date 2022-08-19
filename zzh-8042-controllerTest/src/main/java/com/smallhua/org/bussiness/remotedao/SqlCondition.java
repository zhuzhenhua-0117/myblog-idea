package com.smallhua.org.bussiness.remotedao;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author ZZH
 * @create 2022/8/18
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class SqlCondition {

    private String keyTableName;

    private String valueTableName;

    private String key;

    private String value;

    private OperatorEnum operatorEnum;

    public SqlCondition(String key, String value, OperatorEnum operatorEnum){
        this.key = key;
        this.value = value;
        this.operatorEnum = operatorEnum;
    }

    @Getter
    public enum OperatorEnum{
        EQUAL("="),
        GREATER(">"),
        LESS("<"),
        GE(">="),
        LE(">="),
        LIKE("LIKE");

        private String opt;

        OperatorEnum(String opt){
            this.opt = opt;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SqlCondition)) return false;
        SqlCondition condition = (SqlCondition) o;
        return Objects.equals(key, condition.key) &&
                Objects.equals(value, condition.value) &&
                operatorEnum == condition.operatorEnum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value, operatorEnum);
    }
}