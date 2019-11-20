package com.cn.summer.api.common.entity;

import lombok.Getter;

/**
 * .
 * 返回码.
 * @author YangYK
 * @since 1.0
 */
public enum ResCodeMessage {
    /**SUCCESS.*/
    RES_CODE_6000("6000", "SUCCESS"),
    /**json串格式有误.*/
    RES_CODE_6001("6001", "ERROR：json串格式有误"),
    /**保存异常, 请重试.*/
    RES_CODE_6002("6002", "ERROR：保存异常, 请重试"),
    /**系统异常.*/
    RES_CODE_6003("6003", "ERROR：系统异常"),
    /**数据验签不通过,非法数据来源.*/
    RES_CODE_6004("6004", "ERROR：数据验签不通过,非法数据来源"),
    /**请求参数异常.*/
    RES_CODE_6005("6005", "ERROR：请求参数异常"),
    /**未找到相应渠道的公钥.*/
    RES_CODE_6006("6006", "ERROR：未找到相应渠道的公钥"),
    /**批量处理异常.*/
    RES_CODE_6007("6007", "ERROR：批量处理异常"),
    /**数据库异常.*/
    RES_CODE_6008("6008", "ERROR：数据库异常");
    @Getter
    private String code;

    @Getter
    private String message;

    ResCodeMessage(final String code, final String message) {
        this.code = code;
        this.message = message;
    }


}
