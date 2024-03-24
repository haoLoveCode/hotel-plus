package cn.common.resp.biz.app.auth;

import lombok.Data;
import pro.skywalking.req.platform.base.BaseResp;

import java.io.Serializable;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName multi-platform
 * @Description: 基本的App用户认证返回封装
 * @date 2024-03-06
 */
@Data
public class AuthBaseResp extends BaseResp implements Serializable {

    private static final long serialVersionUID = 3047051882980723L;

    /**
     * 身份证号
     */
    private String identityCard;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 个性签名
     */
    private String signature;


}
