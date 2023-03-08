package com.wshake.generator.config;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/6
 * 注入模板配置
 */

public class InjectionConfig {

    private static InjectionConfig injectionConfig=new InjectionConfig();
    public static InjectionConfig getInjectionConfig(){
        return injectionConfig;
    }
    private List<TableColumn> oneInjections;
    private Supplier<List<TableColumn>> customOneOutputFileConsumer;

    private List<TableColumn> tableColumns;
    private Supplier<List<TableColumn>> customSqlOutputFileConsumer;

    public Boolean isOneInjections(){
        if(oneInjections!=null && oneInjections.size()>0){
            return true;
        }
        return false;
    }
    public List<TableColumn> getOneInjections(){
        return oneInjections;
    }

    public Boolean isSqlInjections(){
        if(tableColumns !=null && tableColumns.size()>0){
            return true;
        }
        return false;
    }
    public List<TableColumn> getSqlInjections(){
        return tableColumns;
    }

    private String fileSuffixName="";
    private String filePrefixName="";

    public String getFileSuffixName() {
        return fileSuffixName;
    }
    public String getFilePrefixName(){
        return filePrefixName;
    }
    @NotNull
    public Map<String, Object> renderData() {
        Map<String, Object> data = new HashMap<>();
        data.put("fileSuffixName", this.fileSuffixName);
        data.put("filePrefixName", this.filePrefixName);
        return data;
    }
    /**
     * 模板路径配置构建者
     *
     * @author nieqiurong 3.5.0
     */
    public static class Builder implements IConfigBuilder<InjectionConfig> {

        private final InjectionConfig injectionConfig;

        /**
         * 默认生成一个空的
         */
        public Builder() {
            this.injectionConfig = InjectionConfig.getInjectionConfig();
        }

        public Builder customOneOutputFile(@NotNull Supplier<List<TableColumn>> consumer) {
            this.injectionConfig.customOneOutputFileConsumer = consumer;
            this.injectionConfig.oneInjections = this.injectionConfig.customOneOutputFileConsumer.get();
            return this;
        }
        public Builder customSqlOutputFile(@NotNull Supplier<List<TableColumn>> consumer) {
            this.injectionConfig.customSqlOutputFileConsumer = consumer;
            this.injectionConfig.tableColumns = this.injectionConfig.customSqlOutputFileConsumer.get();
            return this;
        }

        /**
         * 构建模板配置对象
         *
         * @return 模板配置对象
         */
        @Override
        public InjectionConfig build() {
            return this.injectionConfig;
        }
    }
}
