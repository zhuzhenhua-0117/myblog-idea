package com.smallhua.org.bussiness.remotedao;

import com.alibaba.schedulerx.worker.log.LogFactory;
import com.alibaba.schedulerx.worker.log.Logger;
import com.google.common.collect.Lists;
import com.newretail.common.exception.DefaultException;
import com.newretail.financial.nfms.bmw.remotedao.SqlStatement.TableColumn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.newretail.financial.nfms.bmw.remotedao.SqlStatement.DDlEnum.SELECT;


/**
 * 〈一句话功能简述〉<br>
 *
 * @author ZZH
 * @create 2022/8/18
 * @since 1.0.0
 */
public abstract class AbstractRemoteSqlOpt<T> {

    private static final Logger LOGGER = LogFactory.getLogger(AbstractRemoteSqlOpt.class);

    private static final String COMMON_CHARACTER = "*";
    private static final String SPACE = " ";

    public abstract JdbcTemplate getJdbcTemplate();

    public abstract Map<String, PropertiesMetadata> resultMapping();

    public SqlResult<T> query(SqlStatement statement){
        if (statement.getOperator() != SELECT) throw new DefaultException(9999, "operate invalid! not be select.");
        String sql = assembleQuery(statement.getSqlParam(), statement.getMainTable(), statement.getSubTable());

        LOGGER.info("sql：", sql);
        List<T> data = getJdbcTemplate().queryForObject(sql, (rs, rowNum) -> result(rs, rowNum));
        return new SqlResult(data, null);
    }

    public int insert(SqlStatement statement){

        return 0;
    }

    public int update(SqlStatement statement){

        return 0;
    }

    public int delete(SqlStatement statement){

        return 0;
    }

    private List<T> result(ResultSet rs, int rowNum){
        List<T> ret = Lists.newArrayList();
        try {
            ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
            Class aClass = (Class)genericSuperclass.getActualTypeArguments()[0];
            Map<String, PropertiesMetadata> mapping = resultMapping();

            if(CollectionUtils.isEmpty(mapping)) {
                return ret;
            }

            do {
                Object obj = aClass.getDeclaredConstructor().newInstance();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    PropertiesMetadata propertiesMetadata = mapping.get(columnName);
                    Field declaredField = null;
                    try{
                        declaredField = aClass.getDeclaredField(propertiesMetadata.getPropertyName());
                    }catch (NoSuchFieldException e){
                        declaredField = aClass.getSuperclass().getDeclaredField(propertiesMetadata.getPropertyName());
                    }
                    declaredField.setAccessible(true);
                    Object object;
                    if(propertiesMetadata.getDataType() == LocalDate.class) {
                        object = rs.getDate(columnName).toLocalDate();
                    } else if (propertiesMetadata.getDataType() == LocalTime.class){
                        object = rs.getTime(columnName).toLocalTime();
                    } else {
                        object = rs.getObject(columnName);
                    }
                    declaredField.set(obj, object);
                }
                ret.add((T) obj);
            } while (rs.next());
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn("处理结果异常：", e);
            throw new RuntimeException(e);
        }
    }

    private String assembleQuery(SqlParam sqlParam, TableColumn mainTable, TableColumn ...subTables){
        Assert.notNull(mainTable, "table name not null!");
        Assert.notNull(mainTable.getTableName(), "table name not null!");

        TableColumn var1 = mainTable;
        TableColumn[] var2 = subTables;
        List<String> cols_var1 = var1.getCols();
        processColumn(var1);

        List<String> cols = Lists.newArrayList(cols_var1);
        String queryCols;
        String joinTable = "";
        String whereStr;
        int offset = (sqlParam.getPageNum() - 1) * sqlParam.getPageSize(), end = offset + sqlParam.getPageSize();
        String limitStr = String.format("limit %d, %d", offset, end);

        for (TableColumn tableColumn : var2) {
            String tableName = tableColumn.getTableName();
            List<SqlCondition> joinOn = tableColumn.getJoinOn();
            if (StringUtils.isEmpty(tableName) || CollectionUtils.isEmpty(joinOn)) continue;

            processColumn(tableColumn);

            if (!CollectionUtils.isEmpty(tableColumn.getCols())) cols.addAll(tableColumn.getCols());

            String prefix = "JOIN ".concat(tableName).concat(" on ");
            joinTable = joinTable.concat(SPACE).concat(transferCondition(prefix, joinOn));
        }

        queryCols = CollectionUtils.isEmpty(cols) ? COMMON_CHARACTER : String.join(", ", cols);
        whereStr = transferCondition("",  sqlParam.getConditions());

        StringBuilder sb = new StringBuilder();
        sb.append(SELECT.getStatement().replace(SqlStatement.DDlEnum.MAIN_TABLE_COLS, queryCols).replace(SqlStatement.DDlEnum.MAIN_TABLE_NAME, var1.getTableName()));
        sb.append(SPACE).append(joinTable);
        if (!StringUtils.isEmpty(whereStr)) sb.append(" WHERE ").append(whereStr);
        if (!StringUtils.isEmpty(sqlParam.getOrderBy())) sb.append(SPACE).append(sqlParam.getOrderBy());

        sb.append(SPACE).append(limitStr);

        return sb.toString();
    }

    private String transferCondition(String prefix, List<SqlCondition> conditions){
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(SPACE);
        boolean flag = false;
        for (SqlCondition condition : conditions) {
            String keyTableName = condition.getKeyTableName() == null ? "" : condition.getKeyTableName();
            String valueTableName = condition.getValueTableName() == null ? "" : condition.getValueTableName();
            if(flag) sb.append(SPACE).append("AND"); else flag = true;
            sb.append(SPACE).append(keyTableName.concat(condition.getKey()))
                    .append(SPACE).append(condition.getOperatorEnum().getOpt())
                    .append(SPACE).append(valueTableName.concat(condition.getValue()));
        }
        return sb.toString();
    }

    private void processColumn(TableColumn tableColumn){
        List<String> cols = tableColumn.getCols();
        String tableName = tableColumn.getTableName();
        if (CollectionUtils.isEmpty(cols)) return;
        tableColumn.setCols(cols.stream().map(item -> tableName.concat(".").concat(item)).collect(Collectors.toList()));
    }

}