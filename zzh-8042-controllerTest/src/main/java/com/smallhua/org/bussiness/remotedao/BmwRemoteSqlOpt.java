package com.smallhua.org.bussiness.remotedao;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author ZZH
 * @create 2022/8/19
 * @since 1.0.0
 */
@Component
@Slf4j
public class BmwRemoteSqlOpt extends AbstractRemoteSqlOpt {

    public BmwRemoteSqlOpt(){
        super();
    }

    @Autowired
    private JdbcTemplate bmwJdbcTemplate;

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return bmwJdbcTemplate;
    }

    @Override
    public Map<String, PropertiesMetadata> resultMapping() {
        Map<String, PropertiesMetadata> ret = Maps.newHashMap();
        // 银行账号
        ret.put("ACCNBR", new PropertiesMetadata("bankAccount", String.class));
        // 流水号
        ret.put("TRXNBR", new PropertiesMetadata("bankFlowNo", String.class));
        // 交易日期
        ret.put("TRXDAT", new PropertiesMetadata("trxDate", LocalDate.class));
        // 交易时间
        ret.put("TRXTIM", new PropertiesMetadata("trxTime", LocalTime.class));
        // 币种
        ret.put("CCYNBR", new PropertiesMetadata("currency", String.class));
        // 加盟商
        ret.put("franchisee_no", new PropertiesMetadata("franchiseeNo", String.class));
        // 加盟商
        ret.put("franchisee_name", new PropertiesMetadata("franchiseeName", String.class));
        // 交易金额
        ret.put("TRXAMT", new PropertiesMetadata("taxAmount", BigDecimal.class));
        // 收(付)方账号
        ret.put("RPYACC", new PropertiesMetadata("receiveNo", String.class));
        // 收(付)方账号名称
        ret.put("RPYNAM", new PropertiesMetadata("receiveName", String.class));
        // 交易摘要
        ret.put("TRXTXT", new PropertiesMetadata("transactionRemark", String.class));
        return ret;
    }
}