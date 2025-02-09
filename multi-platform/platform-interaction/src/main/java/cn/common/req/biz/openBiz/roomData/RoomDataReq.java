package cn.common.req.biz.openBiz.roomData;

import lombok.Data;
import pro.skywalking.req.base.BasePageReq;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 房间信息分页查询请求封装类
 * @title: RoomDataReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class RoomDataReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = -980738483757317697L;

    /**
     * 业务主键ID->"roomDataId"
     */
    private String roomDataId;

    /**
     * 房间类型ID
     */
    private String roomTypeId;

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
    private String mainImg;

    /**
     * 房间状态
     */
    private Integer roomStatus;

    /**
     * 房间楼层
     */
    private Integer roomFloor;

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
