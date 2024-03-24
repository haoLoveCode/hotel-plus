package cn.common.api.code;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import pro.skywalking.collection.CollectionUtils;
import pro.skywalking.utils.CheckParam;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * 生成实体,Repository文件
 * @title: CreateCode.java
 * @author Singer
 * @date 2024-02-29
 */
@Slf4j
public class CodeHandler {

    /**
     * 删除标志字段
     */
    private final static String DELETE_STATUS = "delete_status";

    /**
     * 创建时间
     */
    private final static String CREATE_TIME = "create_time";

    /**
     * 删除时间
     */
    private final static String DELETE_TIME = "delete_time";

    /**
     * 更新时间
     */
    private final static String UPDATE_TIME = "update_time";

    /**
     * 当前项目所在的绝对路径
     */
    private final static String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * 主要的包名
     */
    public static String MAIN_PACKAGE_NAME;

    /**
     * 基础的包路径名
     */
    public static String BASE_PACKAGE_PATH;

    /**
     * 基础的包名
     */
    public static String BASE_PACKAGE_NAME;

    /**
     * 前端控制器
     */
    private static String API_WEB_PATH = "";

    /**
     * 域名后缀
     */
    public static String DOMAIN_SUFFIX = "cn.";

    /**
     * 实体类的包名
     */
    private static String ENTITY_PACKAGE = "";

    /**
     * 基础实体类的扩展文件路径
     */
    private static String ENTITY_PATH = "";

    /**
     * 交互实体请求类扩展文件路径
     */
    private static String INTERACTION_REQ_PATH = "";

    /**
     * 交互实体返回类扩展文件路径
     */
    private static String INTERACTION_RESP_PATH = "";

    /**
     * repository层路径
     */
    private static String REPOSITORY_PATH = "";

    /**
     * repository层包名
     */
    private static String REPOSITORY_PACKAGE = "";

    /**
     * 服务层路径
     */
    private static String SERVICE_PATH = "";

    /**
     * 服务层包名
     */
    private static String SERVICE_PACKAGE = "";

    /**
     * 服务实现层路径
     */
    private static String SERVICE_IMPL_PATH = "";

    /**
     * 接口层包名
     */
    private static String API_WEB_PACKAGE = "";

    /**
     * 服务实现层包名
     */
    private static String SERVICE_IMPL_PACKAGE = "";

    /**
     * VUE前端路径
     */
    private static String VUE_PATH = "";

    /**
     * 是否创建Java文件
     */
    private static Boolean CREATE_JAVA = true;

    /**
     * 是否创建VUE文件
     */
    private static Boolean CREATE_VUE = true;

    /**
     * 需要被替换的表名前缀
     */
    private static String NEED_REPLACE_TABLE_PREFIX = "t_";

    /**
     * 是否需要删除之前生成的文件
     */
    private static Boolean NEED_DELETE = false;


    /**
     * 设置是否需要删除之前生成的文件
     * @author: Singer
     * @date 2024-02-294
     * @param needDelete 是否生成后端项目代码
     */
    public static void setNeedDelete(Boolean needDelete){
        //前端VUE代码路径
        CodeHandler.NEED_DELETE = needDelete;
    }

    /**
     * 设置是否生成后端项目代码
     * @author: Singer
     * @date 2024-02-294
     * @param createJava 是否生成后端项目代码
     */
    public static void setCreateJava(Boolean createJava){
        //前端VUE代码路径
        CodeHandler.CREATE_JAVA = createJava;
    }

    /**
     * 设置是否生成前端项目代码
     * @author: Singer
     * @date 2024-02-294
     * @param createVue 是否生成前端项目代码
     */
    public static void setCreateVue(Boolean createVue){
        //前端VUE代码路径
        CodeHandler.CREATE_VUE = createVue;
    }

    /**
      * 设置前端项目代码路径
      * @author: Singer
      * @date 2024-02-294
      * @param vuePath vue项目代码路径
      */
    public static void setVuePath(String vuePath){
        //前端VUE代码路径
        CodeHandler.VUE_PATH = vuePath;
    }


