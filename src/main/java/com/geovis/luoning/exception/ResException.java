package com.geovis.luoning.exception;

import com.geovis.luoning.entity.base.ApiResp;
import com.geovis.luoning.enums.ResEnum;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * 系统异常响应
 *
 * @author jay
 * @version V1.0
 * @date 2022/10/16 15:46
 */
@Getter
public class ResException extends RuntimeException {
    /**
     * 响应值
     */
    private final ApiResp<?> apiResp;

    /**
     * @param message 异常响应信息
     */
    public ResException(@NotBlank String message) {
        super(message);
        this.apiResp = ApiResp.res(500, message, null);
    }

    /**
     * @param message 异常响应信息
     * @param data    响应数据/异常对象
     */
    public ResException(@NotBlank String message, Object data) {
        super(message);
        this.apiResp = ApiResp.res(500, message, data);
    }

    /**
     * @param resEnum 异常响应枚举
     */
    public ResException(@NotNull ResEnum resEnum) {
        super(resEnum.getMessage());
        this.apiResp = ApiResp.res(resEnum);
    }

    /**
     * @param resEnum 异常响应枚举
     */
    public ResException(@NotNull ResEnum resEnum, Object data) {
        super(
                resEnum.getMessage(),
                Optional.ofNullable(data)
                        .filter(Throwable.class::isInstance)
                        .map(Throwable.class::cast)
                        .orElse(null));
        this.apiResp = ApiResp.res(resEnum);
    }

    /**
     * @param resEnum   异常响应枚举
     * @param reMessage 重设异常响应信息
     */
    public ResException(@NotNull ResEnum resEnum, String reMessage) {
        super(reMessage);
        this.apiResp = ApiResp.resMsg(resEnum, reMessage);
    }

    /**
     * @param resEnum   异常响应枚举
     * @param reMessage 重设异常响应信息
     * @param data      响应数据/异常对象
     */
    public ResException(@NotNull ResEnum resEnum, @NotBlank String reMessage, Object data) {
        super(
                reMessage,
                Optional.ofNullable(data)
                        .filter(Throwable.class::isInstance)
                        .map(Throwable.class::cast)
                        .orElse(null));
        this.apiResp = ApiResp.resMsg(resEnum, reMessage, data);
    }

    /**
     * @param apiResp 响应信息
     */
    public ResException(@NotNull ApiResp<?> apiResp) {
        super(apiResp.getMessage());
        this.apiResp = apiResp;
    }
}
