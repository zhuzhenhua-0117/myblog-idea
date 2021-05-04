package com.smallhua.org.front.dto;

import com.smallhua.org.model.TRole;
import com.smallhua.org.model.TUser;
import lombok.Data;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈封装用户角色信息〉
 *
 * @author ZZH
 * @create 2021/4/25
 * @since 1.0.0
 */
@Data
public class UserRole extends TUser {

    private List<TRole> roles;

}