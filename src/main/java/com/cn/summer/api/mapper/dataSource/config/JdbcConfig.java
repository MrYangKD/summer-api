package com.cn.summer.api.mapper.dataSource.config;

import lombok.Data;

import java.io.Serializable;

/**
 * .
 *
 * @author YangYK
 * @since 1.0
 */
@Data
public class JdbcConfig implements Serializable {
    /** 数据源名称. */
    private String environmentId;

    /** 数据源驱动类名. */
    private String driver;

    /** 数据库连接字符串. */
    private String url;

    /** 用户名. */
    private String userName;

    /** 密码. */
    private String passwd;

    /** 是否自动提交. */
    private Boolean autoCommit = Boolean.FALSE;

    /** Statement执行超时时间, Default: 30. */
    private Integer defaultStatementTimeout = 30;


    /**
     * 无参构造器.
     */
    public JdbcConfig() {
    }


    /**
     * 有参构造器.
     * @param environmentId environmentId
     * @param driver driver
     * @param url url
     * @param userName userName
     * @param passwd passwd
     */
    public JdbcConfig(final String environmentId, final String driver, final String url, final String userName, final String passwd) {
        this.environmentId = environmentId;
        this.driver = driver;
        this.url = url;
        this.userName = userName;
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "JdbcConfig{"
                + "environmentId='" + environmentId + '\''
                + ", driver='" + driver + '\''
                + ", url='" + url + '\''
                + ", userName='" + userName + '\''
                + ", passwd='" + passwd + '\''
                + ", autoCommit=" + autoCommit
                + ", defaultStatementTimeout=" + defaultStatementTimeout
                + '}';
    }
}
