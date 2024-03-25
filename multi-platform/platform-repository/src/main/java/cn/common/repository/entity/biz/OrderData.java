package cn.common.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;


/**
 * 订单信息实体
 * @title: OrderData.java
 * @author Singer
 * @date 2024-03-25 11:13
 */
@Data
@TableName("order_data")
public class OrderData extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 6355916981619176618L;

    /**
     * 业务主键ID
     */
    private String orderDataId;

    /**
     * 订单交易订单ID
     */
    private String tradeOrderId;

    /**
     * 订单的支付订单号
     */
    private String outTradeNo;

    /**
     * APP用户ID
     */
    private String authAppUserId;

    /**
     * 商品ID
     */
    private String itemId;

    /**
     * 商品快照数据
     */
    private String itemData;

    /**
     * 购买数量
     */
    private Integer itemNum;

    /**
     * 订单备注
     */
    private String orderRemark;

}
