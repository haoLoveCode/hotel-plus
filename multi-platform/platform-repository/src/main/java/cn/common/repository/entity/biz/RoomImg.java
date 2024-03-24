package cn.common.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;


/**
 * 房间图片信息实体
 * @title: RoomImg.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
@TableName("room_img")
public class RoomImg extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -513648399688999777L;

    /**
     * 业务主键ID
     */
    private String roomImgId;

    /**
     * 业务主键ID
     */
    private String salesItemImgId;

    /**
     * 房间信息ID
     */
    private String roomDataId;

    /**
     * 访问地址
     */
    private String imgUrl;

}
