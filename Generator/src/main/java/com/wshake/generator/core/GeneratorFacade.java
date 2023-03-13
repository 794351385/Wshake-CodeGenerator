package com.wshake.generator.core;

import com.wshake.generator.builder.Entity;
import com.wshake.generator.config.DataBase;
import com.wshake.generator.config.DataSourceConfig;
import com.wshake.generator.config.GlobalConfig;
import com.wshake.generator.config.InjectionConfig;
import com.wshake.generator.config.PackageConfig;
import com.wshake.generator.config.StrategyConfig;
import com.wshake.generator.config.TableColumn;
import com.wshake.generator.config.TemplateConfig;
import com.wshake.generator.utils.DataBaseUtils;
import com.wshake.generator.utils.PropertiesUtils;
import com.wshake.generator.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import freemarker.template.TemplateException;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/5
 * 1.采集配置信息
 *  模板位置
 *  代码生成路径
 *  数据库对象
 * 2.准备数据模型
 *  自定义配置
 *  元数据
 *  各种Config
 * 3.调用核心处理类完成代码生成工作
 */
public class GeneratorFacade {
    private Map<String,Object> dataModel = new HashMap<>();

    public GeneratorFacade dataModel(Map<String,Object> dataModel) {
        this.dataModel.putAll(dataModel);
        return this;
    }
    public GeneratorFacade dataModel(String key,Object value) {
        this.dataModel.put(key, value);
        return this;
    }

    protected static final Logger logger = LoggerFactory.getLogger(GeneratorFacade.class);

    /**
     * 数据源配置 Builder
     */
    private final DataSourceConfig.Builder dataSourceConfigBuilder;

    /**
     * 全局配置 Builder
     */
    private final GlobalConfig.Builder globalConfigBuilder;

    /**
     * 包配置 Builder
     */
    private final PackageConfig.Builder packageConfigBuilder;

    /**
     * 策略配置 Builder
     */
    private final StrategyConfig.Builder strategyConfigBuilder;

    /**
     * 模板配置 Builder
     */
    private final TemplateConfig.Builder templateConfigBuilder;
    /**
     * 注入配置 Builder
     */
    private final InjectionConfig.Builder injectionConfigBuilder;


    private GeneratorFacade(DataSourceConfig.Builder dataSourceConfigBuilder) {
        this.dataSourceConfigBuilder = dataSourceConfigBuilder;
        this.globalConfigBuilder = new GlobalConfig.Builder();
        this.packageConfigBuilder = new PackageConfig.Builder();
        this.strategyConfigBuilder = new StrategyConfig.Builder();
        this.templateConfigBuilder = new TemplateConfig.Builder();
        this.injectionConfigBuilder=new InjectionConfig.Builder();
    }
    public static GeneratorFacade create(@NotNull String url, String username, String password) {
        return new GeneratorFacade(new DataSourceConfig.Builder(url, username, password));
    }
    public static GeneratorFacade create(@NotNull DataSourceConfig.Builder dataSourceConfigBuilder) {
        return new GeneratorFacade(dataSourceConfigBuilder);
    }

    /**
     * 读取控制台输入内容
     */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * 控制台输入内容读取并打印提示信息
     *
     * @param message 提示信息
     * @return String
     */
    public String scannerNext(String message) {
        System.out.println(message);
        String nextLine = scanner.nextLine();
        if (StringUtils.isBlank(nextLine)) {
            // 如果输入空行继续等待
            return scanner.next();
        }
        return nextLine;
    }

    /**
     * 全局配置
     *
     * @param consumer 自定义全局配置
     * @return FastAutoGenerator
     */
    public GeneratorFacade dataSourceConfig(Consumer<DataSourceConfig.Builder> consumer) {
        consumer.accept(this.dataSourceConfigBuilder);
        return this;
    }

    public GeneratorFacade dataSourceConfig(BiConsumer<Function<String, String>, DataSourceConfig.Builder> biConsumer) {
        biConsumer.accept(this::scannerNext, this.dataSourceConfigBuilder);
        return this;
    }

    /**
     * 全局配置
     *
     * @param consumer 自定义全局配置
     * @return FastAutoGenerator
     */
    public GeneratorFacade globalConfig(Consumer<GlobalConfig.Builder> consumer) {
        consumer.accept(this.globalConfigBuilder);
        return this;
    }

    public GeneratorFacade globalConfig(BiConsumer<Function<String, String>, GlobalConfig.Builder> biConsumer) {
        biConsumer.accept(this::scannerNext, this.globalConfigBuilder);
        return this;
    }

    /**
     * 包配置
     *
     * @param consumer 自定义包配置
     * @return FastAutoGenerator
     */
    public GeneratorFacade packageConfig(Consumer<PackageConfig.Builder> consumer) {
        consumer.accept(this.packageConfigBuilder);
        return this;
    }

