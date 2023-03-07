package com.wshake.generator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/4
 */
public class PropertiesUtils {
    public static Map<String,String> customMap=new HashMap<>();

    static {
        String path=System.getProperty("user.dir");
        path=path+"\\Generator\\src\\main\\resources";
        List<File> files = FileUtils.searchFiles(new File(path));
        files.forEach(file->{
            if(file.getName().endsWith(".properties")){
                try {
                    Properties props = new Properties();
                    props.load(new FileInputStream(file));
                    customMap.putAll((Map)props);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void main(String[] args) {
        PropertiesUtils.customMap.forEach((k,v)->{
            System.out.println(k+"="+v);
        });
    }
}
