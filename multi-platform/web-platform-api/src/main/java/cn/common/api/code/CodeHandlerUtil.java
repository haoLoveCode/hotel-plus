package cn.common.api.code;

import lombok.Data;
import pro.skywalking.utils.CheckParam;
import pro.skywalking.utils.StringTool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * @author Singer
 * @packageName cn.common.api.code
 * @Description: 代码生成处理工具类
 * @date 2024-02-29
 */
@Data
public class CodeHandlerUtil {

    /**
     * 主键
     */
    private final static String PKV = "PRI";

    /**
     * String字段
     */
    private final static String STRING = "String";

    /**
     * LocalDateTime字段
     */
    private final static String DATE = "LocalDateTime";

    /**
     * Integer字段
     */
    private final static String INTEGER = "Integer";

    /**
     * LONG字段
     */
    private final static String LONG = "Long";

    /**
     * byte字段
     */
    private final static String BYTE = "byte";

    /**
     * BigDecimal字段
     */
    private final static String BIG_DECIMAL = "BigDecimal";

    /**
     * 枚举字典
     */
    private final static String ENUM = "enum";

    /**
     * 正括号
     */
    private final static String PARENT_BRACKET = "(";

    /**
     * 反括号
     */
    private final static String REVERSE_BRACKET = ")";

    /**
     * 逗号
     */
    private final static String COMMA_STRING = ",";

    /**
     * 表注释
     */
    private static String TABLE_DESC = "";

    /**
     * key=表名  val=col 只支持单主键
     */
    private static HashMap<String, Column> keyHashMap = new HashMap<>();

    /**
     * 表名HashMap
     */
    private static HashMap<String, String> tableHashMap = new HashMap<>();

    /**
     * key=table_name  val=表的列名集合
     */
    private static HashMap<String, List<Column>> columnHashMap = new HashMap<>();


    /**
     * 拿到表注释
     * @author: Singer
     * @date 2024-02-293
     * @return java.util.HashMap
     */
    public static String getTableDesc(){
        return TABLE_DESC;
    }

    /**
      * 拿到表的列名集合HashMap
      * @author: Singer
      * @date 2024-02-293
      * @return java.util.HashMap
      */
    public static HashMap<String, List<Column>> getColumnHashMap(){
        return columnHashMap;
    }


    /**
     * 拿到数据库里面指定的表的所有的字段
     * @author: Singer
     * @date 2024-02-29
     * @param dataBase 数据库名称
     * @param tableName 表名称
     * @param tableDesc 表注释
     * @return
     */
    public static void readTableField(String dataBase,
                                      String tableName,
                                      String tableDesc) throws Exception {
        tableName = dropNull(tableName);
        tableDesc = dropNull(tableDesc);
        //设置表注释
        TABLE_DESC = tableDesc;
        String [] sz  = tableName.split(",");
        StringBuffer buffer = new StringBuffer();
        String tbs = null;
        if (tableName.length() != 0) {
            for (int i = 0; i < sz.length; i++) {
                String temp = sz[i].trim();
                if (temp.length() != 0) {
                    buffer.append(",").append("'").append(temp).append("'");
                }
            }
            tbs = buffer.toString();
            if (tbs.startsWith(COMMA_STRING)) {
                tbs = tbs.substring(1);
            }
        }
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        String insql = "";
        if (tbs.length() != 0) {
            insql = " AND TABLE_NAME in (" + tbs + ") ";
        }
        String sqlTable = "select  TABLE_NAME,table_comment FROM information_schema.tables   " +
                " where TABLE_SCHEMA='" + dataBase + "' " + insql + " ORDER BY TABLE_NAME";
        ResultSet rsTable = statement.executeQuery(sqlTable);
        while (rsTable.next()) {
            String tn = rsTable.getString("TABLE_NAME").toLowerCase().trim();
            String comment = rsTable.getString("table_comment").trim();
            tableHashMap.put(tn, comment);
        }
        String sql = "select  TABLE_NAME,COLUMN_NAME,data_type,COLUMN_key,COLUMN_COMMENT, ORDINAL_POSITION,COLUMN_TYPE  FROM information_schema.`COLUMNS`  " +
                " where TABLE_SCHEMA='" + dataBase + "' " + insql + " ORDER BY TABLE_NAME,ORDINAL_POSITION";
        System.out.println(sql);
        //统计出列的总数
        Integer totalColumn = returnColumnTotalCount(sql);
        ResultSet resultSet = statement.executeQuery(sql);
        //处理表结果查询的结果信息
        handleResultSet(resultSet, columnHashMap,totalColumn);
        statement.close();
        connection.close();
    }

