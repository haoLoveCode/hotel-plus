package cn.common.utils.interaction;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName animal-plus
 * @Description: 腾讯位置服务数据
 * @date 2024-03-25
 */
@Data
public class TcPlaceData implements Serializable {

    private static final long serialVersionUID = -3899071107776033380L;

    private String id;

    private String title;

    private String address;

    private String category;

    private int type;

    private TcLocation location;

    private long adcode;

    private String province;

    private String city;

    private String district;

}
