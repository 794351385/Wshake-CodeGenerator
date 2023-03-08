package com.wshake.generator.config;

import lombok.Data;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/8
 */
@Data
public class TableColumn {
    private String templatePath;
    private String outputPath;
    private String fileSuffixName="";
    private String filePrefixName="";
}
