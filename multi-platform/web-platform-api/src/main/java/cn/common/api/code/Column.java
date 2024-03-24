package cn.common.api.code;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据库表信息
 * @title: Column.java
 * @author Singer
 * @date 2024-02-29
 */
@Data
public class Column implements Serializable {

	private static final long serialVersionUID = 6613837222412443948L;


	/**
	 * 列名
	 */
	public String columnName;

	/**
	 * 驼峰列名
	 */
	public String camelCaseName;

	/**
	 * 列名的get和set的时候填充的名字
	 */
	private String setOrSetName;

	/**
	 * 列名描述(注释)
	 */
	public String columnNameDesc;

	/**
	 * 数据类型
	 */
	public String dataType;

	/**
	 * 列长度
	 */
	public Integer columnLength;


	/**
	 * 枚举值
	 */
	public String[] enums;

}
