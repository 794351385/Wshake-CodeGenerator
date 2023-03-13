package ${package.Service};

<#assign serviceName ="${service.filePrefixNameService}${table.tableUpperName}${service.fileSuffixNameService}"/>
<#assign entityName ="${entity.filePrefixName}${table.tableUpperName}${entity.fileSuffixName}"/>
import ${package.Entity}.${entityName};
import ${package.packageAll}.dto.${table.tableUpperName}DTO;
import ${package.packageTwo}.common.service.CrudService;

/**
* ${table.comment!} ${table.tableUpperName} 表
* Controller类
* @author ${global.author}
* @since ${global.commentDate}
*/
public interface  ${serviceName} extends CrudService<${entityName}, ${table.tableUpperName}DTO> {

}