package ${package.Service};

<#assign serviceName ="${service.filePrefixNameService}${table.tableUpperName}${service.fileSuffixNameService}"/>
<#assign entityName ="${entity.filePrefixName}${table.tableUpperName}${entity.fileSuffixName}"/>
import ${package.Entity}.${entityName};
import ${service.superServiceClassPackage};

/**
* ${table.comment!} Service接口
*
* @author ${global.author}
* @since ${global.commentDate}
*/
public interface ${serviceName} extends ${service.superServiceClass}<${entityName}> {

}
