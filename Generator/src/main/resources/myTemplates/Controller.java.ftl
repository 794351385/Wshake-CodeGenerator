package ${package.Controller};

<#assign controllerName ="${controller.filePrefixName}${table.tableUpperName}${controller.fileSuffixName}"/>
<#assign entityName ="${entity.filePrefixName}${table.tableUpperName}${entity.fileSuffixName}"/>
<#assign mapperName ="${mapper.filePrefixNameMapper}${table.tableUpperName}${mapper.fileSuffixNameMapper}"/>
<#assign serviceName ="${service.filePrefixNameService}${table.tableUpperName}${service.fileSuffixNameService}"/>
<#assign serviceImplName ="${service.filePrefixNameServiceImpl}${table.tableUpperName}${service.fileSuffixNameServiceImpl}"/>
import ${package.packageTwo}.common.annotation.LogOperation;
import ${package.packageTwo}.common.constant.Constant;
import ${package.packageTwo}.common.page.PageData;
import ${package.packageTwo}.common.utils.R;
import ${package.packageTwo}.common.validator.AssertUtils;
import ${package.packageTwo}.common.validator.ValidatorUtils;
import ${package.packageTwo}.common.validator.group.AddGroup;
import ${package.packageTwo}.common.validator.group.DefaultGroup;
import ${package.packageTwo}.common.validator.group.UpdateGroup;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ${package.Service}.${serviceName};
import ${package.Entity}.${entityName};
import ${package.packageAll}.dto.${table.tableUpperName}DTO;
import ${package.packageTwo}.common.constant.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
<#if global.isSpringdoc>
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
<#elseif global.isSwagger>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
</#if>

import java.util.List;
import java.util.Map;

/**
* ${table.comment!} ${table.tableUpperName} 表
* Controller类
* @author ${global.author}
* @since ${global.commentDate}
*/
<#if controller.restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if><#if package.ModuleTwoName?? && package.ModuleTwoName != "">/${package.ModuleTwoName}</#if>/${table.tableLowerName}")
<#if global.isSpringdoc>
@Tag(name="${table.comment!}")
<#elseif global.isSwagger>
@Api(tag="${table.comment!}")
</#if>
@Slf4j
<#if controller.superControllerClass??>
public class ${controllerName} extends ${controller.superControllerClass} {
<#else>
public class ${controllerName} {
</#if>
    @Autowired
    private ${serviceName} ${serviceName?uncap_first};

    @PostMapping("page/{limit}/{current}")
    <#if global.isSpringdoc>
    @Operation(summary = "分页")
    @Parameters({
        @Parameter(name = Constant.LIMIT, description = "当前页码，从1开始", in = ParameterIn.QUERY, required = true, schema = @Schema(type = "integer")),
        @Parameter(name = Constant.CURRENT, description = "每页显示记录数", in = ParameterIn.QUERY, required = true, schema = @Schema(type = "integer")),
    })
    <#elseif global.isSwagger>
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.LIMIT, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "integer"),
        @ApiImplicitParam(name = Constant.CURRENT, value = "每页显示记录数", paramType = "query", required = true, dataType = "integer"),
    })
    </#if>
<#--    @RequiresPermissions("${moduleName}:${pathName}:page")-->
<#assign nameDTO="${table.tableUpperName}DTO" />
    public R<PageData<#noparse><</#noparse>${nameDTO}>> page(@PathVariable long limit, @PathVariable long current, @RequestBody(required = false) PageVo<#noparse><</#noparse>${entityName}> pageVo){
        QueryWrapper<${entityName}> wrapper = new QueryWrapper<>();
        PageData<#noparse><</#noparse>${nameDTO}> page = ${serviceName?uncap_first}.pageDTO(limit,current,pageVo,wrapper);
        return R.ok(page);
    }

    @GetMapping("{id}")
    <#if global.isSpringdoc>
    @Operation(summary = "信息")
    <#elseif global.isSwagger>
    @ApiOperation("信息")
    </#if>
<#--    @RequiresPermissions("${moduleName}:${pathName}:info")-->
    public R<${nameDTO}> get(@PathVariable("id") Long id){
        ${nameDTO} data = ${serviceName?uncap_first}.getDTO(id);
        return R.ok(data);
    }

    @PostMapping("save")
    <#if global.isSpringdoc>
    @Operation(summary = "保存")
    <#elseif global.isSwagger>
    @ApiOperation("保存")
    </#if>
    @LogOperation("保存")
<#--    @RequiresPermissions("${moduleName}:${pathName}:save")-->
    public R save(@RequestBody ${table.tableUpperName}DTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        ${serviceName?uncap_first}.saveDTO(dto);
        return R.ok();
    }

    @PutMapping("update")
    <#if global.isSpringdoc>
    @Operation(summary = "修改")
    <#elseif global.isSwagger>
    @ApiOperation("修改")
    </#if>
    @LogOperation("修改")
<#--    @RequiresPermissions("${moduleName}:${pathName}:update")-->
    public R update(@RequestBody ${table.tableUpperName}DTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        ${serviceName?uncap_first}.updateDTO(dto);
        return R.ok();
    }

    @DeleteMapping("delete")
    <#if global.isSpringdoc>
    @Operation(summary = "删除")
    <#elseif global.isSwagger>
    @ApiOperation("删除")
    </#if>
    @LogOperation("删除")
<#--    @RequiresPermissions("${moduleName}:${pathName}:delete")-->
    public R delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        ${serviceName?uncap_first}.deleteDTO(ids);
        return R.ok();
    }
<#--    ##@GetMapping("export")-->
<#--    ##@ApiOperation("导出")-->
<#--    ##@LogOperation("导出")-->
<#--    ####@RequiresPermissions("${moduleName}:${pathName}:export")-->
<#--    ##public void export(@ApiIgnore @RequestParam ${entity}DTO dto, HttpServletResponse response) throws Exception {-->
<#--    ##    QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();-->
<#--    ##    List<${entity}DTO> list = ${service}.listDTO(wrapper);-->
<#--    ##-->
<#--    ##    ExcelUtils.exportExcelToTarget(response, null, list, ${entity}Excel.class);-->
<#--    ##}-->
}