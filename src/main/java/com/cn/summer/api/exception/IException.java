package com.cn.summer.api.exception;

/**
 * .
 * 异常接口.
 * @author YangYK
 * @create: 2019-11-18 14:13
 * @since 1.0
 */
public interface IException {

    String getCode();

    String getMessage();

    String getNativeMessage();

    void setArguments(Object... var1);

    Object[] getArguments();
}
