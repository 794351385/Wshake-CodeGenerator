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

import com.wshake.generator.config.StrategyConfig;
import com.wshake.generator.config.Table;
import com.wshake.generator.utils.ClassUtils;
import com.wshake.generator.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 实体属性配置
 *
 * @author nieqiurong 2020/10/11.
 * @since 3.5.0
 */
public class Entity implements ITemplate {

    private final static Logger LOGGER = LoggerFactory.getLogger(Entity.class);

    private Entity() {
    }

    private Boolean isOuter = true;
    public Boolean isOuter() {
        return isOuter;
    }

    private String outPath;

    public String getOutPath() {
        return outPath;
    }

    private Boolean newSuperClass=false;
    public Boolean getNewSuperClass() {
        return newSuperClass;
    }

    /**
     * 自定义基础的Entity类，公共字段
     */
    private final Set<String> superEntityColumns = new HashSet<>();


    /**
     * 自定义继承的Entity类全称，带包名
     */
    private String superClass;
    /**
     * 自定义忽略字段
     * https://github.com/baomidou/generator/issues/46
     */
    private final Set<String> ignoreColumns = new HashSet<>();

    /**
     * 实体是否生成 serialVersionUID
     */
    private Boolean serialVersionUID = true;

    /**
     * 【实体】是否生成字段常量（默认 false）<br>
     * -----------------------------------<br>
     * public static final String ID = "test_id";
     */
    private Boolean columnConstant=false;

    /**
     * 【实体】是否为链式模型（默认 false）
     *
     * @since 3.3.2
     */
    private Boolean chain=false;


    /**
     * 【实体】是否为lombok模型（默认 false）<br>
     * <a href="https://projectlombok.org/">document</a>
     */
    private Boolean lombok=false;

    /**
     * Boolean类型字段是否移除is前缀（默认 false）<br>
     * 比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx
     */
    private Boolean columnRemoveIsPrefix=false;

    /**
     * 是否生成实体时，生成字段注解（默认 false）
     */
    private Boolean tableFieldAnnotationEnable=false;

    /**
     * 乐观锁字段名称(数据库字段)
     *
     * @since 3.5.0
     */
    private String versionColumnName;

    /**
     * 乐观锁属性名称(实体字段)
     *
     * @since 3.5.0
     */
    private String versionPropertyName;

    /**
     * 逻辑删除字段名称(数据库字段)
     *
     * @since 3.5.0
     */
    private String logicDeleteColumnName;

    /**
     * 逻辑删除属性名称(实体字段)
     *
     * @since 3.5.0
     */
    private String logicDeletePropertyName;

    /**
     * 表填充字段
     */
    private final List<IFill> tableFillList = new ArrayList<>();

    ///**
    // * 数据库表映射到实体的命名策略，默认下划线转驼峰命名
    // */
    //private NamingStrategy naming = NamingStrategy.underline_to_camel;

    ///**
    // * 数据库表字段映射到实体的命名策略
    // * <p>未指定按照 naming 执行</p>
    // */
    //private NamingStrategy columnNaming = null;

    /**
     * 开启 ActiveRecord 模式（默认 false）
     *
     * @since 3.5.0
     */
    private Boolean activeRecord=false;

    /**
     * 指定生成的主键的ID类型
     *
     * @since 3.5.0
     */
    private IdType idType;

    /**
     * 转换输出文件名称
     *
     * @since 3.5.0
     */
    //private ConverterFileName converterFileName = (entityName -> entityName);

    private String fileSuffixName="Entity";

    private String filePrefixName="";

    /**
     * 是否覆盖已有文件（默认 false）
     *
     * @since 3.5.2
     */
    private Boolean fileOverride=false;

    /**
     * 匹配忽略字段(忽略大小写)
     *
     * @param fieldName 字段名
     * @return 是否匹配
     * @since 3.5.0
     */
    public Boolean matchIgnoreColumns(String fieldName) {
        return ignoreColumns.stream().anyMatch(e -> e.equalsIgnoreCase(fieldName));
    }

    //@NotNull
    //public NamingStrategy getColumnNaming() {
    //    // 未指定以 naming 策略为准
    //    return Optional.ofNullable(columnNaming).orElse(naming);
    //}

    public Boolean isSerialVersionUID() {
        return serialVersionUID;
    }

    public Boolean isColumnConstant() {
        return columnConstant;
    }

    public Boolean isChain() {
        return chain;
    }


    public Boolean isLombok() {
        return lombok;
    }

    public Boolean isBooleanColumnRemoveIsPrefix() {
        return columnRemoveIsPrefix;
    }

    public Boolean isTableFieldAnnotationEnable() {
        return tableFieldAnnotationEnable;
    }

    public Set<String> getSuperEntityColumns() {
        return this.superEntityColumns;
    }
    @Nullable
    public String getVersionColumnName() {
        return versionColumnName;
    }

    @Nullable
    public String getVersionPropertyName() {
        return versionPropertyName;
    }

    @Nullable
    public String getLogicDeleteColumnName() {
        return logicDeleteColumnName;
    }

