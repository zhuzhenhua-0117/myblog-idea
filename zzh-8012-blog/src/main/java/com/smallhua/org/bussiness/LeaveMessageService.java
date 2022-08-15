package com.smallhua.org.bussiness;

import cn.hutool.core.date.DateUtil;
import com.smallhua.org.common.api.CommonPage;
import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.common.domain.BaseParam;
import com.smallhua.org.common.util.ConditionUtil;
import com.smallhua.org.common.util.ConstUtil;
import com.smallhua.org.common.util.IdGenerator;
import com.smallhua.org.common.util.PageUtil;
import com.smallhua.org.mapper.LefMsgMapper;
import com.smallhua.org.mapper.TLeavemsgMapper;
import com.smallhua.org.model.TLeavemsg;
import com.smallhua.org.model.TLeavemsgExample;
import com.smallhua.org.util.SessionHelper;
import com.smallhua.org.vo.LefMsgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈留言功能〉
 *
 * @author ZZH
 * @create 2021/6/12
 * @since 1.0.0
 */
@Service
@Transactional
public class LeaveMessageService {

    @Autowired
    private TLeavemsgMapper tLeavemsgMapper;
    @Autowired
    private MessageService messageService;
    @Autowired
    private LefMsgMapper lefMsgMapper;


    public CommonResult saveOrUpdateLeaveMsg(TLeavemsg leaveMsg) {
        Long id = leaveMsg.getId()==null ? IdGenerator.generateIdBySnowFlake() : leaveMsg.getId();
        leaveMsg.setSourceId(SessionHelper.currentUserId());
        leaveMsg.setUpdTime(DateUtil.date());
        leaveMsg.setUpdId(SessionHelper.currentUserId());

        if (leaveMsg.getPid() == 0l) {
            leaveMsg.setFullPath("/" + id);
        } else {
            StringBuffer sb = new StringBuffer();
            TLeavemsgExample example = new TLeavemsgExample();
            example.createCriteria()
                    .andIdEqualTo(leaveMsg.getPid())
                    .andIsDelEqualTo(ConstUtil.ZERO);
            List<TLeavemsg> tComments = tLeavemsgMapper.selectByExample(example);
            sb.append(tComments.get(0).getFullPath()).append("/").append(id);

            leaveMsg.setFullPath(sb.toString());
        }

        if (leaveMsg.getId() == null) {
            //新增
            leaveMsg.setId(id);
            leaveMsg.setCreId(SessionHelper.currentUserId());
            leaveMsg.setCreTime(DateUtil.date());
            tLeavemsgMapper.insertSelective(leaveMsg);
            messageService.pushLeaveMessage(leaveMsg);
        }else {
            tLeavemsgMapper.updateByPrimaryKeySelective(leaveMsg);
        }

        return CommonResult.success(leaveMsg);
    }

    public CommonResult delLeaveMsg(Long id) {
//        tLeavemsgMapper.deleteByPrimaryKey(id);
        TLeavemsg leaveMsg = new TLeavemsg();
        leaveMsg.setId(id);
        leaveMsg.setIsDel((byte) 1);
        leaveMsg.setUpdTime(DateUtil.date());
        leaveMsg.setUpdId(SessionHelper.currentUserId());

        tLeavemsgMapper.updateByPrimaryKeySelective(leaveMsg);
        return CommonResult.success(null);
    }

    public CommonPage leaveMessageList(BaseParam baseParam) {
        TLeavemsgExample leavemsgExample = new TLeavemsgExample();
        leavemsgExample.setOrderByClause("CRE_TIME DESC");
        TLeavemsgExample.Criteria criteria = leavemsgExample.createCriteria();
        criteria.andIsDelEqualTo(ConstUtil.ZERO)
        .andPidEqualTo(0l);

        ConditionUtil.createCondition(baseParam, criteria, LefMsgVo.class);

        return PageUtil.pagination(baseParam, () -> lefMsgMapper.selectByExample(leavemsgExample));
    }
}