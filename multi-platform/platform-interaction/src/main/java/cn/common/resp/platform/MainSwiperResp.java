package cn.common.resp.platform;

import lombok.Data;
import pro.skywalking.req.platform.base.BaseResp;

import java.io.Serializable;

/**
 * 首页轮播图返回数据封装类
 * @title: MainSwiperResp.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class MainSwiperResp extends BaseResp implements Serializable {

    private static final long serialVersionUID = 7832387214728168472L;

    /**
     * 业务主键ID->"mainSwiperId"
     */
    private String mainSwiperId;

    /**
     * 图片地址
     */
    private String mainUrl;

    /**
     * 标题
     */
    private String mainTitle;

    /**
     * 跳转地址
     */
    private String routerUrl;

    /**
     * 关联ID
     */
    private String itemId;

}