    @Nullable
    public String getLogicDeletePropertyName() {
        return logicDeletePropertyName;
    }

    @Nullable
    public String getSuperClass() {
        return superClass;
    }

    @NotNull
    public List<IFill> getTableFillList() {
        return tableFillList;
    }

    //@NotNull
    //public NamingStrategy getNaming() {
    //    return naming;
    //}

    public Boolean isActiveRecord() {
        return activeRecord;
    }

    @Nullable
    public IdType getIdType() {
        return idType;
    }

    //@NotNull
    //public ConverterFileName getConverterFileName() {
    //    return converterFileName;
    //}

    public Boolean isFileOverride() {
        return fileOverride;
    }

    @Override
    @NotNull
    public Map<String, Object> renderData() {
        Map<String, Object> data = new HashMap<>();
        data.put("idType", idType == null ? null : idType.toString());
        data.put("logicDeleteFieldName", this.logicDeleteColumnName);
        data.put("versionFieldName", this.versionColumnName);
        data.put("activeRecord", this.activeRecord);
        data.put("entitySerialVersionUID", this.serialVersionUID);
        data.put("entityColumnConstant", this.columnConstant);
        data.put("entityBuilderModel", this.chain);
        data.put("chainModel", this.chain);
        data.put("entityLombokModel", this.lombok);
        data.put("fileSuffixName", this.fileSuffixName);
        data.put("filePrefixName", this.filePrefixName);
        data.put("entityColumnRemoveIsPrefix", this.columnRemoveIsPrefix);
        data.put("superEntityClassPath", this.superClass);
        data.put("superEntityClass", ClassUtils.getSimpleName(this.superClass));
        data.put("tableFills", this.tableFillList);
        data.put("superEntityColumns", this.superEntityColumns);
        data.put("newSuperClass", this.newSuperClass);
        return data;
    }

    /**
     * <p>
     * 父类 Class 反射属性转换为公共字段
     * </p>
     *
     * @param clazz 实体父类 Class
     */
    public void convertSuperEntityColumns(Class<?> clazz) {

    }

    public static class Builder extends BaseBuilder {

        private final Entity entity = new Entity();

        public Builder(StrategyConfig strategyConfig) {
            super(strategyConfig);
        }


        public Entity.Builder noOuter() {
            entity.isOuter = false;
            return this;
        }
        public Entity.Builder outPath(String outPath) {
            entity.outPath = outPath;
            return this;
        }

        public Entity.Builder enabledNewSuperClass() {
            entity.newSuperClass = true;
            return this;
        }


        /**
         * 禁用生成serialVersionUID
         *
         * @return this
         * @since 3.5.0
         */
        public Builder disableSerialVersionUID() {
            this.entity.serialVersionUID = false;
            return this;
        }

        /**
         * 开启生成字段常量
         *
         * @return this
         * @since 3.5.0
         */
        public Builder enableColumnConstant() {
            this.entity.columnConstant = true;
            return this;
        }

        /**
         * 开启链式模型
         *
         * @return this
         * @since 3.5.0
         */
        public Builder enableChainModel() {
            this.entity.chain = true;
            return this;
        }


        /**
         * 开启lombok模型
         *
         * @return this
         * @since 3.5.0
         */
        public Builder enableLombok() {
            this.entity.lombok = true;
            return this;
        }

        /**
         * 开启Boolean类型字段移除is前缀
         *
         * @return this
         * @since 3.5.0
         */
        public Builder enableRemoveIsPrefix() {
            this.entity.columnRemoveIsPrefix = true;
            return this;
        }

        /**
         * 开启生成实体时生成字段注解
         *
         * @return this
         * @since 3.5.0
         */
        public Builder enableTableFieldAnnotation() {
            this.entity.tableFieldAnnotationEnable = true;
            return this;
        }

        /**
         * 开启 ActiveRecord 模式
         *
         * @return this
         * @since 3.5.0
         */
        public Builder enableActiveRecord() {
            this.entity.activeRecord = true;
            return this;
        }

        /**
         * 设置乐观锁数据库表字段名称
         *
         * @param versionColumnName 乐观锁数据库字段名称
         * @return this
         */
        public Builder versionColumnName(String versionColumnName) {
            this.entity.versionColumnName = versionColumnName;
            return this;
        }

        /**
         * 设置乐观锁实体属性字段名称
         *
         * @param versionPropertyName 乐观锁实体属性字段名称
         * @return this
         */
        public Builder versionPropertyName(String versionPropertyName) {
            this.entity.versionPropertyName = versionPropertyName;
            return this;
        }

        /**
         * 逻辑删除数据库字段名称
         *
         * @param logicDeleteColumnName 逻辑删除字段名称
         * @return this
         */
        public Builder logicDeleteColumnName(String logicDeleteColumnName) {
            this.entity.logicDeleteColumnName = logicDeleteColumnName;
            return this;
        }

        /**
         * 逻辑删除实体属性名称
         *
         * @param logicDeletePropertyName 逻辑删除实体属性名称
         * @return this
         */
        public Builder logicDeletePropertyName(String logicDeletePropertyName) {
            this.entity.logicDeletePropertyName = logicDeletePropertyName;
            return this;
        }

