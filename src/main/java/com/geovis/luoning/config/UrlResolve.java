package com.geovis.luoning.config;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * 文件路径解析接口
 *
 * @author linrf
 * @version V1.0
 * @date 2021/5/6 23:54
 */
public interface UrlResolve {

    /**
     * 常用路径值
     */
    String NULL_PATH = "";
    String URL_PATH_SEPARATOR = "/";
    String BACK_URL_PATH = "../";
    /**
     * 匹配值
     */
    Pattern START_SLASH_PATH_MATCHER = Pattern.compile("^" + URL_PATH_SEPARATOR);
    Pattern ABS_URL_MATCHER = Pattern.compile("^ *[hH][tT]{2}[pP][sS]?://");
    Pattern SUFFIX_PATH_MATCHER = Pattern.compile("/? *$");
    Pattern PREFIX_PATH_MATCHER = Pattern.compile("^ */?");
    /**
     * 默认自动连接参数类
     * --?&=
     * --
     */
    AutoLinkUrlParam PARAM_AUTO_LINK = UrlResolve.newAutoLinkUrlParam("?", "&", "=");
    AutoLinkUrlParam HASH_AUTO_LINK = UrlResolve.newAutoLinkUrlParam("#", "&", "=");


    // =============== 基础路径方法 ===============

    /**
     * 创建自动连接参数类
     *
     * @param urlSplitChar        url分隔符
     * @param paramLinkChar       参数连接符
     * @param paramValueSplitChar 参数值连接符
     */
    static AutoLinkUrlParam newAutoLinkUrlParam(String urlSplitChar, String paramLinkChar, String paramValueSplitChar) {
        return new AutoLinkUrlParam(urlSplitChar, paramLinkChar, paramValueSplitChar);
    }

    /**
     * 传统方式连接参数值
     *
     * @param url             地址
     * @param paramName       参数名
     * @param paramValue      参数值
     * @param canDirectLinked 是否允许直连参数值，取第一个值，默认false
     */
    static String paramAutoLink(String url, String paramName, String paramValue, boolean... canDirectLinked) {
        return PARAM_AUTO_LINK.autoLinkParam(url, paramName, paramValue, canDirectLinked);
    }

    /**
     * 传统方式连接参数值
     *
     * @param url             地址
     * @param paramName       参数名
     * @param paramValue      参数值
     * @param canDirectLinked 是否允许直连参数值，取第一个值，默认false
     */
    static String hashAutoLink(String url, String paramName, String paramValue, boolean... canDirectLinked) {
        return HASH_AUTO_LINK.autoLinkParam(url, paramName, paramValue, canDirectLinked);
    }


    /**
     * 形成绝对url
     *
     * @param relativeUrl 相对url
     */
    default String absoluteUrl(String baseUrl, String relativeUrl) {
        baseUrl = Optional.ofNullable(baseUrl).orElse(NULL_PATH);
        relativeUrl = Optional.ofNullable(relativeUrl).orElse(NULL_PATH);
        if (ABS_URL_MATCHER.matcher(relativeUrl).matches()) {
            return relativeUrl;
        }
        return SUFFIX_PATH_MATCHER.matcher(baseUrl).replaceFirst(URL_PATH_SEPARATOR) +
                PREFIX_PATH_MATCHER.matcher(relativeUrl).replaceFirst(NULL_PATH);
    }


    /**
     * 自动连接参数类
     */
    class AutoLinkUrlParam {

        /**
         * url分隔符
         */
        private final String urlSplitChar;
        /**
         * 参数连接符
         */
        private final String paramLinkChar;
        /**
         * 参数值连接符
         */
        private final String paramValueSplitChar;

        /**
         * 直连参数值
         */
        private Predicate<String> directParamValueLinkMatcher;
        /**
         * 需要url分隔符
         */
        private Predicate<String> needUrlSplitCharMatcher;
        /**
         * 需要参数连接符
         */
        private Predicate<String> needParamLinkCharMatcher;

        /**
         * 构造参数
         */
        public AutoLinkUrlParam(String urlSplitChar, String paramLinkChar, String paramValueSplitChar) {
            if (null == urlSplitChar || null == paramLinkChar || null == paramValueSplitChar) {
                throw new IllegalArgumentException("参数不能为空！");
            }
            this.urlSplitChar = urlSplitChar;
            this.paramLinkChar = paramLinkChar;
            this.paramValueSplitChar = paramValueSplitChar;
            this.initMapcher();
        }

        /**
         * 初始化匹配器
         */
        private void initMapcher() {
            this.directParamValueLinkMatcher = url -> {
                int urlSplitIdx = url.lastIndexOf(urlSplitChar);
                if (urlSplitIdx < 0) {
                    return false;
                }
                int paramLinkIdx = url.lastIndexOf(paramLinkChar);
                int paramValueSplitIdx = url.lastIndexOf(paramValueSplitChar);
                int diffSplitIdx = paramValueSplitIdx - Math.max(urlSplitIdx, paramLinkIdx);
                return paramValueSplitIdx == url.length() - 1 && diffSplitIdx > 1;
            };
            this.needUrlSplitCharMatcher = url -> !url.contains(urlSplitChar);
            this.needParamLinkCharMatcher = url -> !url.endsWith(urlSplitChar) && !url.endsWith(paramLinkChar);
        }

        /**
         * 连接参数值
         *
         * @param url             地址
         * @param paramName       参数名
         * @param paramValue      参数值
         * @param canDirectLinked 是否允许直连参数值，取第一个值，默认false
         */
        public String autoLinkParam(String url, String paramName, String paramValue, boolean... canDirectLinked) {
            url = Optional.ofNullable(url).map(String::trim).orElse("");
            if (directParamValueLinkMatcher.test(url)) {
                boolean allowDirect = Optional.ofNullable(canDirectLinked).filter(bs -> bs.length > 0).map(bs -> bs[0]).orElse(false);
                if (allowDirect) {
                    return url + paramValue;
                }
            }
            if (needUrlSplitCharMatcher.test(url)) {
                url += urlSplitChar;
            }
            if (needParamLinkCharMatcher.test(url)) {
                url += paramLinkChar;
            }
            // 不指定参数，返回原地址
            return url + paramName + paramValueSplitChar + paramValue;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof AutoLinkUrlParam)) {
                return false;
            }
            AutoLinkUrlParam that = (AutoLinkUrlParam) o;
            return urlSplitChar.equals(that.urlSplitChar) && paramLinkChar.equals(that.paramLinkChar) && paramValueSplitChar.equals(that.paramValueSplitChar);
        }

        @Override
        public int hashCode() {
            return Objects.hash(urlSplitChar, paramLinkChar, paramValueSplitChar);
        }

    }


}
