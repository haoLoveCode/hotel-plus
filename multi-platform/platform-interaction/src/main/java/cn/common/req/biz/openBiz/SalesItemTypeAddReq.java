package cn.common.req.biz.openBiz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 商品类型信息新增Req
 * @title: SalesItemTypeAddReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class SalesItemTypeAddReq implements Serializable {

    private static final long serialVersionUID = -466655162356997675L;

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
