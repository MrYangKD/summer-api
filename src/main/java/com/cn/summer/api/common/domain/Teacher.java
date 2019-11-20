package com.cn.summer.api.common.domain;

import lombok.Data;

/**
 * .
 * 教师类.
 * @author YangYK
 * @create: 2019-11-16 14:07
 * @since 1.0
 */
@Data
public class Teacher extends PersonDto {
    // 毕业院校
    private String graduateSchool;

    // 学历
    private String education;

    // 教学种类
    private String kind;

}
