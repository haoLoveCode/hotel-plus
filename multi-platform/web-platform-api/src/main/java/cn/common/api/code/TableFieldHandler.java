package cn.common.api.code;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.compress.utils.Lists;
import pro.skywalking.utils.CheckParam;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName mellitus-platform
 * @Description:
 * @date 2024-02-23
 */
public class TableFieldHandler {

    /**
     * 用户名
     */
    public final static String DB_USER = "mellitus-platform";

    /**
     * 密码
     */
    public final static String DB_PASSWORD = "123456";


    /**
     * 数据库配置->驱动类名
     */
    private final static String DIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * 数据库配置
     */
    public final static String DB_NAME = "mellitus-platform";

    //忽略的字段
    public static HashMap<String,String> ignoreFieldHashMap = new HashMap();

    static {
        ignoreFieldHashMap.put("id","id");
        ignoreFieldHashMap.put("create_time","create_time");
        ignoreFieldHashMap.put("update_time","update_time");
        ignoreFieldHashMap.put("operator_id","operator_id");
        ignoreFieldHashMap.put("delete_status","delete_status");
    }

    /**
     * 读取mysql某数据库下表的注释信息
     *
     * @author xxx
     */
    public static Connection getMySQLConnection() throws Exception {
        Class.forName(DIVER);
        Connection conn = DriverManager.getConnection("jdbc:mysql://139.155.74.121:6006/"+DB_NAME, DB_USER, DB_PASSWORD);
        return conn;
    }


    /**
     * 获取当前数据库下的所有表名称
     *
     * @return
     * @throws Exception
     */
    public static List<String> getAllTableName() throws Exception {
        List<String> tables = new ArrayList();
        Connection conn = getMySQLConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SHOW TABLES ");
        while (rs.next()) {
            String tableName = rs.getString(1);
            tables.add(tableName);
        }
        rs.close();
        stmt.close();
        conn.close();
        return tables;
    }


