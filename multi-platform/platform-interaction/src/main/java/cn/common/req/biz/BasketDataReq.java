package cn.common.req.biz;

import lombok.Data;
import pro.skywalking.req.base.BasePageReq;
import java.io.Serializable;

/**
 * 购物车信息分页查询请求封装类
 * @title: BasketDataReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class BasketDataReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = -509631951225776340L;

    /**
     * 业务主键ID->"basketDataId"
     */
    private String basketDataId;

    /**
     * 用户ID
     */
    private String authAppUserId;

    /**
     * 商品ID
     */
    private String salesItemId;

    /**
     * 备注信息
     */
    private String remarkData;

    /**
     * 购买数量
     */
    private Integer itemNum;

}
