package com.wshake.generator;

import com.wshake.generator.builder.ColumnFill;
import com.wshake.generator.builder.FieldFill;
import com.wshake.generator.builder.IdType;
import com.wshake.generator.config.TableColumn;
import com.wshake.generator.core.GeneratorFacade;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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
                    builder.outputDir(property)
                            .author("Wshake")
                            .enableSpringdoc()
                            .build();
                })
                .packageConfig(builder -> {
                    builder.parent("com.wshake")
                            .moduleName("generator")
                            .build();
                })

                .strategyConfig(builder -> {
                    builder.addDataBase("students")
                            //.fileOverride()
                            .build();
                    builder.controllerBuilder()
                            .noOuter()
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
                            .addSuperEntityColumns("id","update_time")
                            //.noOuter()
                            .enabledNewSuperClass()
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
                .templateConfig(builder -> {
                    builder.build();
                })
                .injectionConfig(builder -> {
                    builder.customSqlOutputFile(()->{
                        ArrayList<TableColumn> list = new ArrayList<>();
                        TableColumn tableColumn = new TableColumn();
                        tableColumn.setTemplatePath("templates/baseEntity.java.ftl");
                        tableColumn.setOutputPath("demo");
                        list.add(tableColumn);
                        return list;
                    })
                            .build();
                })
                .execute();
    }
}
