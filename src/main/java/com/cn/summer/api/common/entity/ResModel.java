package com.cn.summer.api.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * .
 *
 * @author YangYK
 * @since 1.0
 */
@Data
public class ResModel implements Serializable {
    private String code;
    private String message;

    /**
     * 构造器.
     * @param code code
     * @param message message
     */
    public ResModel(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 构造器.
     * @param resCodeMessage resMessage
     */
    public ResModel(final ResCodeMessage resCodeMessage){
        this.code = resCodeMessage.getCode();
        this.message = resCodeMessage.getMessage();
    }


    private ResModel(){}
}
