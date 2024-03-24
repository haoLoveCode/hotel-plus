package cn.common.resp.biz.openBiz;

import pro.skywalking.req.platform.base.BaseResp;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 商品图片信息返回数据封装类
 * @title: SalesItemImgResp.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class SalesItemImgResp extends BaseResp implements Serializable {

    private static final long serialVersionUID = 566694267167006169L;

    /**
     * 业务主键ID->"salesItemImgId"
     */
    private String salesItemImgId;

    /**
     * 商品信息ID
     */
    private String salesItemId;

    /**
     * 淡纹地址
     */
    private String imgUrl;

}
