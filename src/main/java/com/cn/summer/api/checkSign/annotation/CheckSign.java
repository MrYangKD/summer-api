package com.cn.summer.api.checkSign.annotation;

import java.lang.annotation.*;

/**
 * .
 * 定义注解方法
 * @author YangYK
 * @since 1.0
 */
@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckSign {
}
