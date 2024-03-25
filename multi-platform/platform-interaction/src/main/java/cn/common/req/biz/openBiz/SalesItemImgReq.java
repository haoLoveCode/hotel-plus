package cn.common.req.biz.openBiz;

import lombok.Data;
import pro.skywalking.req.base.BasePageReq;
import java.io.Serializable;

/**
 * 商品图片信息分页查询请求封装类
 * @title: SalesItemImgReq.java
 * @author Singer
 * @date 2024-03-25 11:13
 */
@Data
public class SalesItemImgReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = -153432441344157645L;

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
