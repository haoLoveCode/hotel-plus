package cn.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Singer create by Singer email:singer-coder@qq.com
 * @packageName cn.common.enums
 * @Description: 房间状态枚举
 * @date 2021-01-21
 */
@Getter
@AllArgsConstructor
public enum RoomStatusEnum {


    /**
     * 闲置
     */
    UNUSED(1,"闲置"),

    /**
     * 已预订
     */
    BOOKING(2,"已预订"),

    /**
     * 维护中
     */
    MAINTAINED(3,"维护中"),

    /**
     * 已入住
     */
    CHECK_IN(4,"已入住"),

    /**
     * 已退住
     */
    CHECK_OUT(4,"已退住"),

    ;

    /**
     * 错误码code
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
