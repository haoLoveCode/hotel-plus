package cn.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author create by singer - Singer email:singer-coder@qq.com
 * @packageName pro.common.enums
 * @Description: 订单类型信息
 * @date 2024-03-25
 */
@Getter
@AllArgsConstructor
public enum OrderTypeEnum {

    /**
     * SALES_ITEM
     */
    SALES_ITEM ("SALES_ITEM","商品");

    /**
     * 状态码
     */
    private String code;

    /**
     * 值
     */
    private String value;


    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
