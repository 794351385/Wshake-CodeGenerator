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
    public TableColumn(String templatePath, String outputPath, String filePrefixName, String fileSuffixName) {
        this.templatePath = templatePath;
        this.outputPath = outputPath;
        this.fileSuffixName = fileSuffixName;
        this.filePrefixName = filePrefixName;
    }
    public TableColumn(String templatePath, String outputPath) {
        this.templatePath = templatePath;
        this.outputPath = outputPath;
    }

}
