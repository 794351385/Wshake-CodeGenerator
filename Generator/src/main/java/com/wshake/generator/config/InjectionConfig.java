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
    private List<Injection> injections;
    private Consumer<List<Injection>> beforeOutputFileConsumer;

    @NotNull
    public void beforeOutputFile(List<Injection> injections) {
        if(injections!=null && injections.size()!=0){
            this.injections=injections;
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

        public Builder beforeOutputFile(@NotNull Consumer<List<Injection>> consumer) {
            this.injectionConfig.beforeOutputFileConsumer = consumer;
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
