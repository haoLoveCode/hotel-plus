package cn.common.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 房间信息实体
 * @title: RoomData.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
@TableName("room_data")
public class RoomData extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -133552651946776536L;

    /**
     * 业务主键ID
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
