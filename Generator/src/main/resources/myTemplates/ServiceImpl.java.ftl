package ${package.ServiceImpl};

<#assign entityName ="${entity.filePrefixName}${table.tableUpperName}${entity.fileSuffixName}"/>
<#assign mapperName ="${mapper.filePrefixNameMapper}${table.tableUpperName}${mapper.fileSuffixNameMapper}"/>
<#assign serviceName ="${service.filePrefixNameService}${table.tableUpperName}${service.fileSuffixNameService}"/>
<#assign serviceImplName ="${service.filePrefixNameServiceImpl}${table.tableUpperName}${service.fileSuffixNameServiceImpl}"/>
import ${package.packageTwo}.common.service.impl.CrudServiceImpl;
import org.springframework.stereotype.Service;
import ${package.Entity}.${entityName};
import ${package.packageAll}.dto.${table.tableUpperName}DTO;
import ${package.Mapper}.${mapperName};
import ${package.Service}.${serviceName};

/**
* ${table.comment!} ${table.tableUpperName} 表
* Controller类
* @author ${global.author}
* @since ${global.commentDate}
*/
@Service
public class ${serviceImplName} extends CrudServiceImpl<${mapperName}, ${entityName}, ${table.tableUpperName}DTO> implements ${serviceName} {

}