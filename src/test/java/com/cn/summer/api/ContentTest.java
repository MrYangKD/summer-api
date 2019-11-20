package com.cn.summer.api;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * .
 *
 * @author YangYK
 * @create: 2019-11-18 14:37
 * @since 1.0
 */
public class ContentTest {
    public static void main(String[] args) {
        Map<String, Object> relMap = Maps.newHashMap();
        relMap.put("id", "123");
        relMap.put("name","LiLei");
        relMap.put("age", 18);
        relMap.put("sex", "男");
        relMap.put("vocation","student");
        relMap.put("hobby", "playGames");
        relMap.put("income", 10);
        relMap.put("isMarry", "no");
        relMap.put("type", "student");
        relMap.put("grade", "大一");
        relMap.put("count", 89);

        String reString = JSON.toJSONString(relMap);
        System.out.println(reString);
    }
}
