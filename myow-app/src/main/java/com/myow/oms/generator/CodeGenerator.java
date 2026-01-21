package com.myow.oms.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Arrays;
import java.util.Collections;

/**
 * MyBatis Plus 代码生成器
 * <p>
 * 使用前请确认：
 * 1. 确认 application-dev.yml 中的数据库连接、用户名和密码正确。
 * 2. 在 strategyConfig() 的 .addInclude("table_name") 方法中, 替换为你需要生成代码的表名。
 * 3. 生成的代码会输出到 myow-infrastructure 模块下。
 * </p>
 */
public class CodeGenerator {
    private static final String table_names = "sys_dept, sys_menu, sys_oper_log, sys_position, sys_role, sys_role_dept, sys_role_menu, sys_serial_no_config, sys_serial_no_record, sys_tenant, sys_tenant_plans, sys_user, sys_user_post, sys_user_role, t_i18n_key, t_i18n_message"; // <<<--- 在这里修改为您需要生成代码的表名,可以写多个,用逗号隔开
    private static final String module_name = "myow-system";
    private static final String output_dir = System.getProperty("user.dir") + "/" + module_name + "/src/main/java";
    private static final String xml_path = System.getProperty("user.dir") + "/" + module_name + "/src/main/resources/mapper";
    private static final String module_parent_ = "com.myow.system.persistent";

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:postgresql://localhost:5432/myow_oms_dev?stringtype=unspecified&useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai",
                        "myow_admin",
                        "MyowPass2026!")
                .globalConfig(builder -> {
                    builder.author("yss").outputDir(output_dir).disableOpenDir();
                })
                .packageConfig(builder -> {
                    builder.parent(module_parent_)
                            .entity("po")
                            .serviceImpl("repository")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, xml_path)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(Arrays.stream(table_names.split(","))
                                    .map(String::trim)
                                    .toArray(String[]::new))
                            .addTablePrefix("t_", "sys_") // 去掉表名前缀
                            .entityBuilder()
                            .enableLombok() // 开启 Lombok
                            .enableChainModel() // 开启链式模型
                            .disableSerialVersionUID() // 禁用生成 SerialVersionUID
                            .addTableFills(new Column("created_at", FieldFill.INSERT)) // 创建时间自动填充
                            .addTableFills(new Column("updated_at", FieldFill.INSERT_UPDATE)) // 更新时间自动填充
                            .versionColumnName("version") // 乐观锁字段名
                            .logicDeleteColumnName("deleted") // 逻辑删除字段名
                            .formatFileName("%sDO") // 实体类名后缀
                            .mapperBuilder()
                            .formatXmlFileName("%sMapper") // XML 文件名
                            .serviceBuilder()
                            .formatServiceFileName("%sService") // Service 接口名
                            .formatServiceImplFileName("%sRepository")
                            .controllerBuilder()
                            .formatFileName("");

                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
        System.out.println("生成成功！");
    }
}
