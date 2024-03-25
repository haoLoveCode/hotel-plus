package cn.common.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;


/**
 * 首页轮播图实体
 * @title: MainSwiper.java
 * @author Singer
 * @date 2024-03-25 11:13
 */
@Data
@TableName("main_swiper")
public class MainSwiper extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 5059061052946173547L;

    /**
     * 业务主键ID
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