    /**
     * 设置模块名称和项目名称信息
     * @author: Singer
     * @date 2024-02-29
     * @param moduleName 模块名称
     * @param mainPackageName 主要包名
     * @return
     */
    public static void setProjectNameAndModuleName(String moduleName,
                                                   String mainPackageName) {
        //主要包名
        CodeHandler.MAIN_PACKAGE_NAME = mainPackageName;

        //基础的包路径名
        CodeHandler.BASE_PACKAGE_PATH = StrUtil.replaceIgnoreCase(DOMAIN_SUFFIX+mainPackageName,".","/");
        //基础的包名
        CodeHandler.BASE_PACKAGE_NAME = DOMAIN_SUFFIX+mainPackageName;

        //基础实体类的扩展文件路径
        CodeHandler.ENTITY_PATH = PROJECT_PATH + "/" +  moduleName + "-repository/src/main/java/"+ CodeHandler.BASE_PACKAGE_PATH +"/repository/entity/biz";
        //repository层
        CodeHandler.REPOSITORY_PATH = PROJECT_PATH + "/" +  moduleName + "-repository/src/main/java/"+ CodeHandler.BASE_PACKAGE_PATH +"/repository/repository/biz";
        //请求类封装
        CodeHandler.INTERACTION_REQ_PATH = PROJECT_PATH + "/" +  moduleName + "-interaction/src/main/java/"+ CodeHandler.BASE_PACKAGE_PATH +"/req/biz";
        //返回类封装
        CodeHandler.INTERACTION_RESP_PATH = PROJECT_PATH + "/" +  moduleName + "-interaction/src/main/java/"+ CodeHandler.BASE_PACKAGE_PATH +"/resp/biz";

        //controller层
        CodeHandler.API_WEB_PATH = PROJECT_PATH + "/" + "web-"+ moduleName + "-api/src/main/java/"+ CodeHandler.BASE_PACKAGE_PATH +"/api/biz";

        //service层
        CodeHandler.SERVICE_PATH = PROJECT_PATH + "/" +  moduleName + "-service/src/main/java/"+ CodeHandler.BASE_PACKAGE_PATH +"/service/biz/platform";
        //serviceImpl层
        CodeHandler.SERVICE_IMPL_PATH = PROJECT_PATH + "/" + moduleName + "-service/src/main/java/"+ CodeHandler.BASE_PACKAGE_PATH +"/service/impl/biz/platform";

        //实体包名
        CodeHandler.ENTITY_PACKAGE = DOMAIN_SUFFIX + MAIN_PACKAGE_NAME +".repository.entity.biz";
        //repository包名
        CodeHandler.REPOSITORY_PACKAGE = DOMAIN_SUFFIX + MAIN_PACKAGE_NAME +".repository.repository.biz";

        //service包名
        CodeHandler.SERVICE_PACKAGE = DOMAIN_SUFFIX + MAIN_PACKAGE_NAME +".service.biz.platform";
        //serviceImpl包名
        CodeHandler.SERVICE_IMPL_PACKAGE = DOMAIN_SUFFIX + MAIN_PACKAGE_NAME +".service.impl.biz.platform";
    }

