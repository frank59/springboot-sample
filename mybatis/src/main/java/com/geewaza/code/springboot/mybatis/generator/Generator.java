package com.geewaza.code.springboot.mybatis.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @author : wangheng
 * @date : 2020-10-09 11:10
 **/
public class Generator {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_test";
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_USER = "root";
    private static final String DB_PWD = "123456";

    private static final String BASE_PATH = System.getProperty("user.dir") + "/mybatis";
    private static final String CODE_DIR = BASE_PATH + "/src/main/java";
    private static final String XML_DIR = BASE_PATH + "/src/main/resources/mapper/";
    private static final String BASE_PACKAGE = "com.geewaza.code.springboot.mybatis.dao.auto";
    private static final String CONF_AUTHOR = "wangheng";


    public static void main(String[] args) throws Exception {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        File configFile = new File(BASE_PATH +  "/src/main/resources/generator/generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }
}
