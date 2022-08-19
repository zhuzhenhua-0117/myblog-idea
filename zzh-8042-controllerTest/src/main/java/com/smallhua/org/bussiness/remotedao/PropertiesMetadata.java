package com.smallhua.org.bussiness.remotedao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 〈一句话功能简述〉<br>
 * 〈列data〉
 *
 * @author ZZH
 * @create 2022/8/19
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertiesMetadata {

    private String propertyName;

    private Class dataType;


}