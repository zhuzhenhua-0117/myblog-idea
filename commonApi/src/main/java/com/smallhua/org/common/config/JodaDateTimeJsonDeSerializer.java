package com.smallhua.org.common.config;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * 〈一句话功能简述〉<br>
 * 〈redis反序列化  日期格式定制〉
 *
 * @author ZZH
 * @create 2021/5/1
 * @since 1.0.0
 */
public class JodaDateTimeJsonDeSerializer extends JsonDeserializer<DateTime> {

    @Override
    public DateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String s = p.readValueAs(String.class);
        return DateTime.of(s,"yyyy-MM-dd HH:mm:ss");
    }
}