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
    private String name;

    /**
     * 数据库表集合
     */
    private List<Table> tables;
}
