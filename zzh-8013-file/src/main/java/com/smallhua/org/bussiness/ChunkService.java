package com.smallhua.org.bussiness;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.smallhua.org.common.util.IdGenerator;
import com.smallhua.org.mapper.TChunkMapper;
import com.smallhua.org.model.TChunk;
import com.smallhua.org.model.TChunkExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈分块业务功能〉
 *
 * @author ZZH
 * @create 2021/6/15
 * @since 1.0.0
 */
@Service
@Transactional
public class ChunkService {

    @Autowired
    private TChunkMapper chunkMapper;

    public void saveChunk(TChunk chunk) {
        chunk.setId(IdGenerator.generateIdBySnowFlake());
//        chunk.setCreId(SessionHelper.currentUserId());
        chunk.setCreTime(DateUtil.date());
        chunk.setUpdTime(DateUtil.date());
//        chunk.setUpdId(SessionHelper.currentUserId());
        chunkMapper.insert(chunk);
    }

    public boolean checkChunk(String identifier, Integer chunkNumber) {

        TChunkExample example = new TChunkExample();
        example.createCriteria()
                .andChunkNumberEqualTo(chunkNumber)
                .andIdentifierEqualTo(identifier);
        List<TChunk> tChunks = chunkMapper.selectByExample(example);
        return CollectionUtil.isEmpty(tChunks);
    }
}