    public GeneratorFacade packageConfig(BiConsumer<Function<String, String>, PackageConfig.Builder> biConsumer) {
        biConsumer.accept(this::scannerNext, this.packageConfigBuilder);
        return this;
    }

    /**
     * 策略配置
     *
     * @param consumer 自定义策略配置
     * @return FastAutoGenerator
     */
    public GeneratorFacade strategyConfig(Consumer<StrategyConfig.Builder> consumer) {
        consumer.accept(this.strategyConfigBuilder);
        return this;
    }

    public GeneratorFacade strategyConfig(BiConsumer<Function<String, String>, StrategyConfig.Builder> biConsumer) {
        biConsumer.accept(this::scannerNext, this.strategyConfigBuilder);
        return this;
    }

    /**
     * 模板配置
     *
     * @param consumer 自定义模板配置
     * @return FastAutoGenerator
     */
    public GeneratorFacade templateConfig(Consumer<TemplateConfig.Builder> consumer) {
        consumer.accept(this.templateConfigBuilder);
        return this;
    }

    public GeneratorFacade templateConfig(BiConsumer<Function<String, String>, TemplateConfig.Builder> biConsumer) {
        biConsumer.accept(this::scannerNext, this.templateConfigBuilder);
        return this;
    }

    /**
     * 注入配置
     *
     * @param consumer 自定义注入配置
     * @return FastAutoGenerator
     */
    public GeneratorFacade injectionConfig(Consumer<InjectionConfig.Builder> consumer) {
        consumer.accept(this.injectionConfigBuilder);
        return this;
    }

    public GeneratorFacade injectionConfig(BiConsumer<Function<String, String>, InjectionConfig.Builder> biConsumer) {
        biConsumer.accept(this::scannerNext, this.injectionConfigBuilder);
        return this;
    }



    /**
     * 1.准备数据模型
     * 2.调用核心处理类完成代码生成工作
     */
    public void execute() throws SQLException, TemplateException, IOException {
        List<DataBase> dataBaseList = DataBaseUtils.getDataBase();
        HashMap<String, Object> typeMap = new HashMap<>();
        typeMap.putAll(PropertiesUtils.customMap);
        //自定义配置
        dataModel.put("javaType",typeMap);
        dataModel.put("package",PackageConfig.getPackageConfig().getPackageInfo());
        dataModel.put("global",GlobalConfig.getGlobalConfig().getConfigMap());
        dataModel.put("injection",InjectionConfig.getInjectionConfig().renderData());
        dataModel.put("entity",StrategyConfig.getStrategyConfig().entity().renderData());
        dataModel.put("mapper",StrategyConfig.getStrategyConfig().mapper().renderData());
        dataModel.put("service",StrategyConfig.getStrategyConfig().service().renderData());
        dataModel.put("controller",StrategyConfig.getStrategyConfig().controller().renderData());
        for (DataBase dataBase:dataBaseList){
            dataModel.put("dataBase",dataBase);
            //对每个DataBase进行代码生成
            tablesGenerated(dataBase);
            //for (Map.Entry<String,Object> entry:dataModel.entrySet()){
            //    String key=entry.getKey();
            //    Object value=entry.getValue();
            //    System.out.println(key + ": "+value);
            //}
        }
        TemplateConfig templateConfig = TemplateConfig.getTemplateConfig();
        String mapper = templateConfig.getMapper();
        DataSourceConfig.getDataSourceConfig().closeAll();
        logger.info("生成结束!");
    }
    /**
     * 根据DataBase对象获取数据模型
     */

    public Map<String,Object> getCustomMap(){
        return dataModel;
    }
    public void tablesGenerated(DataBase dataBase) throws TemplateException, IOException {
        logger.info("数据库 "+dataBase.getSqlName()+" 开始生成。");
        Entity entityConfig = StrategyConfig.getStrategyConfig().getEntity();
        InjectionConfig injectionConfig=InjectionConfig.getInjectionConfig();
        Generator generator = Generator.getGenerator();
        if(entityConfig.getIsNewSuperClass()){
            generator.oneGenerate(dataModel,entityConfig.getNewSuperClassTemplate(),entityConfig.getNewSuperClasOutPath());
        }
        for (int i = 0; i < dataBase.getTables().size(); i++) {
            dataModel.put("table",dataBase.getTables().get(i));
            /**
             * 准备数据模型
             * 调用Generator核心处理
             */
            generator.filterAndGenerate(dataModel);
        }
        if(injectionConfig.isOneInjections()){
            List<TableColumn> injections = injectionConfig.getOneInjections();
            for (TableColumn in:injections) {
                generator.oneGenerate(dataModel,in.getTemplatePath(),in.getOutputPath());
            }
        }
        logger.info("数据库 "+dataBase.getSqlName()+" 生成完成!");
        if(Generator.isException()){
            logger.error("表 "+Generator.getExceptionTable()+"生成异常！请查看");
        }
    }
}
