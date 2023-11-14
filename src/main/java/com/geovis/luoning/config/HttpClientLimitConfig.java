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
 * 请求客户端限制配置。
 *
 * @author linrf
 * @version V1.0
 * @date 2020/12/5 20:51
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "http-client-limit")
public class HttpClientLimitConfig {
    /**
     * 是否启用配置
     * --默认false
     */
    boolean enable = false;
    /**
     * 是否允许referer和user-agents
     * --默认true
     */
    boolean allowRefererAndUserAgents = true;
    /**
     * 安全名单未开启时，是否允许空的referer和user-agents
     * --默认true
     */
    boolean allowEmptyRefererAndUserAgents = true;
    /**
     * 是否设置安全名单过滤
     * --默认false
     */
    boolean useWhiteList = false;
    /**
     * 安全referer列表,前缀匹配
     */
    List<String> safeReferers = Collections.emptyList();
    /**
     * 安全user-agents列表,前缀匹配
     */
    List<String> safeUserAgents = Collections.emptyList();

    /**
     * 判断referer安全名单,前缀匹配
     *
     * @param referer referer {@link HttpHeaders#REFERER}
     */
    public boolean checkRefererWhiteList(String referer) {
        boolean hasReferer = StringUtils.hasText(referer);
        if (!allowRefererAndUserAgents) {
            return !hasReferer;
        }
        // 允许 referer
        if (!useWhiteList) {
            // 空referer判断
            return allowEmptyRefererAndUserAgents || hasReferer;
        }
        // 安全名单检测
        return hasReferer && safeReferers.stream().parallel().anyMatch(referer::startsWith);
    }

    /**
     * 判断user-agent安全名单,前缀匹配
     *
     * @param userAgent userAgent {@link HttpHeaders#USER_AGENT}
     */
    public boolean checkUserAgentWhiteList(String userAgent) {
        boolean hasUserAgent = StringUtils.hasText(userAgent);
        if (!allowRefererAndUserAgents) {
            return !hasUserAgent;
        }
        // 允许 user-agent
        if (!useWhiteList) {
            // 空user-agent判断
            return allowEmptyRefererAndUserAgents || hasUserAgent;
        }
        // 安全名单检测
        return hasUserAgent && safeUserAgents.stream().parallel().anyMatch(userAgent::startsWith);
    }


    /**
     * 请求客户端限制配置。
     */
    @Bean
    public OncePerRequestFilter clientLimitFilter() {
        HttpClientLimitConfig thiz = this;
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
                if (!thiz.isEnable()) {
                    // 未启用-直接放行
                    filterChain.doFilter(request, response);
                    return;
                }
                // 请求客户端限制
                String referer = request.getHeader(HttpHeaders.REFERER);
                String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
                boolean checkOk = thiz.checkRefererWhiteList(referer) && thiz.checkUserAgentWhiteList(userAgent);
                if (!checkOk) {
                    response.setStatus(403);
                    log.warn("未知客户端,referer/user-agent:{}/{}，请确认安全名单！", referer, userAgent);
                    return;
                }
                // 放行
                filterChain.doFilter(request, response);
            }
        };
    }
}