    /**
     * 创建表的对应的文件
     * @author: Singer
     * @date 2024-02-29
     * @param tableNames 表名(逗号分割)
     * @param tableDesc 实体类名称
     */
    public static void createTableFile(String tableNames,String tableDesc) throws Exception {
        String [] tableArray = tableNames.split(",");
        Stream.of(tableArray).forEach(item -> {
            try{
                item = item.toLowerCase().trim();
                if(CREATE_JAVA){
                    //创建实体类
                    createJavaCodeByTemplate(item,ENTITY_PATH,"Entity.ftl","");
                    //创建新增请求类
                    createJavaCodeByTemplate(item,INTERACTION_REQ_PATH,"AddItem.ftl","AddReq");
                    //创建导出封装类
                    createJavaCodeByTemplate(item,INTERACTION_RESP_PATH,"ExportItem.ftl","ExportResp");
                    //创建导入封装类
                    //createJavaCodeByTemplate(item,INTERACTION_REQ_PATH,"ImportItem.ftl","ImportReq");
                    //创建更新请求类
                    createJavaCodeByTemplate(item,INTERACTION_REQ_PATH,"UpdateItem.ftl","UpdateReq");
                    //创建返回封装类
                    createJavaCodeByTemplate(item,INTERACTION_RESP_PATH,"RespItem.ftl","Resp");
                    //创建分页查询请求类
                    createJavaCodeByTemplate(item,INTERACTION_REQ_PATH,"PageQueryItem.ftl","Req");
                    //创建Repository类
                    createJavaCodeByTemplate(item,REPOSITORY_PATH,"ItemRepository.ftl","Repository");
                    //服务方法实现类
                    createJavaCodeByTemplate(item,SERVICE_IMPL_PATH,"ServiceImpl.ftl","ServiceImpl");
                    //服务方法类
                    createJavaCodeByTemplate(item,SERVICE_PATH,"Service.ftl","Service");
                    //前端控制器
                    createJavaCodeByTemplate(item,API_WEB_PATH,"Controller.ftl","Controller");
                }
                if(CREATE_VUE){
                    //实体类的驼峰命名
                    String camelCaseEntityName = CodeHandlerUtil.camelCaseName(item);
                    //String vueDirName = StrUtil.replace(StrUtil.replace(item,NEED_REPLACE_TABLE_PREFIX,""),"_","-");
                    String vueDirName = camelCaseEntityName;
                    createVUECodeByTemplate(item,VUE_PATH+vueDirName,"ItemAddVUE.ftl","ItemAdd");
                    createVUECodeByTemplate(item,VUE_PATH+vueDirName,"ItemCopyVUE.ftl","ItemCopy");
                    createVUECodeByTemplate(item,VUE_PATH+vueDirName,"ItemEditVUE.ftl","ItemEdit");
                    //createVUECodeByTemplate(item,VUE_PATH+vueDirName,"ItemFactorVUE.ftl","ItemFactor");
                    createVUECodeByTemplate(item,VUE_PATH+vueDirName,"ItemListVUE.ftl","ItemList");
                    createVUECodeByTemplate(item,VUE_PATH+vueDirName,"ItemParticularsVUE.ftl","ItemParticulars");
                    String vueLocation = "\n"+vueDirName+"/ItemAdd\n"+vueDirName+"/ItemEdit\n"+vueDirName+"/ItemFactor\n"+vueDirName+"/ItemList\n"+vueDirName+"/ItemParticulars\n";
                    log.info("前端路由位置:{}",vueLocation);
                    String authCode = "\n"+
                            camelCaseEntityName+":export \n"+tableDesc+"导出\n"+
                            camelCaseEntityName+":add \n"+tableDesc+"新增\n"+
                            camelCaseEntityName+":batchDelete \n"+tableDesc+"批量删除\n"+
                            camelCaseEntityName+":deleteOne \n"+tableDesc+"删除单个\n"+
                            camelCaseEntityName+":edit \n"+tableDesc+"编辑\n"+
                            camelCaseEntityName+":view \n"+tableDesc+"查看\n"+
                            camelCaseEntityName+":itemList \n"+tableDesc+"列表\n";
                    log.info("权限代码:{}",authCode);
                }
                //替换t_ 是首字母才进行替换
                Boolean prefixStart = StrUtil.startWith(item,NEED_REPLACE_TABLE_PREFIX,false);
                if(prefixStart){
                    item = StrUtil.replace(item,NEED_REPLACE_TABLE_PREFIX,"");
                }
                String className = CodeHandlerUtil.upFirst(item);
                //表名和实体名称(驼峰命名)
                String camelCaseName = CodeHandlerUtil.camelCaseName(item);
                String vueAPI =
                    "//---------------" + tableDesc + "----start-------------------\n" +
                    "/*分页查询" + tableDesc + "*/\n" +
                    "query" + className + "ByPage: {\n" +
                    "    method: 'POST',\n" +
                    "    url: `${httpUrl}/api/v1/" + camelCaseName + "/queryByPage`,\n" +
                    "    needToken:true,\n" +
                    "},\n" +
                    "/*查询" + tableDesc + "*/\n" +
                    "query" + className + ": {\n" +
                    "    method: 'POST',\n" +
                    "    url: `${httpUrl}/api/v1/" + camelCaseName + "/query" + className + "`,\n" +
                    "    needToken:true,\n" +
                    "},\n" +
                    "/*查询单个" + tableDesc + "*/\n" +
                    "queryOne" + className + ": {\n" +
                    "    method: 'POST',\n" +
                    "    url: `${httpUrl}/api/v1/" + camelCaseName + "/queryOne" + className + "`,\n" +
                    "    needToken:true,\n" +
                    "},\n" +
                    "/*新增" + tableDesc + "*/\n" +
                    "add" + className + "Item: {\n" +
                    "    method: 'POST',\n" +
                    "    url: `${httpUrl}/api/v1/" + camelCaseName + "/addItem`,\n" +
                    "    needToken:true,\n" +
                    "},\n" +
                    "/*编辑" + tableDesc + "*/\n" +
                    "edit" + className + "Item: {\n" +
                    "    method: 'PUT',\n" +
                    "    url: `${httpUrl}/api/v1/" + camelCaseName + "/updateItem`,\n" +
                    "    needToken:true,\n" +
                    "},\n" +
                    "/*批量删除" + tableDesc + "*/\n" +
                    "batchDelete" + className + ": {\n" +
                    "    method: 'POST',\n" +
                    "    url: `${httpUrl}/api/v1/" + camelCaseName + "/batchDeleteItem`,\n" +
                    "    needToken:true,\n" +
                    "},\n" +
                    "/*导出" + tableDesc + "*/\n" +
                    "export" + className + "Item: {\n" +
                    "    method: 'POST',\n" +
                    "    url: `${httpUrl}/api/v1/" + camelCaseName + "/exportData" + "`,\n" +
                    "    needToken:true,\n" +
                    "    responseType: 'blob',\n" +
                    "},\n" +
                    "//---------------" + tableDesc + "----end-------------------\n";
                System.out.println(vueAPI);
            }catch (Exception e){
                log.error(">>>>>>>>>>>>>>>>>>>>>>创建数据库对应Java文件出现错误: {} ,{} <<<<<<<<<<<<<<<<",e.getMessage(),e);
            }
        });
    }

