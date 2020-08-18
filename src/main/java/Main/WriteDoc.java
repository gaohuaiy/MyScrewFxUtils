package Main;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class WriteDoc {


    /**
     * 文档生成
     */
    public void write(String JdbcUrl,String Username,String pwd,EngineFileType engineFileType,String path,String DBTYPE) {
        //数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(DBTYPE);
        //hikariConfig.setJdbcUrl("jdbc:oracle:thin:@191.168.0.93:1521:GJCALC");
        //hikariConfig.setJdbcUrl("jdbc:oracle:thin:@191.168.0.79:1521/JTDEV");
        hikariConfig.setJdbcUrl(JdbcUrl);
        //hikariConfig.setUsername("XEQ_TRD");
        hikariConfig.setUsername(Username);
        //hikariConfig.setPassword("xpar");
        hikariConfig.setPassword(pwd);
        //设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(hikariConfig);
        //生成配置
        //String fileOutputDir;
        EngineConfig engineConfig = EngineConfig.builder()
                //生成文件路径
                .fileOutputDir(path)
                //打开目录
                .openOutputDir(true)
                //文件类型
                .fileType(engineFileType)
                //生成模板实现
                .produceType(EngineTemplateType.freemarker).build();
                //自定义文件名称
                //("自定义文件名称").build();

//        //忽略表
//        ArrayList<String> ignoreTableName = new ArrayList<String>();
//        ignoreTableName.add("test_user");
//        ignoreTableName.add("test_group");
//        //忽略表前缀
//        ArrayList<String> ignorePrefix = new ArrayList<String>();
//        ignorePrefix.add("test_");
//        //忽略表后缀
//        ArrayList<String> ignoreSuffix = new ArrayList<String>();
//        ignoreSuffix.add("_test");
        // 2、配置想要忽略的表（可选）
//        ProcessConfig processConfig = ProcessConfig.builder()
//                .ignoreTableName(ignoreTableName)
//                .ignoreTablePrefix(ignorePrefix)
//                .ignoreTableSuffix(ignoreSuffix)
//                .build();

        // 3、生成文档配置（包含以下自定义版本号、标题、描述（数据库名 + 描述 = 文件名）等配置连接）
        Configuration config = Configuration.builder()
                .version("1.0.0")
                .title("数据库文档")
                .description("数据库设计文档生成")
                .dataSource(dataSource)
                .engineConfig(engineConfig)
                /*.produceConfig(processConfig).*/.build();

        // 4、执行生成
        new DocumentationExecute(config).execute();

    }
}
