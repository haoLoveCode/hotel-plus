package cn.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName multi-platform
 * @Description:
 * @date 2024-02-26
 */
@Getter
@AllArgsConstructor
public enum FeedBackStatusEnums {

    /**
     * 已提交
     */
    UN_HANDLE (1,"已提交"),

    /**
     * 已处理
     */
    HDNDLED (2,"已处理"),


    ;


    /**
     * 状态码
     */
    private Integer code;

    /**
     * 值
     */
    private String value;


    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }


}
