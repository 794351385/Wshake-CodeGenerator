package ${package.ServiceImpl};

<#assign entityName ="${entity.filePrefixName}${table.tableUpperName}${entity.fileSuffixName}"/>
<#assign mapperName ="${mapper.filePrefixNameMapper}${table.tableUpperName}${mapper.fileSuffixNameMapper}"/>
<#assign serviceName ="${service.filePrefixNameService}${table.tableUpperName}${service.fileSuffixNameService}"/>
<#assign serviceImplName ="${service.filePrefixNameServiceImpl}${table.tableUpperName}${service.fileSuffixNameServiceImpl}"/>
import ${package.Entity}.${entityName};
import ${package.Mapper}.${mapperName};
<#if global.isServiceInterface>
import ${package.Service}.${serviceName};
</#if>
import ${service.superServiceImplClassPackage};
import org.springframework.stereotype.Service;

/**
* ${table.comment!} ServiceImpl类
*
* @author ${global.author}
* @since ${global.commentDate}
*/
@Service
public class ${serviceImplName} extends ${service.superServiceImplClass}<${mapperName}, ${entityName}><#if global.isServiceInterface> implements ${serviceName}</#if> {

}
