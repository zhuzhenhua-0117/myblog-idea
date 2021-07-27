package com.smallhua.org.util;

import com.smallhua.org.model.ChunkVo;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

/**
 * 〈一句话功能简述〉<br>
 * 〈文件上传工具类〉
 *
 * @author ZZH
 * @create 2021/6/15
 * @since 1.0.0
 */
@Slf4j
public class FileUtils {
    public static String generatePath(String uploadFolder, ChunkVo chunk) {
        StringBuilder sb = new StringBuilder();
        sb.append(uploadFolder).append("/").append(chunk.getIdentifier());
        //判断uploadFolder/identifier 路径是否存在，不存在则创建
        if (!Files.isWritable(Paths.get(sb.toString()))) {
            log.info("path not exist,create path: {}", sb.toString());
            try {
                Files.createDirectories(Paths.get(sb.toString()));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        return sb.append("/")
                .append(chunk.getFileName())
                .append("-")
                .append(chunk.getChunkNumber()).toString();
    }

    /**
     * 文件合并
     *
     * @param targetFile
     * @param folder
     */
    public static void merge(String targetFile, String folder, String filename) {
        try {
            Files.createFile(Paths.get(targetFile));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        try (Stream<Path> stream = Files.list(Paths.get(folder))) {
            stream.filter(path -> !path.getFileName().toString().equals(filename))
                    .sorted((o1, o2) -> {
                        String p1 = o1.getFileName().toString();
                        String p2 = o2.getFileName().toString();
                        int i1 = p1.lastIndexOf("-");
                        int i2 = p2.lastIndexOf("-");
                        return Integer.valueOf(p2.substring(i2)).compareTo(Integer.valueOf(p1.substring(i1)));
                    })
                    .forEach(path -> {
                        try {
                            //以追加的形式写入文件
                            Files.write(Paths.get(targetFile), Files.readAllBytes(path), StandardOpenOption.APPEND);
                            //合并后删除该块
                            Files.delete(path);
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    });
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

//    public static void main(String[] args) {
//        List<Integer> integers = Arrays.asList(1, 2, 3);
//        integers.stream().sorted((o1, o2) -> Integer.valueOf(o2).compareTo(o1)).forEach(System.out::println);
//    }
}