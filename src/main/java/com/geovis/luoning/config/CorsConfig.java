package com.geovis.luoning.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 跨域全局配置
 *
 * @author linrf
 * @version V1.0
 * @date 2020/12/5 20:51
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cors")
public class CorsConfig {

    /**
     * 允许跨域的地址
     */
    private Set<String> allowedUrls = Collections.singleton("/**");
    /**
     * 同源配置(指定ip和端口)
     * 默认：*
     */
    private Set<String> origins = Collections.singleton("*");
    /**
     * header，允许哪些header
     * 默认：*
     */
    private Set<String> headers = Collections.singleton("*");
    /**
     * 允许的请求方法，"GET", "POST", "PUT", "DELETE","PATCH","OPTIONS"等
     * 默认："GET", "POST", "PUT", "DELETE","PATCH","OPTIONS"
     */
    private Set<String> methods = Stream.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS").collect(Collectors.toSet());
    /**
     * 是否允许跨域携带cookie
     * 默认：true
     */
    private boolean credentials = true;
    /**
     * 跨域携带cookie的有效时间
     * 默认：1209600
     */
    private long maxAge = 1209600L;

    /**
     * 生成跨域配置
     */
    @Bean("corsConfigurationSource")
    public UrlBasedCorsConfigurationSource corsConfigurationSource(SystemConfig systemConfig) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //同源配置，*表示任何请求都视为同源，若需指定ip和端口可以改为如“localhost：8080”，多个以“，”分隔；
        this.getOrigins().forEach(corsConfiguration::addAllowedOriginPattern);
        //header，允许哪些header，本案中使用的是token，此处可将*替换为token；
        Set<String> headers = new HashSet<>();
        headers.add(systemConfig.getSessionHeaderName());
        headers.addAll(this.getHeaders());
        headers.forEach(corsConfiguration::addAllowedHeader);
        //允许的请求方法，PSOT、GET等
        this.getMethods().forEach(corsConfiguration::addAllowedMethod);
        //允许跨域携带cookie
        corsConfiguration.setAllowCredentials(this.isCredentials());
        corsConfiguration.setMaxAge(this.getMaxAge());
        //配置允许跨域访问的url
        this.getAllowedUrls().forEach(url -> source.registerCorsConfiguration(url, corsConfiguration));
        return source;
    }

    /**
     * 跨域过滤器
     */
    @Bean
    public CorsFilter corsFilter(UrlBasedCorsConfigurationSource corsConfigurationSource) {
        return new CorsFilter(corsConfigurationSource);
    }

}
