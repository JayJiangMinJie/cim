package com.geovis.luoning.config;

import cn.hutool.core.io.FileUtil;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 文件路径解析接口
 *
 * @author linrf
 * @version V1.0
 * @date 2021/5/6 23:54
 */
public interface FilePathResolve {
    /**
     * 常用路径值
     */
    String NULL_PATH = "";
    String SLASH_PATH = "/";
    String UUID_SPLIT = "-";
    String RAND_FILE_SPLIT = "__";
    /**
     * 常用路径匹配
     */
    Pattern JAR_PATH_MATCHER = Pattern.compile("^ *jar: */?");
    Pattern SUFFIX_PATH_MATCHER = Pattern.compile("/? *$");
    Pattern PREFIX_PATH_MATCHER = Pattern.compile("^ */?");
    Pattern BACKSLASH_PATH_MATCHER = Pattern.compile("\\\\");
    Pattern RAND_FILE_PREFIX_MATCHER = Pattern.compile("^[^" + RAND_FILE_SPLIT + "]+" + RAND_FILE_SPLIT);
    /**
     * 常用日期路径格式
     */
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss/");
    DateTimeFormatter DATE_HOUR_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/");
    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd/");
    DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH/mm/ss/");
    /**
     * Windows下文件名中的无效字符
     */
    Pattern FILE_NAME_INVALID_PATTERN_WIN = Pattern.compile("[\\\\/:*?\"<>|]+");
    /**
     * 相对应用程序主目录
     */
    ApplicationHome APP_HOME = new ApplicationHome(FilePathResolve.class);


    // =============== 基础路径方法 ===============


    /**
     * jar包绝对路径
     */
    default String absJarPath() {
        String jarPath = Optional.ofNullable(APP_HOME.getSource())
                .map(File::getParentFile)
                .map(File::getAbsolutePath)
                .orElse(APP_HOME.getDir().getAbsolutePath());
        jarPath = BACKSLASH_PATH_MATCHER.matcher(jarPath).replaceAll(SLASH_PATH);
        return SUFFIX_PATH_MATCHER.matcher(jarPath).replaceAll(SLASH_PATH);
    }

    /**
     * 合并路径
     *
     * @param parentPath 父路径(默认空）
     * @param childPath  子路径(默认空）
     */
    default String mergeFilePath(String parentPath, String childPath) {
        return Optional.ofNullable(parentPath)
                .map(s -> SUFFIX_PATH_MATCHER.matcher(s).replaceFirst(SLASH_PATH)).orElse(NULL_PATH)
                + Optional.ofNullable(childPath)
                .map(s -> PREFIX_PATH_MATCHER.matcher(s).replaceFirst(NULL_PATH)).orElse(NULL_PATH);
    }

    /**
     * 合并路径
     *
     * @param parentChildPaths 多层路径(默认空）
     */
    default String mergeFilePath(String[] parentChildPaths) {
        // 合并多层相对路径
        return Optional.ofNullable(parentChildPaths)
                .flatMap(rps -> Arrays.stream(rps).reduce(this::mergeFilePath))
                .orElse(NULL_PATH);
    }

    /**
     * 合并路径
     *
     * @param parentPath 父路径(默认空）
     * @param childPath  子路径(默认空）
     * @param childPaths 更多子路径(默认空）
     */
    default String mergeFilePath(String parentPath, String childPath, String... childPaths) {
        // 合并多层相对路径
        String lastChildPath = mergeFilePath(childPaths);
        return mergeFilePath(mergeFilePath(parentPath, childPath), lastChildPath);
    }

    /**
     * 解析文件绝对路径
     *
     * @param basePath      基础路径（classpath:和/和jar:）,打包后classpath:将无法正常读取
     * @param relativePaths 多层相对路径(默认空）
     */
    default String absoluteFilePath(String basePath, String... relativePaths) {
        // 合并多层相对路径
        String relativePath = mergeFilePath(relativePaths);
        // 初步解析路径
        String pathDetail = JAR_PATH_MATCHER.matcher(basePath).replaceFirst(absJarPath());
        // 返回绝对路径
        return mergeFilePath(FileUtil.getAbsolutePath(pathDetail), relativePath);
    }


    // =============== 拓展路径方法 ===============


    /**
     * 清除文件名中的在Windows下不支持的非法字符，包括： \ / : * ? " &lt; &gt; |
     *
     * @param fileName 文件名（必须不包括路径，否则路径符将被替换）
     */
    default String cleanInvalidFilename(String fileName) {
        return StringUtils.hasText(fileName) ? FILE_NAME_INVALID_PATTERN_WIN.matcher(fileName).replaceAll(NULL_PATH) : fileName;
    }

    /**
     * 随机文件名(随机前缀，将清除非法字符)
     *
     * @param filename 文件名称
     */
    default String uuidRandFilename(String filename) {
        String uuidCode = UUID.randomUUID().toString().replaceAll(UUID_SPLIT, NULL_PATH);
        return Optional.ofNullable(cleanInvalidFilename(filename))
                .map(fn -> uuidCode + RAND_FILE_SPLIT + fn)
                .orElseGet(() -> uuidCode + RAND_FILE_SPLIT + uuidCode);
    }

    /**
     * 还原随机文件名(去除随机前缀，将清除非法字符)
     * --{@link FilePathResolve#uuidRandFilename}
     *
     * @param filename 文件名称
     */
    default String restoreRandFilename(String filename) {
        return Optional.ofNullable(cleanInvalidFilename(filename))
                .map(fn -> RAND_FILE_PREFIX_MATCHER.matcher(fn).replaceFirst(NULL_PATH))
                .orElse(NULL_PATH);
    }

    /**
     * 添加日期父路径
     *
     * @param dateTimeFormatter 日期格式化对象
     * @param relativePath      相对路径(默认空）
     */
    default String datePtnFilePath(DateTimeFormatter dateTimeFormatter, String relativePath) {
        String datePath = LocalDateTime.now().format(dateTimeFormatter);
        return mergeFilePath(datePath, relativePath);
    }

    /**
     * 添加日期父路径
     *
     * @param datePattern  日期格式，如“yyyy/MM/dd/HH/mm/ss/”
     * @param relativePath 相对路径(默认空）
     */
    default String datePtnFilePath(String datePattern, String relativePath) {
        return datePtnFilePath(DateTimeFormatter.ofPattern(datePattern), relativePath);
    }

    /**
     * 添加日期父路径
     * --{@link FilePathResolve#DATE_TIME_FORMATTER}
     *
     * @param relativePath 相对路径(默认空）
     */
    default String dateTimeFilePath(String relativePath) {
        return datePtnFilePath(DATE_TIME_FORMATTER, relativePath);
    }

    /**
     * 添加日期父路径
     * --{@link FilePathResolve#DATE_HOUR_FORMATTER}
     *
     * @param relativePath 相对路径(默认空）
     */
    default String dateHourFilePath(String relativePath) {
        return datePtnFilePath(DATE_HOUR_FORMATTER, relativePath);
    }

    /**
     * 添加日期父路径
     * --{@link FilePathResolve#DATE_FORMATTER}
     *
     * @param relativePath 相对路径(默认空）
     */
    default String dateFilePath(String relativePath) {
        return datePtnFilePath(DATE_TIME_FORMATTER, relativePath);
    }

    /**
     * 添加日期父路径
     * --{@link FilePathResolve#TIME_FORMATTER}
     *
     * @param relativePath 相对路径(默认空）
     */
    default String timeFilePath(String relativePath) {
        return datePtnFilePath(TIME_FORMATTER, relativePath);
    }

}
