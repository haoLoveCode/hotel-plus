package cn.common.resp.biz;

import pro.skywalking.req.platform.base.BaseResp;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 购物车信息返回数据封装类
 * @title: BasketDataResp.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class BasketDataResp extends BaseResp implements Serializable {

    private static final long serialVersionUID = 598019871849176815L;

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
