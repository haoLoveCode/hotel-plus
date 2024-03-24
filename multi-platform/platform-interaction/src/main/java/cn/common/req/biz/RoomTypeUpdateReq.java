package cn.common.req.biz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 房间类型信息请求数据封装类
 * @title: RoomTypeUpdateReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class RoomTypeUpdateReq implements Serializable {

    private static final long serialVersionUID = -819005812573055312L;

    /**
     * 主键ID->ID
     */
    private Long id;

    /**
     * 业务主键ID->"roomTypeId"
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String roomTypeId;

    /**
     * 类型名称
     */
    @NotEmpty(message = "类型名称->不可为空")
    private String typeName;

    /**
     * 备注信息
     */
    @NotEmpty(message = "备注信息->不可为空")
    private String remarkData;

}
