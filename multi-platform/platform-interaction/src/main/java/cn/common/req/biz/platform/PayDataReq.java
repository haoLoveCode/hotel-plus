package cn.common.req.biz.platform;

import lombok.Data;
import pro.skywalking.req.base.BasePageReq;

import java.io.Serializable;

/**
 * 支付数据信息分页查询请求封装类
 * @title: PayDataReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class PayDataReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = 7786690980859488324L;

    /**
     * 业务主键ID->"payDataId"
     */
    private String payDataId;

    /**
     * 关联的支付订单号
     */
    private String outTradeNo;

    /**
     * 支付数据
     */
    private String payData;

}
