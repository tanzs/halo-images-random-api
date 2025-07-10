package com.aiym.haloapi.annotation;

import java.lang.annotation.*;

/**
 * @author Tanzs
 * @date 2025/7/10 下午12:17
 * @description 自定义注解，用于标记需要校验 Referer 的方法
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckReferer {

}
