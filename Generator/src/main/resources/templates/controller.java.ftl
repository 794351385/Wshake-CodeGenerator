package ${package.Controller};

<#assign controllerName ="${controller.filePrefixName}${table.tableUpperName}${controller.fileSuffixName}"/>
import org.springframework.web.bind.annotation.RequestMapping;
<#if controller.restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if controller.superControllerClassPackage??>
import ${controller.superControllerClassPackage};
</#if>

/**
* ${table.comment!} Controller类
*
* @author ${global.author}
* @since ${global.commentDate}
*/
<#if controller.restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleTwoName?? && package.ModuleTwoName != "">/${package.ModuleTwoName}<#elseif package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/${table.tableLowerName}")
<#if controller.superControllerClass??>
public class ${controllerName} extends ${controller.superControllerClass} {
<#else>
public class ${controllerName} {
</#if>

}
