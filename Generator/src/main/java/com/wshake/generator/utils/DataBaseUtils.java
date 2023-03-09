package com.wshake.generator.utils;

import com.wshake.generator.builder.Entity;
import com.wshake.generator.builder.IFill;
import com.wshake.generator.config.Column;
import com.wshake.generator.config.DataBase;
import com.wshake.generator.config.DataSourceConfig;
import com.wshake.generator.config.StrategyConfig;
import com.wshake.generator.config.Table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/4
 */
public class DataBaseUtils {
    /**
     * 获取所有数据库列表
     * @return List<String>
     * @throws SQLException
     */
    public static List<String> getSchemas() throws SQLException {
        DataSourceConfig dataSourceConfig = DataSourceConfig.getDataSourceConfig();
        ResultSet resultSet = dataSourceConfig.getResultSetCatalogs();
        ArrayList<String> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(resultSet.getString(1));
        }
        return list;
    }
    /**
     * 输入数据库名获取数据库中的所有表信息
     * @return List<DataBase>
     * @throws SQLException
     */
    public static List<DataBase> getDataBase() throws SQLException {
        ArrayList<DataBase> dataBaseList = new ArrayList<>();
        StrategyConfig strategyConfig=StrategyConfig.getStrategyConfig();
        Set<String> include = strategyConfig.getInclude();
        Set<String> databases = strategyConfig.getDatabases();
        if(include.isEmpty() && !databases.isEmpty()){
            for (String database : databases) {
                DataBase dataBase = new DataBase();
                dataBase.setName(database);
                dataBase.setTables(getTables(database, null));
                dataBaseList.add(dataBase);
            }
        }else if (databases.size()==1){
            for (String database : databases) {
                DataBase dataBase = new DataBase();
                dataBase.setName(database);
                dataBase.setTables(getTables(database, include));
                dataBaseList.add(dataBase);
            }
        }
        return dataBaseList;
    }

    /**
     * 获取某个数据库中的指定表或所有表
     * @param databaseName
     * @param tableNames
     * @return List<Table>
     * @throws SQLException
     */
    public static List<Table> getTables(String databaseName,Set<String> tableNames) throws SQLException {
        ResultSet resultSet=null;
        DataSourceConfig dataSourceConfig=DataSourceConfig.getDataSourceConfig();
        StrategyConfig strategyConfig=StrategyConfig.getStrategyConfig();
        ArrayList<Table> list = new ArrayList<>();
        if(tableNames==null||tableNames.size()==0){
            resultSet = dataSourceConfig.getResultSetTables(databaseName, null, strategyConfig.isSkipView());
            while (resultSet.next()){
                Table table = tableProcessor(dataSourceConfig, databaseName, resultSet);
                if(table!=null){
                    list.add(table);
                }
            }
        }else {
            for (String tableName:tableNames) {
                resultSet = dataSourceConfig.getResultSetTables(databaseName, tableName,strategyConfig.isSkipView());
                while (resultSet.next()) {
                    Table table = tableProcessor(dataSourceConfig, databaseName, resultSet);
                    list.add(table);
                }
            }
        }
        return list;
    }


    /**
     * 表处理器
     * @param dataSourceConfig
     * @param databaseName
     * @param resultSet
     * @return Table
     * @throws SQLException
     */
    public static Table tableProcessor(DataSourceConfig dataSourceConfig,String databaseName,ResultSet resultSet) throws SQLException {
        StrategyConfig strategyConfig = StrategyConfig.getStrategyConfig();

        Table table = new Table();
        //i sql表名称
        String sqlTableName = resultSet.getString("TABLE_NAME");
        table.setSqlTableName(sqlTableName);
        Set<String> tablePrefix = strategyConfig.getTablePrefix();
        Set<String> tableSuffix = strategyConfig.getTableSuffix();
        Set<String> exclude = strategyConfig.getExclude();
        for (String ex : exclude) {
            if(sqlTableName.equals(ex)){
                return null;
            }
        }
        for (String prefix : tablePrefix) {
            if(StringUtils.startsWith(sqlTableName,prefix)){
                StringUtils.removePrefixAfterPrefixToLower(sqlTableName,prefix.length());
            }
        }
        for (String suffix : tableSuffix) {
            if(StringUtils.startsWith(sqlTableName,suffix)){
                StringUtils.removeSuffixAfterPrefixToLower(sqlTableName,suffix.length());
            }
        }
        //ii 处理后的tableName
        String tableNameFilter = tableNameFilter(sqlTableName);
        table.setTableLowerName(tableNameFilter);
        table.setTableUpperName(StringUtils.firstToUpperCase(tableNameFilter));

        //ii 表备注
        table.setComment(resultSet.getString("REMARKS"));

        //iii 表主键
        ResultSet resultSetPrimaryKey = dataSourceConfig.getResultSetPrimaryKeys(databaseName, table.getSqlTableName());
        String keys="";
        while (resultSetPrimaryKey.next()){
            String keyName = resultSetPrimaryKey.getString("COLUMN_NAME");
            keys+=keyName+",";
        }
        keys = keys.substring(0,keys.length() - 1);
        table.setKeys(keys);
        ///iiii 获取列集合
        ResultSet resultSetColumns = dataSourceConfig.getResultSetColumns(table.getSqlTableName(), databaseName);
        List<Column> columns = columnProcessor(resultSetColumns,keys);

        table.setColumns(columns);
        return table;
    }

    /**
     * 列处理器
     * @param resultSet
     * @return List<Column>
     * @throws SQLException
     */
    public static List<Column> columnProcessor(ResultSet resultSet,String keys) throws SQLException {
        StrategyConfig strategyConfig = StrategyConfig.getStrategyConfig();
        Entity entity = StrategyConfig.getStrategyConfig().entity();
        List<IFill> fillList = entity.getTableFillList();
        Set<String> superEntityColumns = entity.getSuperEntityColumns();
        ArrayList<Column> columns = new ArrayList<>();
        Set<String> ignoredColumns = entity.getIgnoredColumns();
        Set<String> fieldPrefix = strategyConfig.getFieldPrefix();
        Set<String> fieldSuffix = strategyConfig.getFieldSuffix();
        while (resultSet.next()){
            Column column = new Column();
            //i 列名称
            String columnName = resultSet.getString("COLUMN_NAME");
            column.setColumnSqlName(columnName);
            for (String igColumn : ignoredColumns) {
                if(columnName.equals(igColumn)){
                    return null;
                }
            }
            for (String prefix : fieldPrefix) {
                if(StringUtils.startsWith(columnName,prefix)){
                    StringUtils.removePrefixAfterPrefixToLower(columnName,prefix.length());
                }
            }
            for (String suffix : fieldSuffix) {
                if(StringUtils.startsWith(columnName,suffix)){
                    StringUtils.removeSuffixAfterPrefixToLower(columnName,suffix.length());
                }
            }
            //ii 属性名
            String str = columnNameFilter(columnName);
            column.setColumnJavaName(str);
            //驼峰名
            str=StringUtils.underlineToCamel(str);
            column.setColumnCamelName(str);
            str=StringUtils.firstToUpperCase(str);
            column.setColumnUpperCamelName(str);
            str=StringUtils.removePrefix(str,"is");
            column.setColumnRemovePrefix(str);
            for (int i = 0; i < fillList.size(); i++) {
                if(fillList.get(i).getName().equals(columnName)){
                    column.setFill(fillList.get(i).getFieldFill().toString());
                    break;
                }
            }
            for (String superEntityColumn : superEntityColumns) {
                if (superEntityColumn.equals(columnName)) {
                    column.setIsSuperColumn(true);
                    break;
                }
            }
            //iii 列类型
            column.setColumnDbType(resultSet.getString("TYPE_NAME"));
            //iiii 列Java类型
            String columnType = PropertiesUtils.customMap.get(column.getColumnDbType());
            column.setColumnType(columnType);
            //iiiii 列备注
            column.setComment(resultSet.getString("REMARKS"));
            //iiiiii 是否为主键
            if(StringUtils.containsSplit(columnName,keys.split(","))){
                column.setIsKey(true);
                column.setKeyIdentityFlag(resultSet.getString("IS_AUTOINCREMENT").equals("YES"));
            }
            if(entity.getVersionColumnName()!=null && entity.getVersionColumnName().equals(columnName)){
                column.setIsVersion(true);
            }
            if (entity.getVersionPropertyName()!=null && entity.getVersionPropertyName().equals(column.getColumnCamelName())) {
                column.setIsVersion(true);
            }
            if(entity.getLogicDeleteColumnName()!=null && entity.getLogicDeleteColumnName().equals(columnName)){
                column.setIsDeleted(true);
            }
            if (entity.getLogicDeletePropertyName()!=null && entity.getLogicDeletePropertyName().equals(column.getColumnCamelName())) {
                column.setIsDeleted(true);
            }
            columns.add(column);
        }
        return columns;
    }


    /**
     * 表名过滤器
     * @param tableName
     * @return String
     */
    public static String tableNameFilter(String tableName){
        StrategyConfig strategyConfig=StrategyConfig.getStrategyConfig();
        Object[] prefixes = strategyConfig.getTablePrefix().toArray();
        for (Object object : prefixes) {
            if (StringUtils.startsWith(tableName,object.toString())) {
                tableName = StringUtils.removePrefixAfterPrefixToLower(tableName,object.toString().length());
            }
        }
        Object[] suffixes = strategyConfig.getTableSuffix().toArray();
        for (Object object : suffixes) {
            if (StringUtils.startsWith(tableName,object.toString())) {
                tableName = StringUtils.removeSuffixAfterPrefixToLower(tableName,object.toString().length());
            }
        }
        tableName=StringUtils.underlineToCamel(tableName);
        return tableName;
    }

    /**
     * 列名过滤器
     * @param columnName
     * @return  String
     */
    public static String columnNameFilter(String columnName) {
        StrategyConfig strategyConfig=StrategyConfig.getStrategyConfig();
        Object[] prefixes = strategyConfig.getFieldPrefix().toArray();
        for (Object object : prefixes) {
            if (StringUtils.startsWith(columnName,object.toString())) {
                columnName = StringUtils.removePrefixAfterPrefixToLower(columnName,object.toString().length());
            }
        }
        Object[] suffixes = strategyConfig.getFieldSuffix().toArray();
        for (Object object : suffixes) {
            if (StringUtils.startsWith(columnName,object.toString())) {
                columnName = StringUtils.removeSuffixAfterPrefixToLower(columnName,object.toString().length());
            }
        }
        return columnName;
    }



}