    /**
     * 拿到数据库里面指定的表的所有的字段
     * @author: Singer
     * @date 2024-02-29
     * @param dataBase 数据库名称
     * @param tableName 表名称
     * @return
     */
    public static void readTableField(String dataBase,
                                      String tableName) throws Exception {
        tableName = dropNull(tableName);
        String [] sz  = tableName.split(",");
        StringBuffer buffer = new StringBuffer();
        String tbs = null;
        if (tableName.length() != 0) {
            for (int i = 0; i < sz.length; i++) {
                String temp = sz[i].trim();
                if (temp.length() != 0) {
                    buffer.append(",").append("'").append(temp).append("'");
                }
            }
            tbs = buffer.toString();
            if (tbs.startsWith(COMMA_STRING)) {
                tbs = tbs.substring(1);
            }
        }
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        String insql = "";
        if (tbs.length() != 0) {
            insql = " AND TABLE_NAME in (" + tbs + ") ";
        }
        String sqlTable = "select  TABLE_NAME,table_comment FROM information_schema.tables   " +
                " where TABLE_SCHEMA='" + dataBase + "' " + insql + " ORDER BY TABLE_NAME";
        ResultSet rsTable = statement.executeQuery(sqlTable);
        while (rsTable.next()) {
            String tn = rsTable.getString("TABLE_NAME").toLowerCase().trim();
            String comment = rsTable.getString("table_comment").trim();
            tableHashMap.put(tn, comment);
        }
        String sql = "select  TABLE_NAME,COLUMN_NAME,data_type,COLUMN_key,COLUMN_COMMENT, ORDINAL_POSITION,COLUMN_TYPE  FROM information_schema.`COLUMNS`  " +
                " where TABLE_SCHEMA='" + dataBase + "' " + insql + " ORDER BY TABLE_NAME,ORDINAL_POSITION";
        System.out.println(sql);
        //统计出列的总数
        Integer totalColumn = returnColumnTotalCount(sql);
        ResultSet resultSet = statement.executeQuery(sql);
        //处理表结果查询的结果信息
        handleResultSet(resultSet, columnHashMap,totalColumn);
        statement.close();
        connection.close();
    }


    /**
     * 处理查询结果
     * @author: Singer
     * @date 2024-02-29
     * @param resultSet resultSet
     * @param columnMap columnMap
     * @param totalCountColumn tot
     */
    public static void handleResultSet(ResultSet resultSet,
                                        Map<String,
                                                List<Column>> columnMap,
                                        Integer totalCountColumn)  throws Exception {
        String tblName = "";
        List<Column> columnList = new Vector<>();
        Integer num = 0;
        while (resultSet.next()) {
            num++;
            String tbName = resultSet.getString("TABLE_NAME").toLowerCase().trim();
            String columnName = resultSet.getString("COLUMN_NAME").toLowerCase().trim();
            String dataType = resultSet.getString("data_type").trim();
            String pk = resultSet.getString("COLUMN_key");
            String desc = resultSet.getString("COLUMN_COMMENT");
            String length = resultSet.getString("COLUMN_TYPE");
            if (desc == null) {
                desc = "";
            }
            Column column = new Column();
            if ("int".equals(dataType) || "long".equals(dataType) || "tinyint".equals(dataType)) {
                dataType = INTEGER;
            }else if("bigint".equals(dataType)){
                dataType = LONG;
            } else if ("date".equals(dataType)
                    || "datetime".equals(dataType)) {
                dataType = DATE;
            } else if ("float".equals(dataType) || "double".equals(dataType) || "decimal".equals(dataType)) {
                dataType = BIG_DECIMAL;
            } else if ("blob".equals(dataType)) {
                dataType = BYTE;
            } else if ("enum".equals(dataType)) {
                dataType = ENUM;
                column.setColumnName(columnName);
                column.setColumnNameDesc(desc);
                column.setDataType(dataType);
                column.setEnums(columnToEnums(length));
            } else {
                dataType = STRING;
            }
            //如果非枚举则执行逻辑
            if (!dataType.equals(ENUM)) {
                column.setColumnName(columnName);
                column.setColumnNameDesc(desc);
                column.setDataType(dataType);
                column.setColumnLength(columnLength(length));
            }
            String camelCaseName = camelCaseName(columnName);
            column.setCamelCaseName(camelCaseName);
            column.setSetOrSetName(camelCaseName.substring(0,1).toUpperCase()
                    .concat(camelCaseName.substring(1)));
            //主键
            if (PKV.equals(pk)) {
                keyHashMap.put(tbName, column);
            }
            //不相等
            if (!tbName.equals(tblName)) {
                if (tblName.length() != 0) {
                    columnMap.put(tblName, columnList);
                    columnList = new Vector<>();
                    tblName = tbName;
                }
            }
            columnList.add(column);
            //如果字段循环完毕，则放进Map里面
            if (totalCountColumn.equals(num)) {
                columnMap.put(tbName, columnList);
            }
        }
        resultSet.close();
    }

