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

import com.wshake.generator.builder.BaseBuilder;
import com.wshake.generator.builder.Controller;
import com.wshake.generator.builder.Entity;
import com.wshake.generator.builder.Mapper;
import com.wshake.generator.builder.Service;
import com.wshake.generator.utils.StringUtils;

import org.apache.ibatis.session.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Data;

/**
 * 策略配置项
 *
 * @author YangHu, tangguo, hubin
 * @since 2016/8/30
 */
@Data
public class StrategyConfig extends Configuration {

    private static StrategyConfig strategyConfig = new StrategyConfig();

    public static StrategyConfig getStrategyConfig() {
        return strategyConfig;
    }

    private StrategyConfig() {
    }

    /**
     * 是否覆盖已有文件（默认 false）（已迁移到策略配置中，3.5.4版本会删除此方法）
     */
    private Boolean fileOverride=false;
    /**
     * 需要生成的数据库
     */
    private Set<String> databases = new HashSet<>();

    /**
     * 是否大写命名（默认 false）
     */
    private Boolean isCapitalMode=false;

    /**
     * 是否跳过视图（默认 false）
     */
    private Boolean skipView=false;

    /**
     * 过滤表前缀
     * example: addTablePrefix("t_")
     * result: t_simple -> Simple
     */
    private final Set<String> tablePrefix = new HashSet<>();

    /**
     * 过滤表后缀
     * example: addTableSuffix("_0")
     * result: t_simple_0 -> Simple
     */
    private final Set<String> tableSuffix = new HashSet<>();

    /**
     * 过滤字段前缀
     * example: addFieldPrefix("is_")
     * result: is_deleted -> deleted
     */
    private final Set<String> fieldPrefix = new HashSet<>();

    /**
     * 过滤字段后缀
     * example: addFieldSuffix("_flag")
     * result: deleted_flag -> deleted
     */
    private final Set<String> fieldSuffix = new HashSet<>();
    /**
     * 过滤数据库前缀
     * example: addFieldPrefix("is_")
     * result: is_deleted -> deleted
     */
    private final Set<String> dataBasePrefix = new HashSet<>();

    /**
     * 过滤数据库后缀
     * example: addFieldSuffix("_flag")
     * result: deleted_flag -> deleted
     */
    private final Set<String> dataBaseSuffix = new HashSet<>();

    /**
     * 需要包含的表名，允许正则表达式（与exclude二选一配置）<br/>
     * 当{@link #enableSqlFilter}为true时，正则表达式无效.
     */
    private final Set<String> include = new HashSet<>();

    /**
     * 需要排除的表名，允许正则表达式<br/>
     * 当{@link #enableSqlFilter}为true时，正则表达式无效.
     */
    private final Set<String> exclude = new HashSet<>();

    /**
     * 启用sql过滤，语法不能支持使用sql过滤表的话，可以考虑关闭此开关.
     *
     * @since 3.3.1
     */
    private Boolean enableSqlFilter = true;


    private final Entity.Builder entityBuilder = new Entity.Builder(this);

    private final Controller.Builder controllerBuilder = new Controller.Builder(this);

    private final Mapper.Builder mapperBuilder = new Mapper.Builder(this);

    private final Service.Builder serviceBuilder = new Service.Builder(this);

    private Entity entity;

    private Controller controller;

    private Mapper mapper;

    private Service service;

    private LikeTable likeTable;
    private LikeTable notLikeTable;

    public Boolean getIsFileOverride() {
        return fileOverride;
    }

    public @Nullable LikeTable getLikeTable() {
        return this.likeTable;
    }

    public @Nullable LikeTable getNotLikeTable() {
        return this.notLikeTable;
    }

    /**
     * 实体配置构建者
     *
     * @return 实体配置构建者
     * @since 3.5.0
     */
    @NotNull
    public Entity.Builder entityBuilder() {
        return entityBuilder;
    }

    /**
     * 实体配置
     *
     * @return 实体配置
     * @since 3.5.0
     */
    @NotNull
    public Entity entity() {
        if (entity == null) {
            this.entity = entityBuilder.get();
        }
        return entity;
    }

    /**
     * 控制器配置构建者
     *
     * @return 控制器配置构建者
     * @since 3.5.0
     */
    @NotNull
    public Controller.Builder controllerBuilder() {
        return controllerBuilder;
    }

    /**
     * 控制器配置
     *
     * @return 控制器配置
     * @since 3.5.0
     */
    @NotNull
    public Controller controller() {
        if (controller == null) {
            this.controller = controllerBuilder.get();
        }
        return controller;
    }

    /**
     * Mapper配置构建者
     *
     * @return Mapper配置构建者
     * @since 3.5.0
     */
    @NotNull
    public Mapper.Builder mapperBuilder() {
        return mapperBuilder;
    }

