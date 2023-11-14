package com.geovis.luoning.entity.base;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.geovis.luoning.enums.ResEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * 统一响应数据结构
 *
 * @author jay
 * @version V1.0
 * @date 2022-10-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@ApiModel(value = "响应数据结构", description = "统一响应数据结构")
public class ApiResp<T> implements Serializable {
    @ApiModelProperty(value = "响应码，请求状态信息")
    private Integer status;

    @ApiModelProperty(value = "响应消息内容")
    private String message;

    @ApiModelProperty(value = "响应数据内容")
    private T data;

    /**
     * 响应成功
     */
    public static <T> ApiResp<T> ok() {
        return ApiResp.res(ResEnum.OK.getCode(), "请求成功!", null);
    }

    /**
     * 响应成功消息
     *
     * @param message 消息内容
     */
    public static <T> ApiResp<T> ok(String message) {
        return ApiResp.res(ResEnum.OK.getCode(), message, null);
    }

    /**
     * 响应成功消息和数据
     *
     * @param message 消息内容
     * @param data    响应数据
     * @param <T>     响应数据类型
     */
    public static <T> ApiResp<T> ok(String message, T data) {
        return ApiResp.res(ResEnum.OK.getCode(), message, data);
    }

    /**
     * 响应失败
     */
    public static <T> ApiResp<T> err() {
        return ApiResp.res(ResEnum.INTERNAL_SERVER_ERROR.getCode(), "系统奔溃了，请稍后再试!", null);
    }

    /**
     * 响应失败消息
     *
     * @param message 消息内容
     */
    public static <T> ApiResp<T> err(String message) {
        return ApiResp.res(ResEnum.INTERNAL_SERVER_ERROR.getCode(), message, null);
    }

    /**
     * 响应失败消息和数据
     *
     * @param message 消息内容
     * @param data    响应数据
     * @param <T>     响应数据类型
     */
    public static <T> ApiResp<T> err(String message, T data) {
        return ApiResp.res(ResEnum.INTERNAL_SERVER_ERROR.getCode(), message, data);
    }

    /**
     * 枚举数据响应数据
     *
     * @param resEnum 响应枚举消息
     */
    public static ApiResp<?> res(@NotNull ResEnum resEnum) {
        return ApiResp.res(resEnum.getCode(), resEnum.getMessage(), null);
    }

    /**
     * 枚举数据响应数据
     *
     * @param resEnum 响应枚举
     * @param data    响应数据
     * @param <T>     响应数据类型
     */
    public static <T> ApiResp<T> res(@NotNull ResEnum resEnum, @Nullable T data) {
        return ApiResp.res(resEnum.getCode(), resEnum.getMessage(), data);
    }

    /**
     * 枚举数据响应数据
     *
     * @param resEnum   响应枚举
     * @param reMessage 消息内容(重设）
     */
    public static ApiResp<?> resMsg(@NotNull ResEnum resEnum, @Nullable String reMessage) {
        return ApiResp.res(
                resEnum.getCode(), Optional.ofNullable(reMessage).orElse(resEnum.getMessage()), null);
    }

    /**
     * 枚举数据响应数据
     *
     * @param resEnum   响应枚举
     * @param reMessage 消息内容(重设）
     * @param data      响应数据
     * @param <T>       响应数据类型
     */
    public static <T> ApiResp<T> resMsg(
            @NotNull ResEnum resEnum, @Nullable String reMessage, @Nullable T data) {
        return ApiResp.res(
                resEnum.getCode(), Optional.ofNullable(reMessage).orElse(resEnum.getMessage()), data);
    }

    /**
     * 响应失败消息和数据
     *
     * @param code    响应码
     * @param message 消息内容
     * @param data    响应数据
     * @param <T>     响应数据类型
     */
    public static <T> ApiResp<T> res(int code, String message, T data) {
        return ApiResp.<T>builder()
                .status(code)
                .message(Optional.ofNullable(message).orElse("系统繁忙，请稍后再试!"))
                .data(data)
                .build();
    }

    /**
     * 跳转页面
     *
     * @param view 页面
     */
    public static ModelAndView view(String view) {
        return new ModelAndView(view);
    }

    /**
     * 带数据 跳转页面
     *
     * @param view  页面
     * @param model 数据
     */
    public static ModelAndView view(String view, Map<String, Object> model) {
        return new ModelAndView(view, model);
    }

    /**
     * 重定向页面
     *
     * @param view 页面
     */
    public static ModelAndView redirectMov(String view) {
        return new ModelAndView("redirect:" + view);
    }

    /**
     * 重定向页面
     *
     * @param view 页面
     */
    public static String redirect(String view) {
        return "redirect:" + view;
    }

    /**
     * 转发向页面
     *
     * @param view 页面
     */
    public static ModelAndView forwardMov(String view) {
        return new ModelAndView("forward:" + view);
    }

    /**
     * 转发向页面
     *
     * @param view 页面
     */
    public static String forward(String view) {
        return "forward:" + view;
    }
}
