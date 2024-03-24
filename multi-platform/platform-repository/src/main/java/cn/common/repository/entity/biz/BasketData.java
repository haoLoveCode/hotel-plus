package cn.common.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;


/**
 * 购物车信息实体
 * @title: BasketData.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
@TableName("basket_data")
public class BasketData extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 145586770543305494L;

    /**
     * 业务主键ID
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
