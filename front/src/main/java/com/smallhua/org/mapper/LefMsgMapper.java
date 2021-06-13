package com.smallhua.org.mapper;

import com.smallhua.org.model.TLeavemsgExample;
import com.smallhua.org.vo.LefMsgVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈自定义留言Mapper〉
 *
 * @author ZZH
 * @create 2021/6/13
 * @since 1.0.0
 */
@Mapper
public interface LefMsgMapper {

    List<LefMsgVo> selectByExample(TLeavemsgExample example);
}