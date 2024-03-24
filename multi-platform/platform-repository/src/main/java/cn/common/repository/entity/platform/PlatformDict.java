package cn.common.repository.entity.platform;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;


/**
* 字典信息实体
* @title: PlatformDict.java
* @author create by Singer - Singer email:singer-coder@qq.com
* @date 2024-03-06 11:13
*/
@Data
@TableName("platform_dict")
public class PlatformDict extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 2513084942873169004L;

    /**
     * 业务主键ID
     */
    private String platformDictId;

    /**
     * 业务字典类型
     */
    private String dictType;

    /**
     * 键
     */
    private String dictKey;

    /**
     * 值
     */
    private String dictValue;

    /**
     * 排序字段
     */
    private Integer sortIndex;

    /**
     * 字典备注
     */
    private String dictRemark;

}
