package cn.common.api.code;


import lombok.extern.slf4j.Slf4j;
import pro.skywalking.utils.CheckParam;

/**
 * 项目代码生成器
 * @title: CreateCode.java
 * @author Singer
 * @date 2024-02-29
 */
@Slf4j
public class CurrentCodeHandler {

    /**
     * 数据库配置->驱动类名
     */
    private final static String DIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * 数据库配置
     */
    public final static String DB_NAME = "hotel-plus";

    /**
     * 数据库配置->连接URL
     */
    public final static String URL = "jdbc:mysql://139.155.74.121:6006/"+ DB_NAME +"?useUnicode=true&characterEncoding=utf-8&useSSL=false";

    /**
     * 用户名
     */
    public final static String DB_USER = "hotel-plus";

    /**
     * 密码
     */
    public final static String DB_PASSWORD = "123456";

    /**
     * 模块前缀名
     */
    public final static String MODULE_SUFFIX_NAME = "platform";

    /**
     * 主要的包名 不带域名前缀(.cn和.com等)
     */
    public final static String MAIN_PACKAGE_NAME = "common";

    /**
     * 数据表配置 多张表用逗号隔开
     */
    public final static String TABLE_NAME = "room_check_out";

    /**
     * 表注释
     */
    private final static String TABLE_DESC = "客房退房信息";

    /**
     * VUE前端路径 需要填写绝对路径
     */
    private static String VUE_PATH = "/Users/singer/GitCode/hotel-plus/platform-vue-web/src/views/";


    /**
     * 运行此方法，可以生成对应的数据表的实体
     * @author: Singer
     * @date 2024/02/27
     * @param args 参数
     */
    public static void main(String[] args) {
        try {
            System.setProperty("DB_DIVER",DIVER);
            System.setProperty("DB_URL",URL);
            System.setProperty("DB_USER",DB_USER);
            System.setProperty("DB_PASSWORD",DB_PASSWORD);
            System.setProperty("ORIGINAL_TABLE_NAME",TABLE_NAME);
            //表名称
            String tableName = TABLE_NAME;
            if(CheckParam.isNull(tableName)){
                return;
            }
            //设置是否生成前端和后台代码
            CodeHandler.setCreateVue(true);
            CodeHandler.setCreateJava(true);
            //设置前端项目代码路径
            CodeHandler.setVuePath(VUE_PATH);
            //初始化项目路径
            CodeHandler.setProjectNameAndModuleName(MODULE_SUFFIX_NAME,MAIN_PACKAGE_NAME);
            //拿到数据库表字段信息
            CodeHandlerUtil.readTableField(DB_NAME, tableName,TABLE_DESC);
            //创建文件
            CodeHandler.createTableFile(tableName.toLowerCase(),TABLE_DESC);
            //只创建实体类文件
            //CodeHandler.createJavaTableEntityFile(tableName.toLowerCase(),TABLE_DESC);
        } catch (Exception e) {
            log.info(">>>>>>>>>>>>>>>>>>>>>>生成项目代码失败,异常信息: {} , {} <<<<<<<<<<<<<<<<<<<",e.getMessage(),e);
        }
    }
}
