package com.wshake.generator.config;

import com.wshake.generator.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.sql.DriverManager;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/3
 */
@Data
public class DataSourceConfig{
    protected final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);
    private static DataSourceConfig dataSourceConfig=new DataSourceConfig();

    public static DataSourceConfig getDataSourceConfig() {
        return dataSourceConfig;
    }

    //private Map<String, Object> DataSourceConfigMap=new HashMap<>();
    //@Override
    //public Map<String, Object> getConfigMap() {
    //    Field[] fields = DataSourceConfig.class.getDeclaredFields();
    //    for (Field field: fields) {
    //        field.setAccessible(true);
    //        try {
    //            DataSourceConfigMap.put(field.getName(), field.get(this));
    //        } catch (IllegalAccessException e) {
    //            throw new RuntimeException(e);
    //        }
    //    }
    //    return DataSourceConfigMap;
    //}

    /**
     * 驱动连接的URL
     */
    private String url="jdbc:mysql://localhost:3306?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true&useSSL=false";

    /**
     * 数据库连接用户名
     */
    private String username="root";

    /**
     * 数据库连接密码
     */
    private String password="123456";

    /**
     * 数据源实例
     *
     * @since 3.5.0
     */
    private DataSource dataSource;

    private Properties properties;

    /**
     * 数据库连接
     *
     * @since 3.5.0
     */
    private Connection connection;

    private DatabaseMetaData databaseMetaData;
    @Getter(AccessLevel.NONE)
    private ResultSet resultSet;

    /**
     * 数据库连接属性
     *
     * @since 3.5.3
     */
    private final Map<String, String> connectionProperties = new HashMap<>();

    public Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            } else {
                synchronized (this) {
                    if (dataSource != null) {
                        connection = dataSource.getConnection();
                    } else {
                        properties = new Properties();
                        connectionProperties.forEach(properties::setProperty);
                        properties.put("user", username);
                        properties.put("password", password);
                        properties.put("useInformationSchema", "true");
                        this.connection = DriverManager.getConnection(url, properties);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }


    public DatabaseMetaData getDatabaseMetaData() {
        try {
            if(connection != null && !connection.isClosed()) {
                this.databaseMetaData = connection.getMetaData();
            }else {
                this.databaseMetaData=getConnection().getMetaData();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return databaseMetaData;
    }

    public ResultSet getResultSetColumns(String tableName,String dataBaseName) {
        try {
            if(databaseMetaData != null){
                resultSet=databaseMetaData.getColumns(dataBaseName,null,tableName,null);
            }else {
                resultSet=getDatabaseMetaData().getColumns(dataBaseName,null,tableName,null);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }
    public ResultSet getResultSetTables(String catalog,String tableName,Boolean isView) {
        try {
            if(databaseMetaData != null){
                if(isView){
                    resultSet=databaseMetaData.getTables(catalog,null,tableName,new String[]{"TABLE","VIEW"});
                }
                resultSet=databaseMetaData.getTables(catalog,null,tableName,new String[]{"TABLE"});
            }else {
                if (isView) {
                    resultSet=getDatabaseMetaData().getTables(catalog,null,tableName,new String[]{"TABLE","VIEW"});
                }
                resultSet=getDatabaseMetaData().getTables(catalog,null,tableName,new String[]{"TABLE"});
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }

    public ResultSet getResultSetCatalogs() {
        try {
            if(databaseMetaData != null){
                resultSet=databaseMetaData.getCatalogs();
            }else {
                resultSet=getDatabaseMetaData().getCatalogs();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }
    public ResultSet getResultSetPrimaryKeys(String catalog,String tableName) {
        try {
            if(databaseMetaData != null){
                resultSet=databaseMetaData.getPrimaryKeys(catalog,null,tableName);
            }else {
                resultSet=getDatabaseMetaData().getPrimaryKeys(catalog,null,tableName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }

    /**
     * 构造初始化方法
     *
     * @param url      数据库连接地址
     * @param username 数据库账号
     * @param password 数据库密码
     */
    public void Builder(@NotNull String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void closeAll() throws SQLException {
        if (connection!=null){
            connection.close();
        }
        if(resultSet!=null){
            resultSet.close();
        }
    }




    /**
     * 数据库配置构建者
     *
     * @author nieqiurong 2020/10/10.
     * @since 3.5.0
     */
    public static class Builder implements IConfigBuilder<DataSourceConfig> {

        private final DataSourceConfig dataSourceConfig;

        private Builder() {
            this.dataSourceConfig = DataSourceConfig.getDataSourceConfig();
        }

        /**
         * 构造初始化方法
         *
         * @param url      数据库连接地址
         * @param username 数据库账号
         * @param password 数据库密码
         */
        public Builder(@NotNull String url, String username, String password) {
            this();
            if (StringUtils.isBlank(url)) {
                throw new RuntimeException("无法创建文件，请正确输入 url 配置信息！");
            }
            this.dataSourceConfig.url = url;
            this.dataSourceConfig.username = username;
            this.dataSourceConfig.password = password;
        }

        /**
         * 增加数据库连接属性
         *
         * @param key   属性名
         * @param value 属性值
         * @return this
         * @since 3.5.3
         */
        public Builder addConnectionProperty(@NotNull String key, @NotNull String value) {
            this.dataSourceConfig.connectionProperties.put(key, value);
            return this;
        }


        /**
         * 构建数据库配置
         *
         * @return 数据库配置
         */
        @Override
        public DataSourceConfig build() {
            return this.dataSourceConfig;
        }
    }

}
