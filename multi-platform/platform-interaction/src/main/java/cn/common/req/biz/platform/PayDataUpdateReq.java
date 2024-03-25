package cn.common.req.biz.platform;

import lombok.Data;
import pro.skywalking.validation.NotEmpty;

import java.io.Serializable;

/**
 * 支付数据信息实体
 * @title: PayDataUpdateReq.java
 * @author Singer
 * @date 2024-03-25 11:13
 */
@Data
public class PayDataUpdateReq implements Serializable {

    private static final long serialVersionUID = -4964248074334247183L;

    /**
     * 主键ID->ID
     */
    private Long id;

    /**
     * 业务主键ID->"payDataId"
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String payDataId;


    /**
     * 关联的支付订单号
     */
     @NotEmpty(message = "关联的支付订单号->不可为空")
     private String outTradeNo;

    /**
     * 支付数据
     */
     @NotEmpty(message = "支付数据->不可为空")
     private String payData;

}
