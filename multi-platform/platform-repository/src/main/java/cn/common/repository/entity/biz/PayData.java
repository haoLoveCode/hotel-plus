package cn.common.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;


/**
 * 支付数据信息实体
 * @title: PayData.java
 * @author Singer
 * @date 2024-03-25 11:13
 */
@Data
@TableName("pay_data")
public class PayData extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2975466263840867589L;

    /**
     * 业务主键ID
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
