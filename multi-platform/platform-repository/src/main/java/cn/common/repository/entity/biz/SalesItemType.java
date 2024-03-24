package cn.common.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;


/**
 * 商品类型信息实体
 * @title: SalesItemType.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
@TableName("sales_item_type")
public class SalesItemType extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 842874471615109275L;

    /**
     * 业务主键ID
     */
    private String salesItemTypeId;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 备注信息
     */
    private String remarkData;

    /**
     * 类型状态
     */
    private Integer dataStatus;

}
