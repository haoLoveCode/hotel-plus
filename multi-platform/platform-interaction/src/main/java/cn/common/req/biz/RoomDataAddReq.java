package cn.common.req.biz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 房间信息新增Req
 * @title: RoomDataAddReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class RoomDataAddReq implements Serializable {

    private static final long serialVersionUID = 370018351044084170L;

    /**
     * 房间类型ID
     */
    @NotEmpty(message = "房间类型ID->不可为空")
    private String roomTypeId;

    /**
     * 房间展示标题
     */
    @NotEmpty(message = "房间展示标题->不可为空")
    private String roomTitle;

    /**
     * 房间简介
     */
    @NotEmpty(message = "房间简介->不可为空")
    private String briefData;

    /**
     * 房间编号
     */
    @NotEmpty(message = "房间编号->不可为空")
    private String roomNo;

    /**
     * 房间图片
     */
    @NotEmpty(message = "房间图片->不可为空")
    private String roomImg;

    /**
     * 房间状态
     */
    @NotEmpty(message = "房间状态->不可为空")
    private Integer roomStatus;

    /**
     * 房间楼层
     */
    @NotEmpty(message = "房间楼层->不可为空")
    private Integer roomFloor;

    /**
     * 价格
     */
    @NotEmpty(message = "价格->不可为空")
    private BigDecimal unitPrice;

    /**
     * 房间面积
     */
    @NotEmpty(message = "房间面积->不可为空")
    private BigDecimal roomArea;

    /**
     * 床位数量
     */
    @NotEmpty(message = "床位数量->不可为空")
    private Integer bedNum;

}
