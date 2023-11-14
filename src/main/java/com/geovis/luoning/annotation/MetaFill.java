package com.geovis.luoning.annotation;

import com.geovis.luoning.enums.MetaFillValueEnum;

import java.lang.annotation.*;

/**
 * 自动填充注解
 *
 * @author jay
 * @version V1.0
 * @date 2022/12/8 8:53
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MetaFill {

    /**
     * 是否覆盖填充，默认false
     */
    boolean overwrite() default false;

    /**
     * 填充值，默认 自动 --自动：自动匹配填充值类型
     */
    MetaFillValueEnum value() default MetaFillValueEnum.AUTO;

    /**
     * 指定填充值 --使用Json解析成对应类型，符合json规范，及注意添加""
     */
    String assign() default "";
}
