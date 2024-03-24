package cn.common.req.biz.openBiz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品图片信息请求数据封装类
 * @title: SalesItemImgUpdateReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class SalesItemImgUpdateReq implements Serializable {

    private static final long serialVersionUID = -416657822164627853L;

    /**
     * 主键ID->ID
     */
    private Long id;

    /**
     * 业务主键ID->"salesItemImgId"
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String salesItemImgId;

    /**
     * 商品信息ID
     */
    @NotEmpty(message = "商品信息ID->不可为空")
    private String salesItemId;

    /**
     * 淡纹地址
     */
    @NotEmpty(message = "淡纹地址->不可为空")
    private String imgUrl;

}
