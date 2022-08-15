package com.smallhua.org.bussiness;

import cn.hutool.core.date.DateUtil;
import com.smallhua.org.common.util.IdGenerator;
import com.smallhua.org.mapper.TFileInfoMapper;
import com.smallhua.org.model.TFileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 〈一句话功能简述〉<br>
 * 〈文件业务功能〉
 *
 * @author ZZH
 * @create 2021/6/15
 * @since 1.0.0
 */
@Service
@Transactional
public class FileInfoService {

    @Autowired
    private TFileInfoMapper fileInfoMapper;

    public int addFileInfo(TFileInfo fileInfo) {
        fileInfo.setId(IdGenerator.generateIdBySnowFlake());
//        fileInfo.setCreId(SessionHelper.currentUserId());
        fileInfo.setCreTime(DateUtil.date());
        fileInfo.setUpdTime(DateUtil.date());
//        fileInfo.setUpdId(SessionHelper.currentUserId());
        return fileInfoMapper.insert(fileInfo);
    }
}