    /**
     * 创建后端代码
     * @param tableName 表名
     * @param path 路径
     * @param templateName 模版名称
     * @param fileNameSuffix 文件后缀名称
     * @return String
     * @throws Exception
     */
    public static void createJavaCodeByTemplate(String tableName,
                                                String path,
                                                String templateName,
                                                String fileNameSuffix) throws Exception {
        //1，创建FreeMarker的配置类
        Configuration config = new Configuration();
        //2，指定模板加载器，将模板加入缓存中
        //文件路径加载器，获取到templates文件的路径
        ClassPathResource classPathResource = new ClassPathResource("templates");
        try {
            String absolutePath = classPathResource.getFile().getAbsolutePath();
            FileTemplateLoader fileTemplateLoader=new FileTemplateLoader(new File(absolutePath));
            config.setTemplateLoader(fileTemplateLoader);
            //3，获取模板
            Template template = config.getTemplate(templateName);
            List<Column> columnList = CodeHandlerUtil.getColumnHashMap().get(tableName);
            if(CollectionUtils.isEmpty(columnList)){
                return;
            }
            Map<String,Object> paramHashMap = new HashMap<>();
            //原始表名
            String originalTableName = System.getProperty("ORIGINAL_TABLE_NAME");
            if(!CheckParam.isNull(originalTableName)){
                paramHashMap.put("originalTableName",originalTableName);
            }else{
                paramHashMap.put("originalTableName",tableName);
            }
            //替换t_ 是首字母才进行替换
            Boolean prefixStart = StrUtil.startWith(tableName,NEED_REPLACE_TABLE_PREFIX,false);
            if(prefixStart){
                tableName = StrUtil.replace(tableName,NEED_REPLACE_TABLE_PREFIX,"");
            }
            //表名和实体名称(驼峰命名)
            String camelCaseName = CodeHandlerUtil.camelCaseName(tableName);
            //不需要生成的字段
            List<String> notNeedGenColumnList = Lists.newArrayList();
            notNeedGenColumnList.add("delete_status");
            notNeedGenColumnList.add("id");
            notNeedGenColumnList.add("create_time");
            notNeedGenColumnList.add("update_time");
            notNeedGenColumnList.add("create_by");
            notNeedGenColumnList.add("update_by");
            notNeedGenColumnList.add("operator_id");
            notNeedGenColumnList.add(tableName+"_id");

            //排除不需要生成的字段
            columnList = columnList.stream().filter(item -> !notNeedGenColumnList.contains(item.getColumnName())).collect(
                    Collectors.toList());
            String className = CodeHandlerUtil.upFirst(tableName);

            //表的驼峰命名
            paramHashMap.put("basePackageName",CodeHandler.BASE_PACKAGE_NAME);
            //业务主键ID
            paramHashMap.put("mainId",camelCaseName+"Id");
            //表的驼峰命名
            paramHashMap.put("camelCaseEntityName",camelCaseName);
            //表的首字母大写命名
            paramHashMap.put("className",className);
            //表名
            paramHashMap.put("tableName",tableName);
            //表注释
            paramHashMap.put("entityDesc", CodeHandlerUtil.getTableDesc());
            //列名
            paramHashMap.put("columnList",columnList);
            //序列化字段
            paramHashMap.put("serialVersionUID",buildSerialVersionUID(18));
            String javaFileName;
            if(CheckParam.isNull(fileNameSuffix)){
                javaFileName = path + "/" + className + ".java";
            }else{
                javaFileName = path + "/" + className + fileNameSuffix + ".java";
            }
            String dictionaryName = path.trim();
            File dictionaryFile = new File(dictionaryName);
            if(!dictionaryFile.isDirectory()){
                dictionaryFile.mkdirs();
            }
            File javaFile = new File(javaFileName);
            if(javaFile.exists()){
                javaFile.delete();
            }
            javaFile.createNewFile();
            template.process(paramHashMap,new FileWriter(javaFile));
            template.process(paramHashMap ,new PrintWriter(System.out));
        } catch (IOException e) {
            log.error(">>>>>>>>>>>>>>>>>>>>>>创建数据库对应实体Java文件出现错误: {} ,{} <<<<<<<<<<<<<<<<",e.getMessage(),e);
        }
    }

