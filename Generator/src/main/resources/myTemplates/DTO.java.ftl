package ${package.packageAll}.dto;

<#if global.isSpringdoc>
import io.swagger.v3.oas.annotations.media.Schema;
<#elseif global.isSwagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entity.entityLombokModel>
import lombok.Getter;
import lombok.Setter;
<#if entity.chainModel>
import lombok.experimental.Accessors;
</#if>
</#if>
<#if !entity.superEntityClass??>
<#--<#if entity.activeRecord>-->
<#--import com.baomidou.mybatisplus.extension.activerecord.Model;-->
<#--</#if>-->
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
import java.io.Serializable;

/**
* ${table.comment!} DTO类
*
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
<#if global.isSpringdoc>
@Schema(name = "${table.tableUpperName}", description = "${table.comment!}")
<#elseif global.isSwagger>
@ApiModel(value = "${table.tableUpperName}对象", description = "${table.comment!}")
</#if>
public class ${table.tableUpperName}DTO implements Serializable {
	private static final long serialVersionUID = 1L;
<#list table.columns as field>
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
	private ${field.columnType} ${field.columnCamelName};
</#list>
}