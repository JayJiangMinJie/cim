package com.geovis.luoning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 全局响应状态
 *
 * @author jay
 * @version V1.0
 * @date 2023/3/5 20:51
 */
@Getter
@AllArgsConstructor
public enum ResEnum {
    /**
     * 200 请求成功
     */
    OK(200, "请求成功！"),
    /**
     * 4000 请求参数出错
     */
    BAD_REQUEST(4000, "请求参数错误！"),
    /**
     * 4010 没有登录
     */
    UNAUTHORIZED(4010, "未登录！"),
    /**
     * 4011 登录失败
     */
    LOGIN_FAIL(4011, "登录失败！"),
    /**
     * 4012 用户名或密码错误
     */
    ACCOUNT_FAIL(4012, "用户名或密码错误！"),
    /**
     * 40121 验证码错误
     */
    VALID_IMG_FAIL(40121, "验证码错误！"),
    /**
     * 40122 验证码缺失
     */
    VALID_IMG_LOST(40122, "验证码缺失！"),
    /**
     * 4013 登录失败次数过多，用户将会锁定一段时间！
     */
    EXCESSIVE_LOGIN_FAIL(4013, "登录失败次数过多，用户将会锁定一段时间！"),
    /**
     * 4014 请不要多次登录
     */
    CONCURRENT_LOGIN_FAIL(4014, "请不要多次登录！"),
    /**
     * 4015 用户异常
     */
    USER_FAIL(4015, "用户异常！"),
    /**
     * 4016 角色异常
     */
    ROLE_LOST(4016, "角色缺失！"),
    /**
     * 4017 令牌已失效
     */
    TOKEN_ERR(4017, "令牌已失效！"),
    /**
     * 4018 令牌已失效
     */
    TOKEN_ILLEGAL(4018, "非法令牌！"),
    /**
     * 4019 未知第三方应用
     */
    APP_LOST(4019, "未知第三方应用！"),
    /**
     * 4030 没有权限
     */
    FORBIDDEN(4030, "权限不足！"),
    /**
     * 4100 已被删除
     */
    GONE(4100, "已删除！"),
    /**
     * 4230 账户已锁定
     */
    LOCKED(4230, "账户已锁定！"),
    /**
     * 4231 账户未激活！请联系管理员
     */
    USER_NO_ACTIVE(4231, "账户未激活！请联系管理员"),
    /**
     * 5000 服务器出错
     */
    INTERNAL_SERVER_ERROR(5000, "服务器出错！"),
    /**
     * 6000 异常
     */
    EXCPTION_ERROR(6000, "特殊错误！"),
    /**
     * 7000 未知异常
     */
    UNKNOWN_ERROR(7000, "未知错误！");

    /**
     * 状态码
     */
    private final int code;
    /**
     * 提示信息
     */
    private final String message;
}
