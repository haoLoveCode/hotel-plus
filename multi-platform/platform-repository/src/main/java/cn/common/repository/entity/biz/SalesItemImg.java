package cn.common.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;


/**
 * 商品图片信息实体
 * @title: SalesItemImg.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
@TableName("sales_item_img")
public class SalesItemImg extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -608217028816341574L;

    /**
     * 业务主键ID
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
