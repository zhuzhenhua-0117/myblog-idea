package com.smallhua.org.controller;

import com.smallhua.org.model.ChunkVo;
import com.smallhua.org.model.TFileInfo;
import com.smallhua.org.service.ChunkService;
import com.smallhua.org.service.FileInfoService;
import com.smallhua.org.util.FileUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 〈一句话功能简述〉<br>
 * 〈文件管理〉
 *
 * @author ZZH
 * @create 2021/6/13
 * @since 1.0.0
 */
@RestController
@Api(tags = "文件控制器")
@RequestMapping("file")
@Slf4j
public class FileController {

    @Value("${prop.upload-folder}")
    private String uploadFolder;
    @Resource
    private FileInfoService fileInfoService;
    @Resource
    private ChunkService chunkService;

    @PostMapping("/public/chunk")
    public String uploadChunk(ChunkVo chunk) {
        MultipartFile file = chunk.getFile();
        log.debug("file originName: {}, chunkNumber: {}", file.getOriginalFilename(), chunk.getChunkNumber());
        String folder = System.getProperty("user.dir") + File.separator + uploadFolder;
        try {
            //前端传入的参数为filename 而实体类中属性为fileName
            String originalFilename = file.getOriginalFilename();
            chunk.setFileName(originalFilename.substring(0,originalFilename.lastIndexOf(".")));
            byte[] bytes = file.getBytes();
            Path path = Paths.get(FileUtils.generatePath(folder, chunk));
            //文件写入指定路径
            Files.write(path, bytes);
            log.debug("文件 {} 写入成功, uuid:{}", chunk.getFileName(), chunk.getIdentifier());
            chunkService.saveChunk(chunk);

            return "文件上传成功";
        } catch (IOException e) {
            e.printStackTrace();
            return "后端异常...";
        }
    }

    @GetMapping("/public/chunk")
    public Object checkChunk(ChunkVo chunk, HttpServletResponse response) {
        if (chunkService.checkChunk(chunk.getIdentifier(), chunk.getChunkNumber())) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        }
        return chunk;
    }

    @PostMapping("/public/mergeFile")
    public String mergeFile(@RequestBody TFileInfo fileInfo) {
        String folder_prefix = System.getProperty("user.dir") + File.separator + uploadFolder;
        String filename = fileInfo.getFileName();
        String folder = folder_prefix + File.separator +fileInfo.getIdentifier();
        String file = folder + File.separator + filename;
        FileUtils.merge(file, folder, filename);
        fileInfo.setLocation(file);
        fileInfoService.addFileInfo(fileInfo);

        return "合并成功";
    }

}