package com.geewaza.code.springboot.mybatisplus.dao.auto.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.*;

/**
 * <p></p>
 *
 * @author : wangheng
 * @date : 2021-03-02 11:01
 **/
public class MybatisPlusGeneratorMain {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_test";
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_USER = "root";
    private static final String DB_PWD = "123456";

    private static final String BASE_PATH = System.getProperty("user.dir") + "/mybatisplus";
    private static final String CODE_DIR = BASE_PATH + "/src/main/java";
    private static final String XML_DIR = BASE_PATH + "/src/main/resources/mapper/";
    private static final String BASE_PACKAGE = "com.geewaza.code.springboot.mybatisplus.dao.auto";
    private static final String CONF_AUTHOR = "wangheng";

    private static String[] TABLES= {"t_user"};

    private static String[] PREFIX= {"t_"};


    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator gen = new AutoGenerator();

        /**
         * 全局配置
         */
        System.out.println(CODE_DIR);
        gen.setGlobalConfig(new GlobalConfig()
                .setOutputDir( CODE_DIR)//输出目录
                .setFileOverride(true)// 是否覆盖文件
                .setActiveRecord(true)// 开启 activeRecord 模式
                .setEnableCache(false)// XML 二级缓存
                .setBaseResultMap(true)// XML ResultMap
                .setBaseColumnList(true)// XML columList
                .setOpen(false)//生成后打开文件夹
                .setAuthor(CONF_AUTHOR)
                // 自定义文件命名，注意 %s 会自动填充表实体属性！
                .setMapperName("%sMapper")
                .setXmlName("%sMapper")
                .setServiceName("%sService")
                .setServiceImplName("%sServiceImpl")
                .setControllerName("%sController")
                .setEntityName("%sDO")
                // id自动生成策略
//                .setIdType(IdType.ASSIGN_ID)
        );

        /**
         * 数据库配置
         */
        gen.setDataSource(new DataSourceConfig()
                .setDbType(DbType.MYSQL)
                .setDriverName(DB_DRIVER)
                .setUrl(DB_URL)
                .setUsername(DB_USER)
                .setPassword(DB_PWD)
                .setTypeConvert(new MySqlTypeConvert() {
                    @Override
                    public IColumnType processTypeConvert(GlobalConfig globalConfig, TableField tableField) {
                         if ( tableField.getType().toLowerCase().contains( "timestamp" ) ) {
                             System.out.println("转换类型：" + tableField + " to " + DbColumnType.DATE);

                             return DbColumnType.DATE;
                         }

                        // if ( fieldType.toLowerCase().contains( "tinyint" ) ) {
                        //    return DbColumnType.BOOLEAN;
                        // }
                        return super.processTypeConvert(gen.getGlobalConfig(), tableField);
                    }
                }));

        /**
         * 策略配置
         */
        gen.setStrategy(new StrategyConfig()
                        // .setCapitalMode(true)// 全局大写命名
                        //.setDbColumnUnderline(true)//全局下划线命名
                        // 此处可以修改为您的表前缀
                        .setTablePrefix(PREFIX)
                        // 表名生成策略
                        .setNaming(NamingStrategy.underline_to_camel)
                        // 需要生成的表
                        .setInclude(TABLES)
                        // 逻辑删除字段
                        .setLogicDeleteFieldName("status")
//                        .setRestControllerStyle(true)
                        //.setExclude(new String[]{"test"}) // 排除生成的表
                        // 自定义实体父类
//                        .setSuperEntityClass("com.zdww.cd.amc.esb.dao.auto.entity.BaseEntity")
                        // 自定义实体，公共字段
//                        .setSuperEntityColumns(new String[]{"create_time", "update_time", "status"})
                        //.setTableFillList(tableFillList)
                        // 自定义 mapper 父类 默认BaseMapper
                        //.setSuperMapperClass("com.baomidou.mybatisplus.mapper.BaseMapper")
                        // 自定义 service 父类 默认IService
                        // .setSuperServiceClass("com.baomidou.demo.TestService")
                        // 自定义 service 实现类父类 默认ServiceImpl
                        // .setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl")
                        // 自定义 controller 父类
                        //.setSuperControllerClass("com.kichun."+packageName+".controller.AbstractController")
                        // 【实体】是否生成字段常量（默认 false）
                        // public static final String ID = "test_id";
                        // .setEntityColumnConstant(true)
                        // 【实体】是否为构建者模型（默认 false）
                        // public User setName(String name) {this.name = name; return this;}
                        // .setEntityBuilderModel(true)
                        // 【实体】是否为lombok模型（默认 false）<a href="https://projectlombok.org/">document</a>
                        .setEntityLombokModel(true)
                // Boolean类型字段是否移除is前缀处理
                // .setEntityBooleanColumnRemoveIsPrefix(true)
                // .setRestControllerStyle(true)
                // .setControllerMappingHyphenStyle(true)
        );


        /**
         * 包配置
         */
        gen.setPackageInfo(new PackageConfig()
                //.setModuleName("User")
                // 自定义包路径
                .setParent(BASE_PACKAGE)
                // 这里是控制器包名，默认 web 不创建包
//                .setController("controller")
                .setEntity("entity")
                .setMapper("dao")
                // 不创建包
//                .setService("service")
                // 不创建包
//                .setServiceImpl("service.impl")
                .setXml("mapper")
        );
        /**
         * 注入自定义配置
         */
        // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
        InjectionConfig abc = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        }.setFileCreate(
                (configBuilder, fileType, filePath) ->
                        // 不生成 controller
                        FileType.CONTROLLER != fileType &&
                                // 不生成 service
                                FileType.SERVICE!=fileType &&
                                // 不生成 service_impl
                                FileType.SERVICE_IMPL!=fileType

        );
        //自定义文件输出位置（非必须）
        List<FileOutConfig> fileOutList = new ArrayList<>();
        fileOutList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return XML_DIR + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        abc.setFileOutConfigList(fileOutList);
        gen.setCfg(abc);

        /**
         * 指定模板引擎 默认是VelocityTemplateEngine ，需要引入相关引擎依赖
         */
        gen.setTemplateEngine(new FreemarkerTemplateEngine());

        /**
         * 模板配置
         */
        gen.setTemplate(
                // 关闭默认 xml 生成，调整生成 至 根目录
                new TemplateConfig().setXml(null)
                // 自定义模板配置，模板可以参考源码 /mybatis-plus/src/main/resources/template 使用 copy
                // 至您项目 src/main/resources/template 目录下，模板名称也可自定义如下配置：
//                 .setController("")
                // .setEntity("...");
                // .setMapper("...");
                // .setXml("...");
//                 .setService("")
//                 .setServiceImpl("")
        );

        // 执行生成
        gen.execute();

    }

}
