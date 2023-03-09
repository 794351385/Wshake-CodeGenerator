package com.wshake.generator.core;

import com.wshake.generator.builder.Controller;
import com.wshake.generator.builder.Entity;
import com.wshake.generator.builder.Mapper;
import com.wshake.generator.builder.Service;
import com.wshake.generator.config.ConstVal;
import com.wshake.generator.config.GlobalConfig;
import com.wshake.generator.config.InjectionConfig;
import com.wshake.generator.config.PackageConfig;
import com.wshake.generator.config.TableColumn;
import com.wshake.generator.config.StrategyConfig;
import com.wshake.generator.config.Table;
import com.wshake.generator.config.TemplateConfig;
import com.wshake.generator.utils.FileUtils;
import com.wshake.generator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import freemarker.cache.FileTemplateLoader;
import freemarker.core.XMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

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
    protected static final Logger logger = LoggerFactory.getLogger(Generator.class);
    private static Generator generator;

    static {
        try {
            generator = new Generator();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Generator getGenerator() {
        return generator;
    }

    private String resourcesPath=Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private Configuration cfg;
    private String defaultTempPath = TemplateConfig.getTemplateConfig().getDefaultTempPath();

    private PackageConfig packageConfig=PackageConfig.getPackageConfig();
    private String outputDir= GlobalConfig.getGlobalConfig().getOutputDir() ;
    public Generator() throws IOException {
        cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setDefaultEncoding(ConstVal.UTF8);
        cfg.setOutputFormat(XMLOutputFormat.INSTANCE);
        cfg.setClassForTemplateLoading(this.getClass(), "/");
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

    public void oneGenerate(Map<String,Object> dataModel,String templateDir,String outputDirPath) throws IOException, TemplateException {
        String templatePath=resourcesPath+"\\"+templateDir;
        File file = new File(templatePath);
        String vmName = file.getName();
        String fileName = StringUtils.getFileName(outputDirPath);
        dataModel.put(StringUtils.getRemoveSuffixName(fileName),StringUtils.getPackage(outputDirPath));
        StrategyConfig strategyConfig = StrategyConfig.getStrategyConfig();
        try{
            logger.info("加载模板地址:"+file.getAbsolutePath());
            cfg.setTemplateLoader(new FileTemplateLoader(file.getParentFile()));
            Template template = cfg.getTemplate(vmName);
            String parentPackage=StringUtils.filePathNameUnificationDOT(packageConfig.getPackageModuleName());
            String out=StringUtils.filePathNameUnification(outputDir+"/"+parentPackage+"/"+outputDirPath);
            File fileOut = new File(out);
            if(!strategyConfig.getIsFileOverride()){
                if (fileOut.exists()){
                    logger.warn(fileOut.getName()+" 文件已存在,已跳过");
                    return;
                }
            }
            if (!fileOut.getParentFile().exists()){
                fileOut.getParentFile().mkdirs();
            }
            File outFile = new File(out);
            FileWriter writer = new FileWriter(outFile);
            template.process(dataModel,writer);
            writer.close();
            logger.info(outFile.getName()+" 生成成功!"+" 生成文件地址:"+out);
        }catch (Exception e){
            logger.error("生成代码异常");
        }
    }

    public String isAddSuffix(String str) {
        if(StringUtils.getSuffix(str).equals(".ftl")){
            return str;
        }else {
            return str+".ftl";
        }
    }
    public void filterAndGenerate(Map<String,Object> dataModel) throws IOException, TemplateException {
        StrategyConfig strategyConfig = StrategyConfig.getStrategyConfig();
        Entity entityConfig = strategyConfig.getEntity();
        Mapper mapperConfig = strategyConfig.getMapper();
        Service serviceConfig = strategyConfig.getService();
        Controller controllerConfig = strategyConfig.getController();
        Map<String, Object> packageMap = (Map<String, Object>) dataModel.get("package");
        TemplateConfig templateConfig = TemplateConfig.getTemplateConfig();
        InjectionConfig injectionConfig=InjectionConfig.getInjectionConfig();
        if(injectionConfig.isSqlInjections()){
            List<TableColumn> tableColumns = injectionConfig.getSqlInjections();
            for (TableColumn in: tableColumns) {
                Map<String,Object> map = (Map<String,Object>) dataModel.get("injection");
                map.put("fileSuffixName",in.getFileSuffixName());
                map.put("filePrefixName",in.getFilePrefixName());
                File file = new File(isAddSuffix(resourcesPath+"\\"+in.getTemplatePath()));
                String parentPackage=StringUtils.filePathNameUnificationDOT(packageConfig.getPackageModuleName());
                String out=StringUtils.filePathNameUnification(outputDir+"/"+parentPackage+"/"+in.getOutputPath());
                executeGenertor(dataModel,file,out,map);
            }
        }
        if(entityConfig.isOuter()){
            File file = new File(isAddSuffix(resourcesPath+templateConfig.getEntity()));
            Map<String,Object> map = (Map<String,Object>) dataModel.get("entity");
            executeGenertor(dataModel,file,outputDir+"/"+packageMap.get(ConstVal.ENTITY),map);
        }
        if(mapperConfig.isOuterMapper()){
            File file = new File(isAddSuffix(resourcesPath+templateConfig.getMapper()));
            Map<String,Object> map = (Map<String,Object>) dataModel.get("mapper");
            map.put("fileSuffixName",map.get("fileSuffixNameMapper"));
            map.put("filePrefixName",map.get("filePrefixNameMapper"));
            executeGenertor(dataModel,file,outputDir+"/"+packageMap.get(ConstVal.MAPPER),map);
        }
        if(mapperConfig.isOuterXml()){
            File file = new File(isAddSuffix(resourcesPath+templateConfig.getXml()));
            Map<String,Object> map = (Map<String,Object>) dataModel.get("mapper");
            map.put("fileSuffixName",map.get("fileSuffixNameXml"));
            map.put("filePrefixName",map.get("filePrefixNameXml"));
            executeGenertor(dataModel,file,outputDir+"/"+packageMap.get(ConstVal.XML),map);

        }
        if(serviceConfig.getIsOuterService()){
            File file = new File(isAddSuffix(resourcesPath+templateConfig.getService()));
            Map<String,Object> map = (Map<String,Object>) dataModel.get("service");
            map.put("fileSuffixName",map.get("fileSuffixNameService"));
            map.put("filePrefixName",map.get("filePrefixNameService"));
            executeGenertor(dataModel,file,outputDir+"/"+packageMap.get(ConstVal.SERVICE),map);
        }
        if(serviceConfig.getIsOuterServiceImpl()){
            File file = new File(isAddSuffix(resourcesPath+templateConfig.getServiceImpl()));
            Map<String,Object> map = (Map<String,Object>) dataModel.get("service");
            map.put("fileSuffixName",map.get("fileSuffixNameServiceImpl"));
            map.put("filePrefixName",map.get("filePrefixNameServiceImpl"));
            executeGenertor(dataModel,file,outputDir+"/"+packageMap.get(ConstVal.SERVICE_IMPL),map);
        }
        if(controllerConfig.isOuter()){
            File file = new File(isAddSuffix(resourcesPath+templateConfig.getController()));
            Map<String,Object> map = (Map<String,Object>) dataModel.get("controller");
            executeGenertor(dataModel,file,outputDir+"/"+packageMap.get(ConstVal.CONTROLLER),map);
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
        logger.info("加载模板地址:"+file.getAbsolutePath());
        try {
            cfg.setTemplateLoader(new FileTemplateLoader(file.getParentFile()));
            Template template = cfg.getTemplate(filePath);
            template.setOutputEncoding("utf-8");
            String removeFirstSuffixName = StringUtils.getRemoveFirstSuffixName(filePath);
            String dotSuffix=StringUtils.getSuffix(removeFirstSuffixName);
            //3.创建文件
            if(map!=null){
                filePath = map.get("filePrefixName") + table.getTableUpperName()+ map.get("fileSuffixName")+dotSuffix;
            }else {
                filePath = table.getTableUpperName()+dotSuffix;
            }
            File mkdirFile;
            if(outPath==null){
                mkdirFile = FileUtils.mkdir(StringUtils.filePathNameUnificationDOT(outputDir), filePath);
            }else {
                mkdirFile = FileUtils.mkdir(StringUtils.filePathNameUnificationDOT(outPath), filePath);
            }
            StrategyConfig strategyConfig = StrategyConfig.getStrategyConfig();
            if(!strategyConfig.getIsFileOverride()){
                if (mkdirFile.exists()){
                    logger.warn(mkdirFile.getName()+" 文件已存在,已跳过");
                    return;
                }
            }
            //4.模板处理(文件生成)
            FileWriter fw=new FileWriter(mkdirFile);
            template.process(dataModel,fw);
            fw.close();
            logger.info(mkdirFile.getName()+ " 生成成功!"+" 生成文件地址:"+mkdirFile.getAbsolutePath());
        }catch (Exception e){
            logger.error("生成代码异常");
        }
    }

    public String processTemplateString(String templateString,Map<String,Object> dataModel) throws IOException, TemplateException {
        StringWriter out=new StringWriter();

        // ii.通过字符串创建模板 参数一是为这个字符串模板取个名字
        Template template=new Template("ts",new StringReader(templateString),cfg);

        template.process(dataModel,out);
        return out.toString();
    }

}
