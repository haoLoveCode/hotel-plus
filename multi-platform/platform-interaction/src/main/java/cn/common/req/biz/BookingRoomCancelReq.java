package cn.common.req.biz;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pro.skywalking.constants.PlatformConstant;
import pro.skywalking.validation.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * BookingRoomCancelReq
 * @title: BookingRoomReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class BookingRoomCancelReq implements Serializable {

    private static final long serialVersionUID = 6269531265385036833L;

    /**
     * 预定ID
     */
    @NotEmpty(message = "预定ID->不可为空")
    private String roomBookingId;

}
