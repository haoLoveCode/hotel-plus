package cn.common.resp.biz;

import pro.skywalking.req.platform.base.BaseResp;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 房间信息返回数据封装类
 * @title: RoomDataResp.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class RoomDataResp extends BaseResp implements Serializable {

    private static final long serialVersionUID = 645050537219026458L;

    /**
     * 业务主键ID->"roomDataId"
     */
    private String roomDataId;

    /**
     * 房间展示标题
     */
    private String roomTitle;

    /**
     * 房间简介
     */
    private String briefData;

    /**
     * 房间编号
     */
    private String roomNo;

    /**
     * 房间图片
     */
    private String roomImg;

    /**
     * 房间状态
     */
    private Integer roomStatus;

    /**
     * 房间楼层
     */
    private Integer roomFloor;

    /**
     * 房间类型
     */
    private String roomType;

    /**
     * 价格
     */
    private BigDecimal unitPrice;

    /**
     * 房间面积
     */
    private BigDecimal roomArea;

    /**
     * 床位数量
     */
    private Integer bedNum;

}
