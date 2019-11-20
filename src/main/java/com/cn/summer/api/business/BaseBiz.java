package com.cn.summer.api.business;

import com.cn.summer.api.common.domain.PersonDto;

/**
 * .
 * 数据处理接口.
 * @author YangYK
 * @create: 2019-11-16 14:41
 * @since 1.0
 */
public interface BaseBiz {
    /**
     * 数据处理
     */
    void dealRecord(PersonDto dto) throws Exception;
}
