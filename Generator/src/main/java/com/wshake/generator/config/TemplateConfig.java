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
package com.wshake.generator.config;

import com.wshake.generator.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;


/**
 * 模板路径配置项
 *
 * @author tzg hubin
 * @since 2017-06-17
 */
@Data
public class TemplateConfig {

    private static TemplateConfig templateConfig=new TemplateConfig();
    public static TemplateConfig getTemplateConfig() {
        return templateConfig;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateConfig.class);

    /**
     * 默认模板路径
     */
    private String defaultTempPath;
    /**
     * 自定义模板路径
     */
    private String customTempPath;
    /**
     * 设置实体模板路径
     */
    private String entity;
    /**
     * 设置实体父类模板路径
     */
    private String entityBase;
    /**
     * 设置控制器模板路径
     */
    private String controller;

    /**
     * 设置Mapper模板路径
     */
    private String mapper;

    /**
     * 设置MapperXml模板路径
     */
    private String xml;

    /**
     * 设置Service模板路径
     */
    private String service;

    /**
     * 设置ServiceImpl模板路径
     */
    private String serviceImpl;

    /**
     * 是否禁用实体模板（默认 false）
     */
    private Boolean disableEntity=false;

    /**
     * 不对外爆露
     */
    private TemplateConfig() {
        this.defaultTempPath="/templates";
        this.entity = ConstVal.TEMPLATE_ENTITY_JAVA;
        this.controller = ConstVal.TEMPLATE_CONTROLLER;
        this.mapper = ConstVal.TEMPLATE_MAPPER;
        this.xml = ConstVal.TEMPLATE_XML;
        this.service = ConstVal.TEMPLATE_SERVICE;
        this.serviceImpl = ConstVal.TEMPLATE_SERVICE_IMPL;
        this.entityBase=ConstVal.TEMPLATE_ENTITY_BASE;
    }

    /**
     * 当模板赋值为空时进行日志提示打印
     *
     * @param value        模板值
     * @param templateType 模板类型
     */
    private void logger(String value, TemplateType templateType) {
        if (StringUtils.isBlank(value)) {
            LOGGER.warn("推荐使用disable(TemplateType.{})方法进行默认模板禁用.", templateType.name());
        }
    }

    /**
     * 获取实体模板路径
     *
     * @param kotlin 是否kotlin
     * @return 模板路径
     */
    public String getEntity(Boolean kotlin) {
        if (!this.disableEntity) {
            return StringUtils.isBlank(this.entity) ? ConstVal.TEMPLATE_ENTITY_JAVA : this.entity;
        }
        return null;
    }
    public String getEntityBase(Boolean kotlin) {
        if (!this.disableEntity) {
            return StringUtils.isBlank(this.entityBase) ? ConstVal.TEMPLATE_ENTITY_BASE : this.entityBase;
        }
        return null;
    }

    /**
     * 禁用模板
     *
     * @param templateTypes 模板类型
     * @return this
     * @since 3.3.2
     */
    public TemplateConfig disable(@NotNull TemplateType... templateTypes) {
        if (templateTypes != null && templateTypes.length > 0) {
            for (TemplateType templateType : templateTypes) {
                switch (templateType) {
                    case ENTITY:
                        this.entity = null;
                        //暂时没其他多的需求,使用一个单独的Boolean变量进行支持一下.
                        this.disableEntity = true;
                        break;
                    case CONTROLLER:
                        this.controller = null;
                        break;
                    case MAPPER:
                        this.mapper = null;
                        break;
                    case XML:
                        this.xml = null;
                        break;
                    case SERVICE:
                        this.service = null;
                        break;
                    case SERVICE_IMPL:
                        this.serviceImpl = null;
                        break;
                    default:
                }
            }
        }
        return this;
    }

    /**
     * 禁用全部模板
     *
     * @return this
     * @since 3.5.0
     */
    public TemplateConfig disable() {
        return disable(TemplateType.values());
    }

    public String getService() {
        return service;
    }

    public String getServiceImpl() {
        return serviceImpl;
    }

    public String getMapper() {
        return mapper;
    }

    public String getXml() {
        return xml;
    }

    public String getController() {
        return controller;
    }

    /**
     * 模板路径配置构建者
     *
     * @author nieqiurong 3.5.0
     */
    public static class Builder implements IConfigBuilder<TemplateConfig> {

        private final TemplateConfig templateConfig;

        /**
         * 默认生成一个空的
         */
        public Builder() {
            this.templateConfig = TemplateConfig.getTemplateConfig();
        }


        public Builder entityBase(String entityBase) {
            this.templateConfig.entityBase = entityBase;
            return this;
        }

        /**
         * 禁用所有模板
         *
         * @return this
         */
        public Builder disable() {
            this.templateConfig.disable();
            return this;
        }

        /**
         * 禁用模板
         *
         * @return this
         */
        public Builder disable(@NotNull TemplateType... templateTypes) {
            this.templateConfig.disable(templateTypes);
            return this;
        }
        public Builder setTemplatePath(String templatePath){
            this.templateConfig.defaultTempPath = templatePath;
            return this;
        }

        /**
         * 设置实体模板路径(JAVA)
         *
         * @param entityTemplate 实体模板
         * @return this
         */
        public Builder entity(@NotNull String entityTemplate) {
            this.templateConfig.disableEntity = false;
            this.templateConfig.entity = entityTemplate;
            return this;
        }

        /**
         * 设置实体模板路径(kotlin)
         *
         * @param entityKtTemplate 实体模板
         * @return this
         */
        public Builder entityKt(@NotNull String entityKtTemplate) {
            this.templateConfig.disableEntity = false;
            return this;
        }

        /**
         * 设置service模板路径
         *
         * @param serviceTemplate     service接口模板路径
         * @return this
         */
        public Builder service(@NotNull String serviceTemplate) {
            this.templateConfig.service = serviceTemplate;
            return this;
        }

        /**
         * 设置serviceImpl模板路径
         *
         * @param serviceImplTemplate service实现类模板路径
         * @return this
         */
        public Builder serviceImpl(@NotNull String serviceImplTemplate) {
            this.templateConfig.serviceImpl = serviceImplTemplate;
            return this;
        }

        /**
         * 设置mapper模板路径
         *
         * @param mapperTemplate mapper模板路径
         * @return this
         */
        public Builder mapper(@NotNull String mapperTemplate) {
            this.templateConfig.mapper = mapperTemplate;
            return this;
        }

        /**
         * 设置mapperXml模板路径
         *
         * @param xmlTemplate xml模板路径
         * @return this
         */
        public Builder xml(@NotNull String xmlTemplate) {
            this.templateConfig.xml = xmlTemplate;
            return this;
        }

        /**
         * 设置控制器模板路径
         *
         * @param controllerTemplate 控制器模板路径
         * @return this
         */
        public Builder controller(@NotNull String controllerTemplate) {
            this.templateConfig.controller = controllerTemplate;
            return this;
        }

        /**
         * 构建模板配置对象
         *
         * @return 模板配置对象
         */
        @Override
        public TemplateConfig build() {
            return this.templateConfig;
        }
    }
}
