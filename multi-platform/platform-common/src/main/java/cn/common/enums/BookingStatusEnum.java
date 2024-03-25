package cn.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Singer create by Singer email:singer-coder@qq.com
 * @packageName cn.common.enums
 * @Description: 房间预定状态枚举
 * @date 2021-01-21
 */
@Getter
@AllArgsConstructor
public enum BookingStatusEnum {

    /**
     * 预定成功
     */
    BOOKING_SUCCESS(1,"预定成功"),

    /**
     * 已入住
     */
    CHECKED_IN(2,"已入住"),

    /**
     * 已取消
     */
    CANCEL_BOOKING(3,"已取消"),

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
