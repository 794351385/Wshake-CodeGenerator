package com.wshake.generator.config;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import lombok.Data;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/4
 */
@Data
public class GlobalConfig{

    private static GlobalConfig globalConfig=new GlobalConfig();
    public static GlobalConfig getGlobalConfig() {
        return globalConfig;
    }
    private GlobalConfig() {
    }

    protected static final Logger LOGGER = LoggerFactory.getLogger(GlobalConfig.class);

    public Map<String, Object> getConfigMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("author", author);
        map.put("commentDate", this.getCommentDate());
        map.put("isSwagger", swagger);
        map.put("isSpringdoc", springdoc);
        map.put("isServiceInterface", isServiceInterface);
        return map;
    }


    /**
     * 生成文件的输出目录【 windows:D://  linux or mac:/tmp 】
     */
    //private String outputDir = System.getProperty("os.name").toLowerCase().contains("windows") ? "D://" : "/tmp";
    private String outputDir = System.getProperty("user.dir") +"/Generator/src/main/java";

    /**
     * 作者
     */
    private String author = "wshake";


    /**
     * 开启 swagger 模式（默认 false 与 springdoc 不可同时使用）
     */
    private Boolean swagger=false;
    /**
     * 开启 springdoc 模式（默认 false 与 swagger 不可同时使用）
     */
    private Boolean springdoc=false;

    /**
     * 时间类型对应策略
     */
    private DateType dateType = DateType.TIME_PACK;

    /**
     * 获取注释日期
     *
     * @since 3.5.0
     */
    private Supplier<String> commentDate = () -> new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    /**
     * 是否生成service 接口（默认 true）
     * 增加此开关的原因：在某些项目实践中，只需要生成service实现类，不需要抽象sevice接口
     * 针对某些项目，生成service接口，开发时反而麻烦，这种情况，可以将该属性设置为false
     */
    private Boolean isServiceInterface = true;


    public Boolean isSwagger() {
        // springdoc 设置优先于 swagger
        return springdoc ? false : swagger;
    }

    public Boolean isSpringdoc() {
        return springdoc;
    }

    @NotNull
    public String getCommentDate() {
        return commentDate.get();
    }

    /**
     * 全局配置构建
     *
     * @author nieqiurong 2020/10/11.
     * @since 3.5.0
     */
    public static class Builder implements IConfigBuilder<GlobalConfig> {

        private final GlobalConfig globalConfig;

        public Builder() {
            this.globalConfig = GlobalConfig.getGlobalConfig();
        }


        /**
         * 输出目录
         */
        public Builder outputDir(@NotNull String outputDir) {
            this.globalConfig.outputDir = outputDir;
            return this;
        }

        /**
         * 作者
         */
        public Builder author(@NotNull String author) {
            this.globalConfig.author = author;
            return this;
        }

        /**
         * 开启 swagger 模式
         */
        public Builder enableSwagger() {
            this.globalConfig.swagger = true;
            return this;
        }

        /**
         * 开启 springdoc 模式
         */
        public Builder enableSpringdoc() {
            this.globalConfig.springdoc = true;
            return this;
        }

        /**
         * 不生成service接口
         * @return
         */
        public Builder disableServiceInterface() {
            this.globalConfig.isServiceInterface = false;
            return this;
        }

        /**
         * 时间类型对应策略
         */
        public Builder dateType(@NotNull DateType dateType) {
            this.globalConfig.dateType = dateType;
            return this;
        }

        /**
         * 注释日期获取处理
         * example: () -> LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)
         *
         * @param commentDate 获取注释日期
         * @return this
         * @since 3.5.0
         */
        public Builder commentDate(@NotNull Supplier<String> commentDate) {
            this.globalConfig.commentDate = commentDate;
            return this;
        }

        /**
         * 指定注释日期格式化
         *
         * @param pattern 格式
         * @return this
         * @since 3.5.0
         */
        public Builder commentDate(@NotNull String pattern) {
            return commentDate(() -> new SimpleDateFormat(pattern).format(new Date()));
        }

        @Override
        public GlobalConfig build() {
            return this.globalConfig;
        }
    }
}
