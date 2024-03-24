package cn.common.req.biz.openBiz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品类型信息请求数据封装类
 * @title: SalesItemTypeUpdateReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class SalesItemTypeUpdateReq implements Serializable {

    private static final long serialVersionUID = -931395315145224724L;

    /**
     * 主键ID->ID
     */
    private Long id;

    /**
     * 业务主键ID->"salesItemTypeId"
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String salesItemTypeId;

    /**
     * 类型名称
     */
    @NotEmpty(message = "类型名称->不可为空")
    private String typeName;

    /**
     * 备注信息
     */
    @NotEmpty(message = "备注信息->不可为空")
    private String remarkData;

    /**
     * 类型状态
     */
    @NotEmpty(message = "类型状态->不可为空")
    private Integer dataStatus;

}
