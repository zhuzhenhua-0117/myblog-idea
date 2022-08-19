package com.smallhua.org.bussiness.remotedao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈sql语句〉
 *
 * @author ZZH
 * @create 2022/8/18
 * @since 1.0.0
 */
@Data
public class SqlStatement {

    private String schemaName;

    private TableColumn mainTable;

    private DDlEnum operator;

    private SqlParam sqlParam;

    private TableColumn subTable;


    @Getter
    public enum DDlEnum {


        SELECT("select ".concat(DDlEnum.MAIN_TABLE_COLS).concat(" from ").concat(DDlEnum.MAIN_TABLE_NAME)),
        INSERT("insert into {1}"),
        UPDATE("update {1}"),
        delete("delete {1}"),
        ;

        public static final String MAIN_TABLE_COLS = "{0}";
        public static final String MAIN_TABLE_NAME = "{1}";
        private String statement;

        DDlEnum(String statement){ this.statement = statement;}


    }

    @Data
    @AllArgsConstructor
    public static class TableColumn {

        private String tableName;

        private List<String> cols;

        private List<SqlCondition> joinOn;

        public TableColumn(){

        }

        public TableColumn(String tableName, List<String> cols){
            this.tableName = tableName;
            this.cols = cols;
        }

    }

}
