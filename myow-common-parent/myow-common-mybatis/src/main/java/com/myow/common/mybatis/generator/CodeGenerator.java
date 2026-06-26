package com.myow.common.mybatis.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Arrays;
import java.util.Collections;

/**
 * MyBatis Plus code generator for development use.
 */
public class CodeGenerator {

    private static final String TABLE_NAMES = "sys_dict, sys_dict_data";
    private static final String MODULE_NAME = "myow-user";
    private static final String OUTPUT_DIR = System.getProperty("user.dir") + "/" + MODULE_NAME + "/src/main/java";
    private static final String XML_PATH = System.getProperty("user.dir") + "/" + MODULE_NAME + "/src/main/resources/mapper";
    private static final String MODULE_PARENT = "com.myow.user.system.infrastructure.persistence";

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:postgresql://localhost:5432/myow_oms_dev?stringtype=unspecified&useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai",
                        "myow_admin",
                        "MyowPass2026!")
                .globalConfig(builder -> builder.author("yss").outputDir(OUTPUT_DIR).disableOpenDir())
                .packageConfig(builder -> builder.parent(MODULE_PARENT)
                        .entity("po")
                        .serviceImpl("repository")
                        .pathInfo(Collections.singletonMap(OutputFile.xml, XML_PATH)))
                .strategyConfig(builder -> builder.addInclude(Arrays.stream(TABLE_NAMES.split(","))
                                .map(String::trim)
                                .toArray(String[]::new))
                        .addTablePrefix("t_", "sys_")
                        .entityBuilder()
                        .enableLombok()
                        .enableChainModel()
                        .disableSerialVersionUID()
                        .addTableFills(new Column("created_at", FieldFill.INSERT))
                        .addTableFills(new Column("updated_at", FieldFill.INSERT_UPDATE))
                        .versionColumnName("version")
                        .logicDeleteColumnName("deleted")
                        .formatFileName("%sDO")
                        .mapperBuilder()
                        .formatXmlFileName("%sMapper")
                        .serviceBuilder()
                        .formatServiceFileName("%sRepository")
                        .formatServiceImplFileName("%sRepository")
                        .controllerBuilder()
                        .formatFileName(""))
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
        System.out.println("Code generation completed.");
    }
}
