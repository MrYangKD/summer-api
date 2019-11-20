package com.cn.summer.api.mapper.dataSource;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * .
 * 动态数据源适配器.
 * @author YangYK
 * @since 1.0
 */
public class DynamicDataSourceContextHolder {


    /**数据Id 列表.*/
    private static final Set<String> DATA_SOURCE_ID = Sets.newHashSet();

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<String>();

    /***
     * 设置 dataSourceType.
     * @param dataSourceType dataSourceType
     */
    public static void setDataSourceType(final String dataSourceType){
        CONTEXT_HOLDER.set(dataSourceType);
    }

    /**
     * 获取 dataSourceType.
     * @return string
     */
    public static String getDataSourceType(){
        return (String) CONTEXT_HOLDER.get();
    }

    /**
     * 删除 dataSourceType.
     */
    public static void clearDataSourceType(){
        CONTEXT_HOLDER.remove();
    }

    /**
     * 判断指定DataSrouce当前是否存在.
     * @param dataSourceId dataSourceId
     * @return boolean
     */
    public static boolean containsDataSource(final String dataSourceId){
        return DATA_SOURCE_ID.contains(dataSourceId);
    }


    /**
     * 加入 dataSourceId.
     * @param dataSourceId dataSourceId
     */
    public static void setDataSourceId(final String dataSourceId){
        DATA_SOURCE_ID.add(dataSourceId);
    }

}
