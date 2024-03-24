package cn.common.req.biz.openBiz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 商品图片信息新增Req
 * @title: SalesItemImgAddReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class SalesItemImgAddReq implements Serializable {

    private static final long serialVersionUID = 319917373417983630L;

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
