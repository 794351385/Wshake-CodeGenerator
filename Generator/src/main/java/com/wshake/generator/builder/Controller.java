/*
 * Copyright (c) 2011-2022, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wshake.generator.builder;


import com.wshake.generator.config.ConstVal;
import com.wshake.generator.config.StrategyConfig;
import com.wshake.generator.config.Table;
import com.wshake.generator.utils.ClassUtils;
import com.wshake.generator.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 控制器属性配置
 *
 * @author nieqiurong 2020/10/11.
 * @since 3.5.0
 */
public class Controller implements ITemplate {

    private final static Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    private Controller() {
    }

    private Boolean isOuter = true;
    public Boolean isOuter() {
        return isOuter;
    }

    private String outPath;

    public String getOutPath() {
        return outPath;
    }

    private String fileSuffixName="Controller";

    private String filePrefixName="";


    /**
     * 生成 <code>@RestController</code> 控制器（默认 false）
     * <pre>
     *      <code>@Controller</code> -> <code>@RestController</code>
     * </pre>
     */
    private Boolean restStyle=false;

    /**
     * 驼峰转连字符（默认 false）
     * <pre>
     *      <code>@RequestMapping("/managerUserActionHistory")</code> -> <code>@RequestMapping("/manager-user-action-history")</code>
     * </pre>
     */
    private Boolean hyphenStyle=false;

    /**
     * 自定义继承的Controller类全称，带包名
     */
    private String superClass;

    /**
     * 转换输出控制器文件名称
     *
     * @since 3.5.0
     */
    private ConverterFileName converterFileName = (entityName -> entityName + ConstVal.CONTROLLER);

    /**
     * 是否覆盖已有文件（默认 false）
     *
     * @since 3.5.2
     */
    private Boolean fileOverride=false;

    public Boolean isRestStyle() {
        return restStyle;
    }

    public Boolean isHyphenStyle() {
        return hyphenStyle;
    }

    @Nullable
    public String getSuperClass() {
        return superClass;
    }

    @NotNull
    public ConverterFileName getConverterFileName() {
        return converterFileName;
    }

    public Boolean isFileOverride() {
        return fileOverride;
    }

    @Override
    @NotNull
    public Map<String, Object> renderData() {
        Map<String, Object> data = new HashMap<>(5);

        data.put("controllerMappingHyphenStyle", this.hyphenStyle);
        data.put("restControllerStyle", this.restStyle);
        data.put("superControllerClassPackage", StringUtils.isBlank(superClass) ? null : superClass);
        data.put("superControllerClass", ClassUtils.getSimpleName(this.superClass));
        data.put("fileSuffixName", this.fileSuffixName);
        data.put("filePrefixName", this.filePrefixName);
        return data;
    }

    public static class Builder extends BaseBuilder {

        private final Controller controller = new Controller();

        public Builder(@NotNull StrategyConfig strategyConfig) {
            super(strategyConfig);
        }

        public Builder noOuter() {
            controller.isOuter = false;
            return this;
        }
        public Builder outPath(String outPath) {
            controller.outPath = outPath;
            return this;
        }

        public Controller.Builder filePrefixName(String filePrefixName){
            this.controller.filePrefixName = filePrefixName;
            return this;
        }
        public Controller.Builder fileSuffixName(String fileSuffixName){
            this.controller.fileSuffixName=fileSuffixName;
            return this;
        }

        /**
         * 父类控制器
         *
         * @param clazz 父类控制器
         * @return this
         */
        public Builder superClass(@NotNull Class<?> clazz) {
            return superClass(clazz.getName());
        }

        /**
         * 父类控制器
         *
         * @param superClass 父类控制器类名
         * @return this
         */
        public Builder superClass(@NotNull String superClass) {
            this.controller.superClass = superClass;
            return this;
        }

        /**
         * 开启驼峰转连字符
         *
         * @return this
         * @since 3.5.0
         */
        public Builder enableHyphenStyle() {
            this.controller.hyphenStyle = true;
            return this;
        }

        /**
         * 开启生成@RestController控制器
         *
         * @return this
         * @since 3.5.0
         */
        public Builder enableRestStyle() {
            this.controller.restStyle = true;
            return this;
        }

        /**
         * 转换输出文件名称
         *
         * @param converter 　转换处理
         * @return this
         * @since 3.5.0
         */
        public Builder convertFileName(@NotNull ConverterFileName converter) {
            this.controller.converterFileName = converter;
            return this;
        }

        /**
         * 格式化文件名称
         *
         * @param format 　格式
         * @return this
         * @since 3.5.0
         */
        public Builder formatFileName(@NotNull String format) {
            return convertFileName((entityName) -> String.format(format, entityName));
        }

        /**
         * 覆盖已有文件（该方法后续会删除，替代方法为enableFileOverride方法）
         *
         * @see #enableFileOverride()
         */
        @Deprecated
        public Builder fileOverride() {
            LOGGER.warn("fileOverride方法后续会删除，替代方法为enableFileOverride方法");
            this.controller.fileOverride = true;
            return this;
        }

        /**
         * 覆盖已有文件
         *
         * @since 3.5.3
         */
        public Builder enableFileOverride() {
            this.controller.fileOverride = true;
            return this;
        }

        @NotNull
        public Controller get() {
            return this.controller;
        }
    }
}

