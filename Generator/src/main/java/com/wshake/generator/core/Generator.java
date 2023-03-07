package com.wshake.generator.core;

import com.wshake.generator.builder.Controller;
import com.wshake.generator.builder.Entity;
import com.wshake.generator.builder.Mapper;
import com.wshake.generator.builder.Service;
import com.wshake.generator.config.ConstVal;
import com.wshake.generator.config.GlobalConfig;
import com.wshake.generator.config.PackageConfig;
import com.wshake.generator.config.StrategyConfig;
import com.wshake.generator.config.Table;
import com.wshake.generator.config.TemplateConfig;
import com.wshake.generator.utils.FileUtils;
import com.wshake.generator.utils.PropertiesUtils;
import com.wshake.generator.utils.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.cache.FileTemplateLoader;
import freemarker.core.XMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import javafx.scene.control.Tab;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/5
 * 代码生成器核心处理类
 *  使用Freemarker完成文件生成
 *      数据模型+模板
 * 数据:
 *  数据模型
 *  模板的位置
 *  生成文件的路径
 */
public class Generator {
    private static Generator generator;

    static {
        try {
            generator = new Generator();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            generator = new Generator();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Generator getGenerator() {
        return generator;
    }

    private String resourcesPath=System.getProperty("user.dir")+"/Generator/src/main/resources";
    private Configuration cfg;
    private String defaultTempPath = TemplateConfig.getTemplateConfig().getDefaultTempPath();

    private PackageConfig packageConfig=PackageConfig.getPackageConfig();
    private String outputDir= GlobalConfig.getGlobalConfig().getOutputDir() ;
    public Generator() throws IOException {
        cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setDefaultEncoding(ConstVal.UTF8);
        cfg.setOutputFormat(XMLOutputFormat.INSTANCE);
        cfg.setClassForTemplateLoading(this.getClass(), "/");
        FileTemplateLoader ftl = new FileTemplateLoader(new File(defaultTempPath));
        cfg.setTemplateLoader(ftl);
    }
    /**
     * 代码生成
     *  1.扫描模板路径下的所有代码
     *  2.对每个模板进行代码生成(数据模型)
     */
    public void scanAndGenerate(Map<String,Object> dataModel) throws IOException, TemplateException {
        //1.根据模板路径找到此路径下的所有模板文件
        List<File> files = FileUtils.searchFiles(new File(defaultTempPath));
        for (File file : files) {
            executeGenertor(dataModel,file,null,null);
        }
    }

    public void filterAndGenerate(Map<String,Object> dataModel) throws IOException, TemplateException {
        StrategyConfig strategyConfig = StrategyConfig.getStrategyConfig();
        Entity entityConfig = strategyConfig.getEntity();
        Mapper mapperConfig = strategyConfig.getMapper();
        Service serviceConfig = strategyConfig.getService();
        Controller controllerConfig = strategyConfig.getController();
        Map<String, Object> packageMap = (Map<String, Object>) dataModel.get("package");
        if(entityConfig.isOuter()){
            TemplateConfig templateConfig = TemplateConfig.getTemplateConfig();
            File file = new File(resourcesPath+templateConfig.getEntity()+".ftl");
            Map<String,Object> map = (Map<String,Object>) dataModel.get("entity");
            if(StringUtils.isBlank(entityConfig.getOutPath())){
                executeGenertor(dataModel,file,outputDir+"/"+packageMap.get(ConstVal.ENTITY),map);
            }else {
                executeGenertor(dataModel,file,entityConfig.getOutPath(),map);
            }
        }
        if(mapperConfig.isOuterMapper()){
            TemplateConfig templateConfig = TemplateConfig.getTemplateConfig();
            File file = new File(resourcesPath+templateConfig.getMapper()+".ftl");
            Map<String,Object> map = (Map<String,Object>) dataModel.get("mapper");
            map.put("fileSuffixName",map.get("fileSuffixNameMapper"));
            map.put("filePrefixName",map.get("filePrefixNameMapper"));
            if(StringUtils.isBlank(mapperConfig.getOutPathMapper())){
                executeGenertor(dataModel,file,outputDir+"/"+packageMap.get(ConstVal.MAPPER),map);
            }else {
                executeGenertor(dataModel,file,mapperConfig.getOutPathMapper(),map);
            }
        }
        if(mapperConfig.isOuterXml()){
            TemplateConfig templateConfig = TemplateConfig.getTemplateConfig();
            File file = new File(resourcesPath+templateConfig.getXml()+".ftl");
            Map<String,Object> map = (Map<String,Object>) dataModel.get("mapper");
            map.put("fileSuffixName",map.get("fileSuffixNameXml"));
            map.put("filePrefixName",map.get("filePrefixNameXml"));
            if(StringUtils.isBlank(mapperConfig.getOutPathMapper())){
                executeGenertor(dataModel,file,outputDir+"/"+packageMap.get(ConstVal.XML),map);
            }else {
                executeGenertor(dataModel,file,mapperConfig.getOutPathMapper(),map);
            }
        }
        if(serviceConfig.getIsOuterService()){
            TemplateConfig templateConfig = TemplateConfig.getTemplateConfig();
            File file = new File(resourcesPath+templateConfig.getService()+".ftl");
            Map<String,Object> map = (Map<String,Object>) dataModel.get("service");
            map.put("fileSuffixName",map.get("fileSuffixNameService"));
            map.put("filePrefixName",map.get("filePrefixNameService"));
            //map.put("serviceNameImpl",map.get("fileSuffixNameService") + ((Table)dataModel.get("table")).getTableUpperName()+ map.get("filePrefixNameService"));
            if(StringUtils.isBlank(serviceConfig.getOutPath())){
                executeGenertor(dataModel,file,outputDir+"/"+packageMap.get(ConstVal.SERVICE),map);
            }else {
                executeGenertor(dataModel,file,serviceConfig.getOutPath(),map);
            }
        }
        if(serviceConfig.getIsOuterServiceImpl()){
            TemplateConfig templateConfig = TemplateConfig.getTemplateConfig();
            File file = new File(resourcesPath+templateConfig.getServiceImpl()+".ftl");
            Map<String,Object> map = (Map<String,Object>) dataModel.get("service");
            map.put("fileSuffixName",map.get("fileSuffixNameServiceImpl"));
            map.put("filePrefixName",map.get("filePrefixNameServiceImpl"));
            if(StringUtils.isBlank(serviceConfig.getOutPathImpl())){
                executeGenertor(dataModel,file,outputDir+"/"+packageMap.get(ConstVal.SERVICE_IMPL),map);
            }else {
                executeGenertor(dataModel,file,serviceConfig.getOutPathImpl(),map);
            }
        }
        if(controllerConfig.isOuter()){
            TemplateConfig templateConfig = TemplateConfig.getTemplateConfig();
            File file = new File(resourcesPath+templateConfig.getController()+".ftl");
            Map<String,Object> map = (Map<String,Object>) dataModel.get("controller");
            if(StringUtils.isBlank(controllerConfig.getOutPath())){
                executeGenertor(dataModel,file,outputDir+"/"+packageMap.get(ConstVal.CONTROLLER),map);
            }else {
                executeGenertor(dataModel,file,controllerConfig.getOutPath(),map);
            }
        }
    }

    /**
     * 对模板进行生成
     * @param dataModel 数据模型
     * @param file  模板文件
     */
    public void executeGenertor(Map<String,Object> dataModel,File file,String outPath,Map map) throws IOException, TemplateException {
        //1.文件路径处理  绝对路径截取获得项目路径
        String filePath = StringUtils.getFileName(file.getAbsolutePath());
        Table table = (Table) dataModel.get("table");
        //填充数据进入文件
        //String outFilePathName = processTemplateString(filePath,dataModel);
        //2.读取文件模板
        Template template = cfg.getTemplate(filePath);
        template.setOutputEncoding("utf-8");
        //3.创建文件
        if(filePath.endsWith("ftl")){
            if(map!=null){
                filePath = map.get("filePrefixName") + table.getTableUpperName()+ map.get("fileSuffixName")+"."+StringUtils.getSuffix(filePath);
            }else {
                filePath = table.getTableUpperName()+".java";
            }
        }
        File mkdirFile;
        if(outPath==null){
            mkdirFile = FileUtils.mkdir(StringUtils.filePathNameUnification(outputDir), filePath);
        }else {
            mkdirFile = FileUtils.mkdir(StringUtils.filePathNameUnification(outPath), filePath);
        }
        //4.模板处理(文件生成)
        FileWriter fw=new FileWriter(mkdirFile);
        template.process(dataModel,fw);
        fw.close();

        //if(file.getName().endsWith("ftl")){
        //    Template template = cfg.getTemplate(file.getName());
        //}
    }

    public String processTemplateString(String templateString,Map<String,Object> dataModel) throws IOException, TemplateException {
        StringWriter out=new StringWriter();

        // ii.通过字符串创建模板 参数一是为这个字符串模板取个名字
        Template template=new Template("ts",new StringReader(templateString),cfg);

        template.process(dataModel,out);
        return out.toString();
    }

    public static void main(String[] args) throws TemplateException, IOException {

    }
}
