<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<#assign entityName ="${entity.filePrefixName}${table.tableUpperName}${entity.fileSuffixName}"/>
<mapper namespace="${package.Xml}">

<#if mapper.enableCache>
    <!-- 开启二级缓存 -->
    <cache type="${mapper.cacheClassName}"/>

</#if>
<#if mapper.baseResultMap>
    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package.Entity}.${entityName}">
<#list table.columns as field>
<#if field.isKey><#--生成主键排在第一位-->
        <id column="${field.columnSqlName}" property="${field.columnCamelName}" />
</#if>
</#list>
<#--<#list table.commonFields as field>&lt;#&ndash;生成公共字段 &ndash;&gt;-->
<#--        <result column="${field.columnSqlName}" property="${field.columnCamelName}" />-->
<#--</#list>-->
<#--<#list table.commonFields as field>-->
<#--<#if !field.isKey>&lt;#&ndash;生成普通字段 &ndash;&gt;-->
<#--        <result column="${field.columnSqlName}" property="${field.columnCamelName}" />-->
<#--</#if>-->
<#--</#list>-->
<#list table.columns as field>
    <#if !field.isKey><#--生成普通字段 -->
        <result column="${field.columnSqlName}" property="${field.columnCamelName}" />
    </#if>
</#list>
    </resultMap>

</#if>
<#if mapper.baseColumnList>
    <!-- 通用查询结果列 -->
<#--    <sql id="Base_Column_List">-->
<#--<#list table.commonFields as field>-->
<#--        ${field.columnName},-->
<#--</#list>-->
<#--        ${table.fieldNames}-->
<#--    </sql>-->
    <sql id="Base_Column_List">
        <#list table.columns as field>
            ${field.columnSqlName},
        </#list>
    </sql>

</#if>
</mapper>
