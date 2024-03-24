package cn.common.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;


/**
 * 房间类型信息实体
 * @title: RoomType.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
@TableName("room_type")
public class RoomType extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 851158774279046643L;

    /**
     * 业务主键ID
     */
    private String roomTypeId;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 备注信息
     */
    private String remarkData;

}
