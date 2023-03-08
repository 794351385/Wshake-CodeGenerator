package com.wshake.generator.config;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

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
    private List<Injection> oneInjections;
    private Consumer<List<Injection>> customOneOutputFileConsumer;

    private List<Injection> sqlInjections;
    private Consumer<List<Injection>> customSqlOutputFileConsumer;

    public Boolean isOneInjections(){
        if(oneInjections!=null && oneInjections.size()>0){
            return true;
        }
        return false;
    }
    public List<Injection> getOneInjections(){
        return oneInjections;
    }

    @NotNull
    public void customOneOutputFile(List<Injection> injections) {
        if(injections!=null && injections.size()!=0){
            this.oneInjections=injections;
        }
    }

    public Boolean isSqlInjections(){
        if(sqlInjections!=null && sqlInjections.size()>0){
            return true;
        }
        return false;
    }
    public List<Injection> getSqlInjections(){
        return sqlInjections;
    }

    @NotNull
    public void customSqlOutputFile(List<Injection> injections) {
        if(injections!=null && injections.size()!=0){
            this.sqlInjections=injections;
        }
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

        public Builder customOneOutputFile(@NotNull Consumer<List<Injection>> consumer) {
            this.injectionConfig.customOneOutputFileConsumer = consumer;
            return this;
        }
        public Builder customSqlOutputFile(@NotNull Consumer<List<Injection>> consumer) {
            this.injectionConfig.customSqlOutputFileConsumer = consumer;
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