    /**
     * 获得某表的建表语句
     *
     * @param tableName
     * @return
     * @throws Exception
     */
    public static Map getCommentByTableName(List tableName) throws Exception {
        Map map = new HashMap();
        Connection conn = getMySQLConnection();
        Statement stmt = conn.createStatement();
        for (int i = 0; i < tableName.size(); i++) {
            String table = (String) tableName.get(i);
            ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + table);
            if (rs != null && rs.next()) {
                String createDDL = rs.getString(2);
                String comment = parse(createDDL);
                map.put(table, comment);
            }
            rs.close();
        }
        stmt.close();
        conn.close();
        return map;
    }

    /**
     * 获得某表中所有字段的注释
     *
     * @param tableName
     * @return
     * @throws Exception
     */
    public static void getTableColumnComment(List<String> tableName) throws Exception {
        Map<String,Object> map = new HashMap();
        Connection conn = getMySQLConnection();
        Statement stmt = conn.createStatement();
        for (int index = 0; index < tableName.size(); index++) {
            String table = String.valueOf(tableName.get(index));
            ResultSet rs = stmt.executeQuery("show full columns from " + table);
            System.out.println("【" + table + "】");
            while (rs.next()) {
                System.out.println(rs.getString("Field") + "\t:\t" + rs.getString("Comment"));
            }
            rs.close();
        }
        stmt.close();
        conn.close();
    }

    /**
     * 拿到某个表的表名称和注释
     *
     * @param tableName 表名称
     * @return
     * @throws Exception
     */
    public static String getTableCommentByTableName(String tableName) throws Exception {
        Connection conn = getMySQLConnection();
        Statement stmt = conn.createStatement();
        String querySQL = StrUtil.format(
                "SELECT " +
                        "  table_name AS tableName, " +
                        "  TABLE_COMMENT AS tableComment " +
                        "  FROM " +
                        "  INFORMATION_SCHEMA.TABLES " +
                        "  WHERE " +
                        "  table_schema = '{}' " +
                        "  AND table_name = '{}'", DB_NAME, tableName);
        ResultSet resultSet = stmt.executeQuery(querySQL);
        StringBuilder stringBuilder = new StringBuilder();
        while (resultSet.next()) {
            String tableNameFromResult;
            if(StrUtil.contains(resultSet.getString("tableComment"),"表")){
                tableNameFromResult = resultSet.getString("tableComment");
            }else{
                tableNameFromResult = StrUtil.removeAll(resultSet.getString("tableComment"),"表");
            }
            String format = StrUtil.format("{}({}表)字段说明\n",
                    tableNameFromResult,
                    resultSet.getString("tableName")
            );
            //String format = StrUtil.format("{}、", resultSet.getString("Comment"));
            stringBuilder.append(format);
        }
        resultSet.close();
        stmt.close();
        conn.close();
        System.out.println(stringBuilder);
        return stringBuilder.toString();
    }
    /**
     * 获得某表中所有字段的注释
     *
     * @param tableName
     * @return
     * @throws Exception
     */
    public static void getTableColumnComment(String tableName) throws Exception {
        Connection conn = getMySQLConnection();
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("show full columns from " + tableName);
        //拿到某个表的表名称和注释
        getTableCommentByTableName(tableName);
        StringBuilder tableColumnBuilder = new StringBuilder();
        StringBuilder tableFieldCommentBuilder = new StringBuilder();
        tableFieldCommentBuilder.append("该表主要包含了");

        List<String> fieldList = Lists.newArrayList();
        while (resultSet.next()) {
            String ignoreField = ignoreFieldHashMap.get(resultSet.getString("Field"));
            if(!CheckParam.isNull(ignoreField)){
                continue;
            }
            /*String format = StrUtil.format("{}\t{}\t{}\t{}\t{}\t{}\n",
                    resultSet.getString("Field"),
                    resultSet.getString("Comment"),
                    getLengthByColumnType(resultSet.getString("Type")),
                    getJavaTypeByColumnType(resultSet.getString("Type")),
                    resultSet.getString("Type"),
                    "无");*/

            String format = StrUtil.format("{}\t{}\t{}\t{}\t{}\t{}\t{}\n",
                    resultSet.getString("Field"),
                    resultSet.getString("Comment"),
                    getLengthByColumnType(resultSet.getString("Type")),
                    getJavaTypeByColumnType(resultSet.getString("Type")),
                    resultSet.getString("Type"),
                    "否",
                    "无");
            //String format = StrUtil.format("{}、", resultSet.getString("Comment"));
            tableColumnBuilder.append(format);

            tableFieldCommentBuilder.append(StrUtil.format("{}、",resultSet.getString("Comment")));
            fieldList.add(resultSet.getString("Field"));
        }
        System.out.println(tableColumnBuilder);
        System.out.println(tableFieldCommentBuilder);
        System.out.println(StrUtil.format("字段数量:{}",fieldList.size()));
        resultSet.close();
        stmt.close();
        conn.close();
    }

    /**
     *
     * @description: 正则表达式从字符串中获取数字
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024/2/23
     * @param  str 字符串
     * @return
     */
    public static String getNumFromStr(String str) {
        String dest = "";
        if (str != null) {
            dest = str.replaceAll("[^0-9]","");
        }
        if(CheckParam.isNull(dest)){
            return BigInteger.ZERO.toString();
        }
        return dest;
    }

    /**
     *
     * @description: 根据数据库类型匹配Java类型
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024/2/23
     * @param
     * @return
     */
    public static Integer getLengthByColumnType(String columnType){
        if(CheckParam.isNull(columnType)){
            return 0;
        }
        if(StrUtil.equalsIgnoreCase("bigint",columnType)){
            return 20;
        }
        if(StrUtil.contains(columnType,"varchar")){
            return Integer.valueOf(getNumFromStr(columnType));
        }
        if(StrUtil.equalsIgnoreCase("text",columnType)){
            return 0;
        }
        if(StrUtil.equalsIgnoreCase("int",columnType)){
            return 10;
        }
        if(StrUtil.equalsIgnoreCase("datetime",columnType)){
            return 0;
        }
        return 0;
    }

    /**
     *
     * @description: 根据数据库类型匹配Java类型
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024/2/23
     * @param
     * @return
     */
    public static String getJavaTypeByColumnType(String columnType){
        if(CheckParam.isNull(columnType)){
            return "String";
        }
        if(StrUtil.equalsIgnoreCase("bigint",columnType)){
            return "String";
        }
        if(StrUtil.contains("varchar",columnType)){
            return "String";
        }
        if(StrUtil.equalsIgnoreCase("text",columnType)){
            return "String";
        }
        if(StrUtil.equalsIgnoreCase("int",columnType)){
            return "Integer";
        }
        if(StrUtil.equalsIgnoreCase("datetime",columnType)){
            return "LocalDateTime";
        }
        return "String";
    }

    /**
     * 返回注释信息
     *
     * @param all
     * @return
     */

    public static String parse(String all) {
        String comment = null;
        int index = all.indexOf("COMMENT='");
        if (index < 0) {
            return "";
        }
        comment = all.substring(index + 9);
        comment = comment.substring(0, comment.length() - 1);
        return comment;
    }

    public static void main(String[] args) throws Exception {
        List tables = getAllTableName();
        Map tablesComment = getCommentByTableName(tables);
        Set names = tablesComment.keySet();
        Iterator iter = names.iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            //System.out.println(StrUtil.format("{},{}",name,tablesComment.get(name)));
            String tableName = StrUtil.replace(String.valueOf(tablesComment.get(name)), "表", "");
            System.out.println(StrUtil.format("{}",tableName));

            //System.out.println(StrUtil.format("update {} set create_time = '2024-03-14 16:11:43' ;",name));
            //System.out.println(StrUtil.format("update {} set update_time = '2024-03-14 16:11:43' ;",name));
        }

        //拿到某张表的所有字段注释
        getTableColumnComment("medical_history");
    }
}
