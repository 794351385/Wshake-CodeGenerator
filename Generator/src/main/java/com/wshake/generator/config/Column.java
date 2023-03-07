package com.wshake.generator.config;

import lombok.Data;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/4
 * 列对象
 */
@Data
public class Column {
    /**
     * Sql中的列名  user_id未过滤
     */
    private String columnSqlName;
    /**
     * java中的列名 userId 属性名
     */
    private String columnCamelName;

    /**
     * java中的列名 UserId 属性名
     */
    private String columnUpperCamelName;
    /**
     * java中的列名 user_id
     */
    private String columnJavaName;
    /**
     * 是否是父类字段
     */
    private Boolean isSuperColumn = false;
    /**
     * 移除Boolean类型字段is前缀
     */
    private String columnRemovePrefix;
    /**
     * 列Java类型
     */
    private String columnType;
    /**
     * 列数据库类型
     */
    private String columnDbType;
    /**
     * 列备注
     */
    private String comment;
    /**
     * 列是否主键
     */
    private Boolean isKey=false;

    private String fill;

    private Boolean keyIdentityFlag=false;

    private Boolean isVersion=false;

    private Boolean isDeleted=false;

}
