package cn.common.service.biz.app;


import cn.common.resp.platform.MainSwiperResp;

import java.util.List;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName dining-platform
 * @Description: 轮播图相关接口
 * @date 2024-03-25
 */
public interface AppMainSwiperService {

    /**
     * 查询所有信息
     * @author: Singer
     * @date 2024-03-25
     * @param
     * @return java.util.List
     */
    List<MainSwiperResp> queryAllMainSwiper();
}
