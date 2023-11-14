package com.geovis.luoning.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 预防 http host 攻击（漏洞）配置。
 *
 * @author linrf
 * @version V1.0
 * @date 2020/12/5 20:51
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "http-host")
public class HttpHostConfig {
    /**
     * 是否启用配置
     * --默认false
     */
    boolean enable = false;
    /**
     * 安全host列表
     */
    List<String> safeHosts = Collections.emptyList();

    /**
     * 判断主机是否存在安全名单,前缀匹配
     *
     * @param host host {@link HttpHeaders#HOST}
     */
    public boolean checkWhiteList(String host) {
        // 安全名单检测
        return StringUtils.hasText(host) &&
                safeHosts.stream().parallel().anyMatch(host::startsWith);
    }


    /**
     * 预防 http host 攻击（漏洞）。
     */
    @Bean
    public OncePerRequestFilter httpHostFilter() {
        HttpHostConfig thiz = this;
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
                if (!thiz.isEnable()) {
                    // 未启用-直接放行
                    filterChain.doFilter(request, response);
                    return;
                }
                // 头攻击检测  过滤主机名
                String requestHost = request.getHeader(HttpHeaders.HOST);
                if (!thiz.checkWhiteList(requestHost)) {
                    response.setStatus(403);
                    log.warn("未知主机Host:{}，请确认安全名单！", requestHost);
                    return;
                }
                // 放行
                filterChain.doFilter(request, response);
            }
        };
    }
}
