package com.wshake.generator.config;

import java.util.List;

import lombok.Data;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/4
 */
@Data
public class DataBase {
    /**
     * 数据库库名
     */
    private String SqlName;
    /**
     * 数据库Java库名
     */
    private String JavaName;
    /**
     * 数据库驼峰库名
     */
    private String CamelName;
    /**
     * 数据库首字母大写驼峰库名
     */
    private String UpperCamelName;

    /**
     * 数据库表集合
     */
    private List<Table> tables;
}
