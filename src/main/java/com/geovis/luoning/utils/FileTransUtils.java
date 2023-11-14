package com.geovis.luoning.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.net.URLEncoder;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 文件传输工具
 *
 * @author jay
 * @version V1.0
 * @date 2022/10/16 14:12
 */
public enum FileTransUtils {
    /**
     * 工具实例
     */
    use;

    public static String uploadFiles(List<MultipartFile> files, String dir) {
        List<String> filePaths = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
                    String fullFilePath = dir + uniqueFileName;
                    File dest = new File(fullFilePath);

                    // 将上传的文件保存到服务器
                    file.transferTo(dest);

//                    filePaths.add(fullFilePath + "/uploaded-files/" + uniqueFileName); // 添加文件路径到集合
                    filePaths.add(fullFilePath); // 添加文件路径到集合
                } catch (IOException e) {
                    e.printStackTrace();
                    // 处理文件上传失败情况
                }
            }
        }

        // 将文件路径集合转换为逗号分隔的字符串
        return String.join(",", filePaths);
    }

    private static String generateUniqueFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString(); // 使用UUID生成唯一文件名
        return fileName + extension;
    }

    /**
     * 加载Headers
     * --自动判断MIME类型
     *
     * @param fileName     文件名
     * @param mediaTypeOne 媒体类型--自动判断MIME类型
     */
    private HttpHeaders loadHeader(String fileName, MediaType... mediaTypeOne) {
        String fn = Optional.ofNullable(fileName)
                .map(FileNameUtil::cleanInvalid)
                .orElseGet(() -> FileNameUtil.cleanInvalid(UUID.randomUUID().toString()));
        MediaType mt = Optional.ofNullable(mediaTypeOne)
                .map(Arrays::stream).orElseGet(Stream::empty)
                .filter(Objects::nonNull)
                .findFirst().orElseGet(() -> {
                    // 解析文件的 mime 类型
                    String mediaTypeStr = URLConnection.getFileNameMap().getContentTypeFor(fn);
                    // 无法判断MIME类型时，作为流类型
                    mediaTypeStr = (mediaTypeStr == null) ? MediaType.APPLICATION_OCTET_STREAM_VALUE : mediaTypeStr;
                    // 实例化MIME
                    return MediaType.parseMediaType(mediaTypeStr);
                });
        // 构造响应的头
        HttpHeaders headers = new HttpHeaders();
        // 下载之后需要在请求头中放置文件名，该文件名按照ISO_8859_1编码。
        // String filenames = new String(fn.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        String filenames = URLEncoder.DEFAULT.encode(fn, StandardCharsets.UTF_8);
        headers.setContentDispositionFormData("attachment", filenames);
        headers.setContentType(mt);
        return headers;
    }

    /**
     * 处理文件下载
     * --FileSystemResource
     *
     * @param absFileName 文件全路径名
     */
    public ResponseEntity<FileSystemResource> download(String absFileName) {
        File file = new File(absFileName);
        return this.download(file, file.getName(), RuntimeException::new);
    }

    /**
     * 处理文件下载
     * --FileSystemResource
     *
     * @param absFile 绝对路径的文件对象
     */
    public ResponseEntity<FileSystemResource> download(File absFile) {
        return this.download(absFile, absFile.getName(), RuntimeException::new);
    }

    /**
     * 处理文件下载
     * --FileSystemResource
     *
     * @param absFileName    文件全路径名
     * @param exceptinHandle 异常处理
     */
    public ResponseEntity<FileSystemResource> download(String absFileName, Function<Throwable, ? extends RuntimeException> exceptinHandle) {
        File file = new File(absFileName);
        return this.download(file, file.getName(), exceptinHandle);
    }

    /**
     * 处理文件下载
     * --FileSystemResource
     *
     * @param absFileName    文件全路径名
     * @param fileName       自定义文件名
     * @param exceptinHandle 异常处理
     */
    public ResponseEntity<FileSystemResource> download(String absFileName, String fileName, Function<Throwable, ? extends RuntimeException> exceptinHandle) {
        File file = new File(absFileName);
        return this.download(file, fileName, exceptinHandle);
    }

    /**
     * 处理文件下载
     * --FileSystemResource
     *
     * @param absFile        绝对路径的文件对象
     * @param exceptinHandle 异常处理
     */
    public ResponseEntity<FileSystemResource> download(File absFile, Function<Throwable, ? extends RuntimeException> exceptinHandle) {
        return this.download(absFile, absFile.getName(), exceptinHandle);
    }

    /**
     * 处理文件下载
     * --FileSystemResource
     *
     * @param absFile        绝对路径的文件对象
     * @param fileName       自定义文件名,（非路径，自动补全文件后缀）
     * @param exceptinHandle 异常处理
     */
    public ResponseEntity<FileSystemResource> download(File absFile, String fileName, Function<Throwable, ? extends RuntimeException> exceptinHandle) {
        try {
            if (!absFile.exists() || !absFile.isFile()) {
                throw new IOException("文件不存在！File:" + absFile.getAbsoluteFile());
            }
            fileName = Optional.ofNullable(fileName)
                    .map(FileNameUtil::cleanInvalid)
                    .map(s -> StrUtil.EMPTY.equals(FileNameUtil.extName(s)) ? s + "." + FileUtil.getType(absFile) : s)
                    .orElseGet(absFile::getName);
            // 获取本地文件系统中的文件资源
            FileSystemResource resource = new FileSystemResource(absFile);
            HttpHeaders headers = loadHeader(fileName);
            // 返还资源
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.getInputStream().available())
                    .contentType(MediaType.parseMediaType("application/json; charset=UTF-8"))
                    .body(resource);
        } catch (Throwable e) {
            throw exceptinHandle.apply(e);
        }
    }

    /**
     * 处理下载
     * --ByteArrayResource
     *
     * @param bytes    字节数据
     * @param fileName 自定义文件名,（非路径,可空）
     */
    public ResponseEntity<ByteArrayResource> download(byte[] bytes, String fileName) {
        return this.download(bytes, fileName, RuntimeException::new);
    }

    /**
     * 处理下载
     * --ByteArrayResource
     *
     * @param bytes          字节数据
     * @param exceptinHandle 异常处理
     */
    public ResponseEntity<ByteArrayResource> download(byte[] bytes, Function<Throwable, ? extends RuntimeException> exceptinHandle) {
        return this.download(bytes, UUID.randomUUID().toString(), exceptinHandle);
    }

    /**
     * 处理下载
     * --ByteArrayResource
     *
     * @param bytes          字节数据
     * @param fileName       自定义文件名,（非路径,可空）
     * @param exceptinHandle 异常处理
     */
    public ResponseEntity<ByteArrayResource> download(byte[] bytes, String fileName, Function<Throwable, ? extends RuntimeException> exceptinHandle) {
        try {
            if (null == bytes) {
                throw new IOException("字节数据不能为null！");
            }
            fileName = Optional.ofNullable(fileName)
                    .map(FileNameUtil::cleanInvalid)
                    .orElseGet(() -> FileNameUtil.cleanInvalid(UUID.randomUUID().toString()));
            // 获取本地文件系统中的文件资源
            ByteArrayResource resource = new ByteArrayResource(bytes);
            HttpHeaders headers = loadHeader(fileName);
            // 返还资源
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (Throwable e) {
            throw exceptinHandle.apply(e);
        }
    }

    /**
     * 处理下载
     * --InputStreamResource
     *
     * @param inputStream 输入流
     * @param fileName    自定义文件名,（非路径,可空）
     */
    public ResponseEntity<InputStreamResource> download(InputStream inputStream, String fileName) {
        return this.download(inputStream, fileName, RuntimeException::new);
    }

    /**
     * 处理下载
     * --InputStreamResource
     *
     * @param inputStream    输入流
     * @param exceptinHandle 异常处理
     */
    public ResponseEntity<InputStreamResource> download(InputStream inputStream, Function<Throwable, ? extends RuntimeException> exceptinHandle) {
        return this.download(inputStream, UUID.randomUUID().toString(), exceptinHandle);
    }

    /**
     * 处理下载
     * --InputStreamResource
     *
     * @param inputStream    输入流
     * @param fileName       自定义文件名,（非路径,可空）
     * @param exceptinHandle 异常处理
     */
    public ResponseEntity<InputStreamResource> download(InputStream inputStream, String fileName, Function<Throwable, ? extends RuntimeException> exceptinHandle) {
        try {
            if (null == inputStream) {
                throw new IOException("数据流不能为null！");
            }
            fileName = Optional.ofNullable(fileName)
                    .map(FileNameUtil::cleanInvalid)
                    .orElseGet(() -> FileNameUtil.cleanInvalid(UUID.randomUUID().toString()));
            // 获取本地文件系统中的文件资源
            InputStreamResource resource = new InputStreamResource(inputStream);
            HttpHeaders headers = loadHeader(fileName);
            // headers.add(HttpHeaders.TRANSFER_ENCODING, "chunked");
            // headers.setConnection("close");
            // 返还资源
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Throwable e) {
            throw exceptinHandle.apply(e);
        }
    }

    public List<File> searchFilesByName(File folder, final String keyword) {
        List<File> result = new ArrayList<File>();
        if (folder.isFile()){
            result.add(folder);
        }

        File[] subFolders = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                if (file.getName().contains(keyword)) {
                    return true;
                }
                return false;
            }
        });

        if (subFolders != null) {
            for (File file : subFolders) {
                if (file.isFile()) {
                    // 如果是文件则将文件添加到结果列表中
                    result.add(file);
                } else {
                    // 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
                    result.addAll(searchFilesByName(file, keyword));
                }
            }
        }
        return result;
    }

    public List<File> searchFiles(File folder) {
        List<File> result = new ArrayList<File>();
        if (folder.isFile()){
            result.add(folder);
        }

        File[] subFolders = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
//                if (file.getName().toLowerCase().contains(keyword)) {
                    return true;
//                }
//                return false;
            }
        });

        if (subFolders != null) {
            for (File file : subFolders) {
                if (file.isFile()) {
                    // 如果是文件则将文件添加到结果列表中
                    result.add(file);
                } else {
                    // 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
                    result.addAll(searchFiles(file));
                }
            }
        }
        return result;
    }

}
