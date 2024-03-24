package cn.common.req.biz.openBiz;

import lombok.Data;
import pro.skywalking.req.base.BasePageReq;
import java.io.Serializable;

/**
 * 商品类型信息分页查询请求封装类
 * @title: SalesItemTypeReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class SalesItemTypeReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = 542086740062774291L;

    /**
     * 业务主键ID->"salesItemTypeId"
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
