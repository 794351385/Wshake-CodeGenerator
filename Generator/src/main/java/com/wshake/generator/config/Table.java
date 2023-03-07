package com.wshake.generator.config;


import java.util.List;

import lombok.Data;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/4
 * 表实体
 */
@Data
public class Table {
    /**
     * 数据库表名称   pms_order
     */
    private String sqlTableName;

    /**
     * 处理后的表名称(首字母小写)  order
     */
    private String tableLowerName;

    /**
     * 处理后的表名称(首字母大写) Order
     */
    private String tableUpperName;

    /**
     * 表注释
     */
    private String comment;

    /**
     * 主键列
     */
    private String keys;

    /**
     * 列集合
     */
    private List<Column> columns;
}
