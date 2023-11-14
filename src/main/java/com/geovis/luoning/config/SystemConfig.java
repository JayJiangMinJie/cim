package com.geovis.luoning.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 系统配置信息
 *
 * @author linrf
 * @version V1.0
 * @date 2020/12/6 12:49
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "system-config")
public class SystemConfig implements FilePathResolve, UrlResolve {
    /**
     * 会话信息请求头名称（令牌）
     * 默认：Authorization
     */
    private String sessionHeaderName = "Authorization";

}