    /**
     * 转换为驼峰下划线
     * @author: Singer
     * @date 2024-02-29
     * @param underscoreName 需要被转换的下划线字段字段
     * @return String
     */
    public static String camelCaseName(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * 首字母转驼峰，下划线转驼峰
     * @author: Singer
     * @date 2024-02-29
     * @param str 需要被转换的字段
     * @return String
     */
    public static String upFirst(String str) {
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        str = StringTool.camel(new StringBuffer(str)).toString();
        return str;
    }

    /**
     * 拿到列的长度
     * @author: Singer
     * @date 2024-02-29
     * @param columnType
     * @return
     */
    private static Integer columnLength(String columnType) {
        int ret = 1000;
        if (CheckParam.isNull(columnType) ||
                columnType.indexOf(PARENT_BRACKET) == -1) {
            return ret;
        }
        String result = columnType.substring(columnType.indexOf(PARENT_BRACKET) + 1);
        result = result.substring(0, result.indexOf(REVERSE_BRACKET));
        if (result.indexOf(COMMA_STRING) != -1) {
            result = result.substring(0, result.indexOf(COMMA_STRING));
        }
        return Integer.valueOf(result);
    }

    /**
     *
     * 创建UTF文件
     * @author: Singer
     * @date 2024-02-29
     * @param filePath 文件路径
     * @param content 文件内容
     * @return
     */
    public static void createUtfFile(String filePath, String content) throws Exception {
        String path = filePath.substring(0, filePath.lastIndexOf("/"));
        File file = new File(path);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath), "utf-8");
        out.write(content);
        out.close();
    }

    /**
     * 统计出表所有的字段的数量
     * @author: Singer
     * @date 2024-02-29
     * @param sql SQL
     * @return Integer
     */
    public static Integer returnColumnTotalCount(String sql) throws Exception {
        Connection con = getConnection();
        Statement st = con.createStatement();
        sql = "select count(*) from (" + sql + ") as tt ";
        ResultSet resultSet = st.executeQuery(sql);
        resultSet.next();
        return resultSet.getInt(1);
    }

    /**
     * 需要被清除空的值
     * @author: Singer
     * @date 2024-02-29
     * @param value
     * @return String
     */
    public static String dropNull(String value){
        if(null == value) {
            return "";
        }
        return value.trim();
    }

    /**
     * 列数据变为枚举
     * @author: Singer
     * @date 2024-02-29
     * @param  columnType 列数据类型
     * @return String[]
     */
    private static String[] columnToEnums(String columnType) {
        if(columnType == null || columnType.length() == 0 || columnType.indexOf(PARENT_BRACKET) == -1) {
            return null;
        }
        String s = columnType.substring(columnType.indexOf(PARENT_BRACKET) + 1);
        s = s.substring(0, s.indexOf(REVERSE_BRACKET));
        String[] enums = null;
        if (s.indexOf(COMMA_STRING) != -1) {
            enums = s.replace("\'", "").trim().split(",");
        }
        return enums;
    }

    /**
     * 拿到数据库的连接
     * @author: Singer
     * @date 2024-02-29
     * @return
     */
    public static Connection getConnection() throws Exception {
        Class.forName(System.getProperty("DB_DIVER"));
        Connection con = DriverManager.getConnection(
                System.getProperty("DB_URL"),
                System.getProperty("DB_USER"),
                System.getProperty("DB_PASSWORD"));
        return con;
    }

}
