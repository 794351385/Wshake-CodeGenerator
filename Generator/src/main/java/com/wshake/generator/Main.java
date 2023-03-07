package com.wshake.generator;

import com.wshake.generator.builder.ColumnFill;
import com.wshake.generator.builder.FieldFill;
import com.wshake.generator.builder.IdType;
import com.wshake.generator.config.Injection;
import com.wshake.generator.core.GeneratorFacade;

import java.io.IOException;
import java.sql.SQLException;

import freemarker.template.TemplateException;


/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/5
 */
public class Main {
    public static void main(String[] args) throws SQLException, TemplateException, IOException {
        String property=System.getProperty("user.dir") +"/Generator/src/main/java";
        GeneratorFacade.create("jdbc:mysql://localhost:3306?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true&useSSL=false",
                "root","123456")
                .globalConfig(builder -> {
                    builder.author("Wshake")
                            .build();
                })
                .globalConfig(builder -> {
                    builder.outputDir(property)
                            .enableSpringdoc();
                })
                .packageConfig(builder -> {
                    builder.parent("com.wshake")
                            .moduleName("generator")
                            //.setModulTwoName("mall")
                            .build();
                })
                .strategyConfig(builder -> {
                    builder.addDataBase("order");
                    builder.controllerBuilder()
                            //.noOuter()
                            .build();
                    builder.entityBuilder()
                            .enableColumnConstant()
                            .enableActiveRecord()
                            .logicDeleteColumnName("is_deleted")
                            .idType(IdType.AUTO)
                            .enableLombok()
                            .superClass(Main.class)
                            .enableChainModel()
                            .addTableFills(new ColumnFill("username",FieldFill.INSERT))
                            .superClass(Main.class)
                            .addSuperEntityColumns("id","update_time")
                            .noOuter()
                            .build();
                    builder.serviceBuilder()
                            .noOuterService()
                            .noOuterServiceImpl()
                            .build();
                    builder.mapperBuilder()
                            //.superClass(Main.class)
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .noOuterXml()
                            .noOuterMapper()
                            .build();
                })
                .injectionConfig(builder -> {
                    //builder.beforeOutputFile(list->{
                    //    Injection injection = new Injection();
                    //    injection.setTemplatePath();
                    //})
                })
                .execute();
    }
}