    /**
     * Mapper配置
     *
     * @return Mapper配置
     * @since 3.5.0
     */
    @NotNull
    public Mapper mapper() {
        if (mapper == null) {
            this.mapper = mapperBuilder.get();
        }
        return mapper;
    }

    /**
     * Service配置构建者
     *
     * @return Service配置构建者
     * @since 3.5.0
     */
    @NotNull
    public Service.Builder serviceBuilder() {
        return serviceBuilder;
    }

    /**
     * Service配置
     *
     * @return Service配置
     * @since 3.5.0
     */
    @NotNull
    public Service service() {
        if (service == null) {
            this.service = serviceBuilder.get();
        }
        return service;
    }


    /**
     * 大写命名、字段符合大写字母数字下划线命名
     *
     * @param word 待判断字符串
     */
    public Boolean isCapitalModeNaming(@NotNull String word) {
        return isCapitalMode && StringUtils.isCapitalMode(word);
    }

    /**
     * 表名称匹配过滤表前缀
     *
     * @param tableName 表名称
     * @since 3.3.2
     */
    public Boolean startsWithTablePrefix(@NotNull String tableName) {
        return this.tablePrefix.stream().anyMatch(tableName::startsWith);
    }

    /**
     * 验证配置项
     *
     * @since 3.5.0
     */
    public void validate() {
        Boolean isInclude = this.getInclude().size() > 0;
        Boolean isExclude = this.getExclude().size() > 0;
        if (isInclude && isExclude) {
            throw new IllegalArgumentException("<strategy> 标签中 <include> 与 <exclude> 只能配置一项！");
        } else if (this.getNotLikeTable() != null && this.getLikeTable() != null) {
            throw new IllegalArgumentException("<strategy> 标签中 <likeTable> 与 <notLikeTable> 只能配置一项！");
        }
    }

    /**
     * 包含表名匹配
     *
     * @param tableName 表名
     * @return 是否匹配
     * @since 3.5.0
     */
    public Boolean matchIncludeTable(@NotNull String tableName) {
        return matchTable(tableName, this.getInclude());
    }

    /**
     * 排除表名匹配
     *
     * @param tableName 表名
     * @return 是否匹配
     * @since 3.5.0
     */
    public Boolean matchExcludeTable(@NotNull String tableName) {
        return matchTable(tableName, this.getExclude());
    }

    /**
     * 表名匹配
     *
     * @param tableName   表名
     * @param matchTables 匹配集合
     * @return 是否匹配
     * @since 3.5.0
     */
    private Boolean matchTable(@NotNull String tableName, @NotNull Set<String> matchTables) {
        return matchTables.stream().anyMatch(t -> tableNameMatches(t, tableName));
    }

    /**
     * 表名匹配
     *
     * @param matchTableName 匹配表名
     * @param dbTableName    数据库表名
     * @return 是否匹配
     */
    private Boolean tableNameMatches(@NotNull String matchTableName, @NotNull String dbTableName) {
        return matchTableName.equalsIgnoreCase(dbTableName) || StringUtils.matches(matchTableName, dbTableName);
    }

    public Boolean isCapitalMode() {
        return isCapitalMode;
    }

    public Boolean isSkipView() {
        return skipView;
    }

    @NotNull
    public Set<String> getTablePrefix() {
        return tablePrefix;
    }

    @NotNull
    public Set<String> getTableSuffix() {
        return tableSuffix;
    }

    @NotNull
    public Set<String> getFieldPrefix() {
        return fieldPrefix;
    }

    @NotNull
    public Set<String> getFieldSuffix() {
        return fieldSuffix;
    }

    @NotNull
    public Set<String> getInclude() {
        return include;
    }

    @NotNull
    public Set<String> getExclude() {
        return exclude;
    }

    @NotNull
    public Set<String> getDatabases() {
        return databases;
    }

    public Boolean isEnableSqlFilter() {
        return enableSqlFilter;
    }


    /**
     * 策略配置构建者
     *
     * @author nieqiurong 2020/10/11.
     * @since 3.5.0
     */
    public static class Builder extends BaseBuilder {

        private final StrategyConfig strategyConfig= super.build();

        public Builder() {
            super(StrategyConfig.getStrategyConfig());
        }


        /**
         * 覆盖已有文件
         */
        public Builder fileOverride() {
            this.strategyConfig.fileOverride = true;
            return this;
        }
        /**
         * 开启大写命名
         *
         * @return this
         * @since 3.5.0
         */
        public Builder enableCapitalMode() {
            this.strategyConfig.isCapitalMode = true;
            return this;
        }

