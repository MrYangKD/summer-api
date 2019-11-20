package com.cn.summer.api.exception;

/**
 * .
 * 重新定义异常抛出.
 * @author YangYK
 * @create: 2019-11-18 14:13
 * @since 1.0
 */
public class BusinessException extends RuntimeException implements IException {
    private static final long serialVersionUID = 4286015280221509266L;
    protected String code;
    private String nativeMessage;
    private Object[] arguments;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getMessage() {
        return this.nativeMessage == null ? super.getMessage() : this.nativeMessage;
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(String code, String message, String nativeMessage) {
        super(message);
        this.code = code;
        this.nativeMessage = nativeMessage;
    }

    public BusinessException(String code, String message, String nativeMessage, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.nativeMessage = nativeMessage;
    }

    public BusinessException(String code, Object... arguments) {
        this.code = code;
        this.arguments = arguments;
    }

    public BusinessException(String code, String msg, Object... arguments) {
        super(msg);
        this.code = code;
        this.arguments = arguments;
    }

    public void setArguments(Object... arguments) {
        this.arguments = arguments;
    }

    public Object[] getArguments() {
        return this.arguments;
    }

    public String getCode() {
        return this.code;
    }

    public String getNativeMessage() {
        return this.nativeMessage;
    }
}
