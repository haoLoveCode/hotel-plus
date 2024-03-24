package cn.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author create by singer - Singer email:singer-coder@qq.com
 * @packageName pro.common.enums
 * @Description: 购物车操作类型信息
 * @date 2024-03-06
 */
@Getter
@AllArgsConstructor
public enum BasketHandleTypeEnum {

    /**
     * 增加
     */
    ADD (1,"增加"),


    /**
     * 减少
     */
    MINUS (2,"减少");


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
