package com.wshake.generator.config;

import java.util.List;

import lombok.Data;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/6
 */
@Data
public class Injection {
    private String templatePath;
    private String outputPath;
    private Boolean isOutOne=false;
}
