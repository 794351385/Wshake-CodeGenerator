package generator;

import com.wshake.generator.config.ConstVal;
import com.wshake.generator.utils.PropertiesUtils;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/3
 * 第一个FreeMarker程序
 *      1.第一个步骤
 */
public class FreeMarkerTest01 {
    public static void main(String[] args) {
        String property=System.getProperty("user.dir");
    }
    private Configuration cfg;

    @Test
    public void test01() throws IOException, TemplateException {
        //1.创建FreeMarker的配置类
        this.cfg = new Configuration(Configuration.VERSION_2_3_23);
        this.cfg.setDefaultEncoding(ConstVal.UTF8);
        this.cfg.setClassForTemplateLoading(this.getClass(), "/");
        this.cfg.setNumberFormat("#");

        //2.指定模板加载器，将模板存入缓存
        //  文件路径加载器
        FileTemplateLoader ftl = new FileTemplateLoader(new File("src/main/resources/templates"));
        this.cfg.setTemplateLoader(ftl);

        //3.获取模板
        Template template = this.cfg.getTemplate("template01.ftl");

        //4.构造数据模型
        HashMap<String, Object> map = new HashMap<>();
        map.put("username","Wshake");
        map.put("flag",1);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i <5; i++) {
            list.add(String.valueOf(i));
        }
        map.put("list",list);
        //5.文件输出
        /**
         * 处理模型
         *  参数一:数据模型
         *  参数二:writer(FileWriter(文件输出)/PrintWriter(控制台输出))
         */
        String property=System.getProperty("user.dir");
        String filePath = PropertiesUtils.class.getResource("/").getPath();
        //File file = new File("src/main/java/com/wshake/generator/test/test01.txt");
        //template.process(map,new FileWriter(file));
        template.process(map,new PrintWriter(System.out));
    }
}
