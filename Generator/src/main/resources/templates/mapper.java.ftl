package ${package.Mapper};

<#assign mapperName ="${mapper.filePrefixName}${table.tableUpperName}${mapper.fileSuffixName}"/>
<#assign entityName ="${entity.filePrefixName}${table.tableUpperName}${entity.fileSuffixName}"/>
import ${package.Entity}.${entityName};
import ${mapper.superMapperClassPackage};
<#if mapper.mapperAnnotationClass??>
import ${mapper.mapperAnnotationClass.name};
</#if>

/**
* ${table.comment!} Mapper类
*
* @author ${global.author}
* @since ${global.commentDate}
*/
<#if mapper.mapperAnnotationClass??>
@${mapper.mapperAnnotationClass.simpleName}
</#if>
public interface ${mapperName} extends ${mapper.superMapperClass}<${entityName}> {

}
