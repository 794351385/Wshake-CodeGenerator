package demo;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/6
 */
public class test01 {
    @Test
    public void test(){
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("11","22");
        hashMap.put("22","33");
        hashMap.forEach((k,v)->{
            System.out.println(k+"="+v);
        });
        Map<String,String> map=hashMap;
        hashMap.put("22","44");
        System.out.println();
        map.forEach((k,v)->{
            System.out.println(k+"="+v);
        });
    }
}
