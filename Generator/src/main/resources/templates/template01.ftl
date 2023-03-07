<#--assign指令 在ftl模板中定义数据存入到root节点下-->
<#assign name="张三">
${name}

欢迎您，${username}
<#--这是一段注释-->
<#--<#..>FTL指令-->
<#--<@>宏-->

<#--if指令-->
<#if flag=1>
    x
    <#elseif flag=2>
    xx
    <#else >
    xxx
</#if>

<#--List 指令 循环迭代 数据名称 as 别名-->
<#list list as data>
    ${data_index}=${data}
</#list>

<#--模板保护 include-->
<#include "template02.ftl">
<#--?加方法 使用方法-->
${username?uncap_first}