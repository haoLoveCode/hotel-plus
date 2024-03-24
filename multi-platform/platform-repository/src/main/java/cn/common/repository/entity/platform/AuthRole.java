package cn.common.repository.entity.platform;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;
import pro.skywalking.utils.SnowflakeIdWorker;

import java.io.Serializable;


/**
* 系统角色实体
* @title: AuthRole.java
* @author create by Singer - Singer email:singer-coder@qq.com
* @date 2024-03-06 11:13
*/
@Data
@TableName("auth_role")
public class AuthRole extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3430644136167481629L;

    /**
     * 业务主键ID
     */
    private String authRoleId = SnowflakeIdWorker.uniqueMainId();

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 描述信息
     */
    private String roleRemark;

}
