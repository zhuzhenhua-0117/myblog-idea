package com.smallhua.org.bussiness.remotedao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author ZZH
 * @create 2022/8/18
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlResult<T> {

    private List<T> data;

    private Date lastTime;

}