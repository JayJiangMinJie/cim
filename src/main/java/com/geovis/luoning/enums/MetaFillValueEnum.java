package com.geovis.luoning.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * 匹配类型时，填充指定值，否则填充 {@code null}
 *
 * @author jay
 * @version V1.0
 * @date 2022/12/8 8:59
 */
@AllArgsConstructor
@SwaggerDisplayEnum
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum MetaFillValueEnum {
    /**
     * 填充第一个枚举值
     */
    FIRST_ENUM(
            fieldType -> {
                if (!Enum.class.isAssignableFrom(fieldType)) {
                    return null;
                }
                return fieldType.getEnumConstants()[0];
            }),
    /**
     * String填充""
     */
    String_EMPTY(
            fieldType -> {
                if (!String.class.isAssignableFrom(fieldType)) {
                    return null;
                }
                return "";
            }),
    /**
     * 填充当前时间
     */
    DATE_NOW(
            fieldType -> {
                if (LocalDateTime.class.isAssignableFrom(fieldType)) {
                    return LocalDateTime.now();
                }
                if (LocalDate.class.isAssignableFrom(fieldType)) {
                    return LocalDate.now();
                }
                if (LocalTime.class.isAssignableFrom(fieldType)) {
                    return LocalTime.now();
                }
                if (Date.class.isAssignableFrom(fieldType)) {
                    return new Date();
                }
                if (java.sql.Date.class.isAssignableFrom(fieldType)) {
                    return new java.sql.Date(System.currentTimeMillis());
                }
                return null;
            }),
    /**
     * int 填充0
     */
    INT_ZERO(
            fieldType -> {
                if (!Integer.class.isAssignableFrom(fieldType)) {
                    return null;
                }
                return 0;
            }),
    /**
     * long 填充0
     */
    LONG_ZERO(
            fieldType -> {
                if (!Long.class.isAssignableFrom(fieldType)) {
                    return null;
                }
                return 0L;
            }),
    /**
     * float填充0
     */
    FLOAT_ZERO(
            fieldType -> {
                if (!Float.class.isAssignableFrom(fieldType)) {
                    return null;
                }
                return 0F;
            }),
    /**
     * double填充0
     */
    DOUBLE_ZERO(
            fieldType -> {
                if (!Double.class.isAssignableFrom(fieldType)) {
                    return null;
                }
                return 0D;
            }),
    /**
     * 填充指定值
     */
    ASSIGN(fieldType -> null),
    /**
     * 自动填充
     */
    AUTO(
            fieldType -> {
                Object result = null;
                // 排除特定填充
                List<String> excludNames = Arrays.asList("AUTO", "ASSIGN");
                MetaFillValueEnum[] enumConstants = MetaFillValueEnum.class.getEnumConstants();
                for (MetaFillValueEnum enumConstant : enumConstants) {
                    if (excludNames.contains(enumConstant.name())) {
                        // 排除特定填充
                        continue;
                    }
                    result = enumConstant.fillFun.apply(fieldType);
                    if (null != result) {
                        break;
                    }
                }
                return result;
            });

    private final Function<Class<?>, Object> fillFun;

    /**
     * 填充值
     *
     * @param clazz 填充类
     */
    public Object fill(Class<?> clazz) {
        return this.fillFun.apply(clazz);
    }
}
