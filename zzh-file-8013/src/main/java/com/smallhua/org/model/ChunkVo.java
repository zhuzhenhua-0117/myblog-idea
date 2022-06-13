package com.smallhua.org.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 〈一句话功能简述〉<br>
 * 〈分块文件上传vo〉
 *
 * @author ZZH
 * @create 2021/6/15
 * @since 1.0.0
 */
@Data
public class ChunkVo extends TChunk {

    private MultipartFile file;

}