package com.geovis.luoning.utils;

import com.geovis.luoning.exception.ResException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * @author jay
 * @date 2023/4/23 15:36
 */

@Slf4j
@Configuration
public class ImageUtility {
    private static String staticDir;

    public static String fileUpLoad(MultipartFile file, String dir) {
        String filePath = staticDir + dir;
        // 自定义的文件名称
        String trueFileName = UUID.randomUUID().toString();
        // 文件原名称
        String fileName = file.getOriginalFilename();
        // 文件类型
        String contentType = file.getContentType();
        assert contentType != null;
        String type = contentType.substring(contentType.indexOf('/') + 1);
        boolean isImage = Stream.of("jpg", "png", "gif", "tif", "bmp")
                .anyMatch(type::equalsIgnoreCase);
        if (!isImage) {
            throw new ResException("不支持的图片类型！");
        }
        // 设置存放图片文件的路径
        String path = filePath + File.separator + trueFileName + "." + type;
        File tmpFile = null;
        // 转存文件到指定的路径
        try {
            tmpFile = new File(path);
            File parentFile = tmpFile.getParentFile();
            if (!parentFile.exists()) {
                boolean mkdirs = parentFile.mkdirs();
            }
            file.transferTo(tmpFile);

            return dir + "/" + trueFileName + "." + type;
        } catch (IllegalStateException | IOException e) {
            log.error("文件上传异常!", e);
            if (null != tmpFile) {
                throw new ResException("文件上传异常!", tmpFile.delete());
            }
            throw new ResException("文件上传异常!");
        }
    }

    @Value("${spring.static-dir}")
    public void setStaticDir(String staticDir) {
        ImageUtility.staticDir = staticDir;
    }

}

