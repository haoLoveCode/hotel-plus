package cn.common.resp.biz.openBiz;

import com.alibaba.excel.annotation.ExcelProperty;
import pro.skywalking.req.platform.base.BaseResp;
import lombok.Data;
import pro.skywalking.validation.NotEmpty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 商品类型信息返回数据封装类
 * @title: SalesItemTypeResp.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class SalesItemTypeResp extends BaseResp implements Serializable {

    private static final long serialVersionUID = 141279481981751211L;

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
