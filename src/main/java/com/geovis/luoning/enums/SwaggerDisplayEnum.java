package com.geovis.luoning.enums;

import java.lang.annotation.*;

/**
 * swagger枚举描述注解 ==可作用于：参数=字段>类型
 *
 * @author jay
 * @version V1.0
 * @date 2022/12/5 20:51
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SwaggerDisplayEnum {

    /**
     * 值字段（索引），作为参数时传递的值，默认naming
     */
    String index() default "";

    /**
     * 描述字段，默认空
     */
    String name() default "name";
}
