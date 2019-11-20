package com.cn.summer.api.common.domain;

import lombok.Data;

/**
 * .
 * 学生类.
 * @author YangYK
 * @create: 2019-11-16 13:55
 * @since 1.0
 */
@Data
public class Student extends  PersonDto{
    // 年级
    private String grade;
    // 分数
    private int count;
}