        ///**
        // * 数据库表映射到实体的命名策略
        // *
        // * @param namingStrategy 数据库表映射到实体的命名策略
        // * @return this
        // */
        //public Builder naming(NamingStrategy namingStrategy) {
        //    this.entity.naming = namingStrategy;
        //    return this;
        //}

        ///**
        // * 数据库表字段映射到实体的命名策略
        // *
        // * @param namingStrategy 数据库表字段映射到实体的命名策略
        // * @return this
        // */
        //public Builder columnNaming(NamingStrategy namingStrategy) {
        //    this.entity.columnNaming = namingStrategy;
        //    return this;
        //}

        /**
         * 添加忽略字段
         *
         * @param ignoreColumns 需要忽略的字段(数据库字段列名)
         * @return this
         * @since 3.5.0
         */
        public Builder addIgnoreColumns(@NotNull String... ignoreColumns) {
            return addIgnoreColumns(Arrays.asList(ignoreColumns));
        }

        public Builder addIgnoreColumns(@NotNull List<String> ignoreColumnList) {
            this.entity.ignoreColumns.addAll(ignoreColumnList);
            return this;
        }

        /**
         * 添加表字段填充
         *
         * @param tableFills 填充字段
         * @return this
         * @since 3.5.0
         */
        public Builder addTableFills(@NotNull IFill... tableFills) {
            return addTableFills(Arrays.asList(tableFills));
        }

        /**
         * 添加表字段填充
         *
         * @param tableFillList 填充字段集合
         * @return this
         * @since 3.5.0
         */
        public Builder addTableFills(@NotNull List<IFill> tableFillList) {
            this.entity.tableFillList.addAll(tableFillList);
            return this;
        }

        /**
         * 指定生成的主键的ID类型
         *
         * @param idType ID类型
         * @return this
         * @since 3.5.0
         */
        public Builder idType(IdType idType) {
            this.entity.idType = idType;
            return this;
        }

        ///**
        // * 转换输出文件名称
        // *
        // * @param converter 　转换处理
        // * @return this
        // * @since 3.5.0
        // */
        //public Builder convertFileName(@NotNull ConverterFileName converter) {
        //    this.entity.converterFileName = converter;
        //    return this;
        //}

        ///**
        // * 格式化文件名称
        // *
        // * @param format 　格式
        // * @return this
        // * @since 3.5.0
        // */
        //public Builder formatFileName(String format) {
        //    return convertFileName((entityName) -> String.format(format, entityName));
        //}
        public Builder filePrefixName(String filePrefixName){
            this.entity.filePrefixName = filePrefixName;
            return this;
        }
        public Builder fileSuffixName(String fileSuffixName){
            this.entity.fileSuffixName=fileSuffixName;
            return this;
        }

        /**
         * 自定义继承的Entity类全称
         *
         * @param clazz 类
         * @return this
         */
        public Builder superClass(@NotNull Class<?> clazz) {
            return superClass(clazz.getName());
        }

        /**
         * 自定义继承的Entity类全称，带包名
         *
         * @param superEntityClass 类全称
         * @return this
         */
        public Builder superClass(String superEntityClass) {
            this.entity.superClass = superEntityClass;
            return this;
        }
        /**
         * 覆盖已有文件（该方法后续会删除，替代方法为enableFileOverride方法）
         *
         * @see #enableFileOverride()
         */
        @Deprecated
        public Builder fileOverride() {
            LOGGER.warn("fileOverride方法后续会删除，替代方法为enableFileOverride方法");
            this.entity.fileOverride = true;
            return this;
        }

        /**
         * 添加父类公共字段
         *
         * @param superEntityColumns 父类字段(数据库字段列名)
         * @return this
         * @since 3.5.0
         */
        public Builder addSuperEntityColumns(@NotNull String... superEntityColumns) {
            return addSuperEntityColumns(Arrays.asList(superEntityColumns));
        }

        public Builder addSuperEntityColumns(@NotNull List<String> superEntityColumnList) {
            this.entity.superEntityColumns.addAll(superEntityColumnList);
            return this;
        }


        /**
         * 覆盖已有文件
         *
         * @since 3.5.3
         */
        public Builder enableFileOverride() {
            this.entity.fileOverride = true;
            return this;
        }

        public Entity get() {
            String superClass = this.entity.superClass;
            if (StringUtils.isNotBlank(superClass)) {
                tryLoadClass(superClass).ifPresent(this.entity::convertSuperEntityColumns);
            } else {
                if (!this.entity.superEntityColumns.isEmpty()) {
                    LOGGER.warn("Forgot to set entity supper class ?");
                }
            }
            return this.entity;
        }

        private Optional<Class<?>> tryLoadClass(String className) {
            try {
                return Optional.of(ClassUtils.toClassConfident(className));
            } catch (Exception e) {
                //当父类实体存在类加载器的时候,识别父类实体字段，不存在的情况就只有通过指定superEntityColumns属性了。
            }
            return Optional.empty();
        }
    }
}
