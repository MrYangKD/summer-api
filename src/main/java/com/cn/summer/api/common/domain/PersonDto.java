package com.cn.summer.api.common.domain;

import lombok.Data;

/**
 * .
 * 人类
 * @author YangYK
 * @create: 2019-11-16 13:54
 * @since 1.0
 */
@Data
public class PersonDto  {
    // id
    private String id;
    // 姓名
    private String name;
    // 年龄
    private String age;
    // 性别
    private String sex;
    //职业
    private String vocation;
    // 兴趣
    private String  hobby;
    // 收入
    private int income;
    // 是否结婚 yes 是  no 否
    private String isMarry;
    // 标识
    private String type;

}