        /**
         * 开启跳过视图
         *
         * @return this
         * @since 3.5.0
         */
        public Builder enableSkipView() {
            this.strategyConfig.skipView = true;
            return this;
        }


        /**
         * 增加过滤表前缀
         *
         * @param tablePrefix 过滤表前缀
         * @return this
         * @since 3.5.0
         */
        public Builder addTablePrefix(@NotNull String... tablePrefix) {
            return addTablePrefix(Arrays.asList(tablePrefix));
        }

        public Builder addTablePrefix(@NotNull List<String> tablePrefixList) {
            this.strategyConfig.tablePrefix.addAll(tablePrefixList);
            return this;
        }

        /**
         * 增加过滤表后缀
         *
         * @param tableSuffix 过滤表后缀
         * @return this
         * @since 3.5.1
         */
        public Builder addTableSuffix(String... tableSuffix) {
            return addTableSuffix(Arrays.asList(tableSuffix));
        }

        public Builder addTableSuffix(@NotNull List<String> tableSuffixList) {
            this.strategyConfig.tableSuffix.addAll(tableSuffixList);
            return this;
        }

        /**
         * 增加过滤字段前缀
         *
         * @param fieldPrefix 过滤字段前缀
         * @return this
         * @since 3.5.0
         */
        public Builder addFieldPrefix(@NotNull String... fieldPrefix) {
            return addFieldPrefix(Arrays.asList(fieldPrefix));
        }

        public Builder addFieldPrefix(@NotNull List<String> fieldPrefix) {
            this.strategyConfig.fieldPrefix.addAll(fieldPrefix);
            return this;
        }

        /**
         * 增加过滤字段后缀
         *
         * @param fieldSuffix 过滤字段后缀
         * @return this
         * @since 3.5.1
         */
        public Builder addFieldSuffix(@NotNull String... fieldSuffix) {
            return addFieldSuffix(Arrays.asList(fieldSuffix));
        }

        public Builder addFieldSuffix(@NotNull List<String> fieldSuffixList) {
            this.strategyConfig.fieldSuffix.addAll(fieldSuffixList);
            return this;
        }

        /**
         * 增加过滤数据库前缀
         *
         * @param dataBasePrefix 过滤数据库前缀
         * @return this
         * @since 3.5.0
         */
        public Builder addDataBasePrefix(@NotNull String... dataBasePrefix) {
            return addDataBasePrefix(Arrays.asList(dataBasePrefix));
        }

        public Builder addDataBasePrefix(@NotNull List<String> dataBasePrefix) {
            this.strategyConfig.dataBasePrefix.addAll(dataBasePrefix);
            return this;
        }

        /**
         * 增加过滤数据库后缀
         *
         * @param dataBaseSuffix 过滤数据库后缀
         * @return this
         * @since 3.5.1
         */
        public Builder addDataBaseSuffix(@NotNull String... dataBaseSuffix) {
            return addFieldSuffix(Arrays.asList(dataBaseSuffix));
        }

        public Builder addDataBaseSuffix(@NotNull List<String> dataBaseSuffix) {
            this.strategyConfig.dataBaseSuffix.addAll(dataBaseSuffix);
            return this;
        }

        /**
         * 增加包含的表名
         *
         * @param include 包含表
         * @return this
         * @since 3.5.0
         */
        public Builder addInclude(@NotNull String... include) {
            this.strategyConfig.include.addAll(Arrays.asList(include));
            return this;
        }

        public Builder addInclude(@NotNull List<String> includes) {
            this.strategyConfig.include.addAll(includes);
            return this;
        }

        public Builder addInclude(@NotNull String include) {
            this.strategyConfig.include.addAll(Arrays.asList(include.split(",")));
            return this;
        }

        /**
         * 增加需要生成的数据库(所有表)
         *
         * @param dataBase
         * @return
         */
        public Builder addDataBase(@NotNull String... dataBase) {
            this.strategyConfig.databases.addAll(Arrays.asList(dataBase));
            return this;
        }

        public Builder addDataBase(@NotNull List<String> dataBase) {
            this.strategyConfig.databases.addAll(dataBase);
            return this;
        }

        public Builder addDataBase(@NotNull String dataBase) {
            this.strategyConfig.databases.addAll(Arrays.asList(dataBase.split(",")));
            return this;
        }

        /**
         * 增加排除表
         *
         * @param exclude 排除表
         * @return this
         * @since 3.5.0
         */
        public Builder addExclude(@NotNull String... exclude) {
            return addExclude(Arrays.asList(exclude));
        }

        public Builder addExclude(@NotNull List<String> excludeList) {
            this.strategyConfig.exclude.addAll(excludeList);
            return this;
        }


        @NotNull
        public StrategyConfig build() {
            this.strategyConfig.validate();
            return this.strategyConfig;
        }
    }
}
