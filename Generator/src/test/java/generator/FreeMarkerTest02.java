package generator;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/3
 *
 * 测试字符串模板
 */

public class FreeMarkerTest02 {

    @Test
    public void test01() throws Exception {
        //1.创建配置对象
        Configuration cfg = new Configuration(Configuration.getVersion());
        //2.指定加载器
        cfg.setTemplateLoader(new StringTemplateLoader());
        //3.创建字符串模板
        // i.字符串
        String templateString="欢迎您:${username}";
        // ii.通过字符串创建模板 参数一是为这个字符串模板取个名字
        Template template=new Template("name1",new StringReader(templateString),cfg);
        //4.构造数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("username","张三");
        //5.处理模板
        //template.process(map,new PrintWriter(System.out));
        File file = new File("src/main/java/com/wshake/generator/test/test02.txt");
        template.process(map,new FileWriter(file));
    }
}
