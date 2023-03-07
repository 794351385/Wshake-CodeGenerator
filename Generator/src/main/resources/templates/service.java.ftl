package ${package.Service};

<#assign serviceName ="${service.filePrefixName}${table.tableUpperName}${service.fileSuffixName}"/>
<#assign entityName ="${entity.filePrefixName}${table.tableUpperName}${entity.fileSuffixName}"/>
import ${package.Entity}.${entityName};
import ${service.superServiceClassPackage};

/**
* ${table.comment!} Service类
*
* @author ${global.author}
* @since ${global.commentDate}
*/
public interface ${serviceName} extends ${service.superServiceClass}<${entityName}> {

}