    /**
      *
      * @description: 随机正数和负数
      * @author: create by singer - Singer email:singer-coder@qq.com
      * @date 2024/3/1
      * @param  length 长度
      * @return
      */
    public static String buildSerialVersionUID(Integer length) {
        String randomSerialVersionUID = RandomUtil.randomNumbers(length);
        if(StrUtil.startWith(randomSerialVersionUID,"0")){
            randomSerialVersionUID = StrUtil.replace(randomSerialVersionUID,"0","1",false);
        }
        //随机正负
        boolean randomPlusOrMinus = RandomUtil.randomBoolean();
        if(randomPlusOrMinus){
            System.out.println(StrUtil.format("生成的serialVersionUID:{}","-"+randomSerialVersionUID+"L"));
            return "-"+randomSerialVersionUID+"L";
        }else{
            System.out.println(StrUtil.format("生成的serialVersionUID:{}",randomSerialVersionUID+"L"));
            return randomSerialVersionUID+"L";
        }
    }

    public static void main(String[] args) {
        IntStream.range(1,30).forEach(item -> {
            buildSerialVersionUID(19);
        });
    }

    /**
      *
      * @description: 判定字段名称是否是选型类型的
      * @author: create by singer - Singer email:singer-coder@qq.com
      * @date 2024/3/20
      * @param filedName 字段名称
      * @return
      */
    public static Boolean isOptionsField(String filedName){
        if(CheckParam.isNull(filedName)){
            return Boolean.FALSE;
        }
        if(StrUtil.endWith(filedName, "Id", Boolean.TRUE)){
            return Boolean.TRUE;
        }
        if(StrUtil.endWith(filedName, "Status", Boolean.TRUE)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 创建后端代码
     * @param tableName 表名
     * @param path 路径
     * @param templateName 模版名称
     * @param fileName 文件名称
     * @return String
     * @throws Exception
     */
    public static void createVUECodeByTemplate(String tableName,
                                            String path,
                                            String templateName,
                                            String fileName) throws Exception {
        //1，创建FreeMarker的配置类
        Configuration config = new Configuration();
        //2，指定模板加载器，将模板加入缓存中
        //文件路径加载器，获取到templates文件的路径
        ClassPathResource classPathResource = new ClassPathResource("templates");
        try {
            String absolutePath = classPathResource.getFile().getAbsolutePath();
            FileTemplateLoader fileTemplateLoader=new FileTemplateLoader(new File(absolutePath));
            config.setTemplateLoader(fileTemplateLoader);
            //3，获取模板
            Template template = config.getTemplate(templateName);
            List<Column> columnList = CodeHandlerUtil.getColumnHashMap().get(tableName);
            if(CollectionUtils.isEmpty(columnList)){
                return;
            }
            Map<String,Object> paramHashMap = new HashMap<>();
            //原始表名
            String originalTableName = System.getProperty("ORIGINAL_TABLE_NAME");
            if(!CheckParam.isNull(originalTableName)){
                paramHashMap.put("originalTableName",originalTableName);
            }else{
                paramHashMap.put("originalTableName",tableName);
            }
            //替换t_ 是首字母才进行替换
            Boolean prefixStart = StrUtil.startWith(tableName,NEED_REPLACE_TABLE_PREFIX,false);
            if(prefixStart){
                tableName = StrUtil.replace(tableName,NEED_REPLACE_TABLE_PREFIX,"");
            }
            //表名和实体名称(驼峰命名)
            String camelCaseName = CodeHandlerUtil.camelCaseName(tableName);
            //不需要生成的字段
            List<String> notNeedGenColumnList = Lists.newArrayList();
            notNeedGenColumnList.add("delete_status");
            notNeedGenColumnList.add("id");
            notNeedGenColumnList.add("create_time");
            notNeedGenColumnList.add("update_time");
            notNeedGenColumnList.add("create_by");
            notNeedGenColumnList.add("update_by");
            notNeedGenColumnList.add("operator_id");
            notNeedGenColumnList.add(tableName+"_id");

            //排除不需要生成的字段
            columnList = columnList.stream()
                    .filter(item -> !notNeedGenColumnList.contains(item.getColumnName())).collect(
                    Collectors.toList());
            //在vue端需要选择的选项
            List<Column> idOptionsList = columnList.stream()
                    .filter(item -> isOptionsField(item.getColumnName()))
                    .collect(Collectors.toList());
            //再次进行数据清洗排除不需要生成的字段信息以及以非选项类型的字段
            columnList = columnList.stream()
                    .filter(item -> !notNeedGenColumnList.contains(item.getColumnName()))
                    .filter(item -> !isOptionsField(item.getColumnName())).collect(
                            Collectors.toList());
            String className = CodeHandlerUtil.upFirst(tableName);
            //表的驼峰命名
            paramHashMap.put("basePackageName",CodeHandler.BASE_PACKAGE_NAME);
            //业务主键ID
            paramHashMap.put("mainId",camelCaseName+"Id");
            //paramHashMap.put("mainId","id");
            //表的驼峰命名
            paramHashMap.put("camelCaseEntityName",camelCaseName);
            //类名
            paramHashMap.put("className",className);
            //表名
            paramHashMap.put("tableName",tableName);
            //表注释
            paramHashMap.put("entityDesc", CodeHandlerUtil.getTableDesc());
            //列名
            paramHashMap.put("columnList",columnList);
            if(!CollectionUtils.isEmpty(idOptionsList)){
                //选项
                paramHashMap.put("idOptionsList",idOptionsList);
            }
            String vueFileName = path + "/" + fileName + ".vue";
            String dictionaryName = path.trim();
            File dictionaryFile = new File(dictionaryName);
            if(!dictionaryFile.isDirectory()){
                dictionaryFile.mkdirs();
            }
            File vueFile = new File(vueFileName);
            if(vueFile.exists()){
                vueFile.delete();
            }
            vueFile.createNewFile();
            template.process(paramHashMap,new FileWriter(vueFile));
            template.process(paramHashMap ,new PrintWriter(System.out));
        } catch (IOException e) {
            log.error(">>>>>>>>>>>>>>>>>>>>>>创建数据库对应实体VUE文件出现错误: {} ,{} <<<<<<<<<<<<<<<<",e.getMessage(),e);
        }
    }


}
