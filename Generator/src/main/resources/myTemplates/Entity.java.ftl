package ${package.Entity};

<#assign entityName ="${entity.filePrefixName}${table.tableUpperName}${entity.fileSuffixName}"/>
<#if global.isSpringdoc>
import io.swagger.v3.oas.annotations.media.Schema;
<#elseif global.isSwagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
import com.baomidou.mybatisplus.annotation.TableName;
<#if table.keys??>
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
</#if>
<#if entity.entityLombokModel>
import lombok.Getter;
import lombok.Setter;
    <#if entity.chainModel>
import lombok.experimental.Accessors;
    </#if>
</#if>
<#if !entity.superEntityClass??>
    <#if entity.activeRecord>
import com.baomidou.mybatisplus.extension.activerecord.Model;
    </#if>
</#if>
<#if entity.superEntityClassPath??>
import ${entity.superEntityClassPath};
</#if>
<#list table.columns as field>
<#if (field.columnType!"defaultValue")=="BigDecimal">
import java.math.BigDecimal;
    <#break>
</#if>
</#list>
<#list table.columns as field>
    <#if (field.columnType!"defaultValue")=="Timestamp">
import java.sql.Timestamp;
        <#break>
    </#if>
</#list>
<#assign tag="0"/>
<#if entity.activeRecord>
import java.io.Serializable;
<#assign tag="1"/>
</#if>
<#if tag=="0">
<#if entity.entitySerialVersionUID>
import java.io.Serializable;
</#if>
</#if>

/**
* ${table.comment!} ${table.tableUpperName} 表
* Controller类
* @author ${global.author}
* @since ${global.commentDate}
*/
<#if entity.entityLombokModel>
@Getter
@Setter
    <#if entity.chainModel>
@Accessors(chain = true)
    </#if>
</#if>
<#--<#if table.convert>-->
@TableName("${table.sqlTableName}")
<#--</#if>-->
<#if global.isSpringdoc>
@Schema(name = "${table.tableUpperName}", description = "${table.comment!}")
<#elseif global.isSwagger>
@ApiModel(value = "${table.tableUpperName}对象", description = "${table.comment!}")
</#if>
<#if entity.superEntityClass??>
public class ${entityName} extends ${entity.superEntityClass}<#if entity.activeRecord><${entityName}></#if> {
<#elseif entity.activeRecord>
public class ${entityName} extends Model<${entityName}> {
<#elseif entity.entitySerialVersionUID>
public class ${entityName} implements Serializable {
<#else>
public class ${entityName} {
<#--</#if>-->
</#if>
<#if entity.entitySerialVersionUID>
    private static final long serialVersionUID = 1L;
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.columns as field>
    <#if field.isKey>
        <#assign keyPropertyName="${field.columnCamelName}"/>
    </#if>
    <#if field.comment!?length gt 0>
        <#if global.isSpringdoc>
    @Schema(description = "${field.comment}")
        <#elseif global.isSswagger>
    @ApiModelProperty("${field.comment}")
        <#else>
    /**
    * ${field.comment}
    */
        </#if>
    </#if>
    <#if field.isKey>
    <#-- 主键 -->
        <#if field.keyIdentityFlag>
    @TableId(value = "${field.columnSqlName}", type = IdType.AUTO)
        <#elseif entity.idType??>
    @TableId(value = "${field.columnSqlName}", type = IdType.${entity.idType})
        </#if>
    <#-- 普通字段 -->
    <#elseif field.fill??>
    <#-- -----   存在字段填充设置   ----->
    @TableField(fill = FieldFill.${field.fill})
    </#if>
<#-- 乐观锁注解 -->
    <#if field.isVersion>
    @Version
    </#if>
<#-- 逻辑删除注解 -->
    <#if field.isDeleted>
    @TableLogic
    </#if>
    private ${field.columnType} ${field.columnCamelName};
</#list>
<#------------  END 字段循环遍历  ---------->
<#if !entity.entityLombokModel>
    <#list table.columns as field>
        <#if field.columnType == "boolean">
            <#assign getprefix="get"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>
    public ${field.columnType} ${getprefix}${field.columnUpperCamelName}() {
        return ${field.columnCamelName};
    }
        <#if entity.chainModel>
    public ${field.tableUpperName} set${field.columnUpperCamelName}(${field.columnType} ${field.columnCamelName}) {
        <#else>
    public void set${field.columnUpperCamelName}(${field.columnType} ${field.columnCamelName}) {
        </#if>
        this.${field.columnCamelName} = ${field.columnCamelName};
        <#if entity.chainModel>
        return this;
        </#if>
    }
    </#list>
</#if>
<#if entity.entityColumnConstant>
    <#list table.columns as field>
    public static final String ${field.columnUpperCamelName} = "${field.columnSqlName}";
    </#list>
</#if>
<#if entity.activeRecord>
    @Override
    public Serializable pkVal() {
    <#if table.keys??>
        return this.${table.keys};
    <#else>
        return null;
    </#if>
    }
</#if>
<#if !entity.entityLombokModel>
    @Override
    public String toString() {
        return "${table.tableUpperName}{" +
    <#list table.columns as field>
        <#if field_index==0>
            "${field.columnCamelName} = " + ${field.columnCamelName} +
        <#else>
            ", ${field.columnCamelName} = " + ${field.columnCamelName} +
        </#if>
    </#list>
            "}";
    }
</#if>
}