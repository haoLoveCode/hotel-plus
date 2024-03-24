package cn.common.service.biz.platform;


import cn.common.req.platform.MainSwiperAddReq;
import cn.common.req.platform.MainSwiperReq;
import cn.common.req.platform.MainSwiperUpdateReq;
import cn.common.resp.platform.MainSwiperResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;

import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service.biz
 * @Description: 首页轮播图相关服务
 * @date 2024-03-06
 */
public interface MainSwiperService {

    /**
     * 新增
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增Req
     */
    void addItem(MainSwiperAddReq addReq);

    /**
     * 批量删除信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询所有信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    List<MainSwiperResp> queryAllMainSwiper();

    /**
     * 分页查询
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    Pagination<MainSwiperResp> queryByPage(
        MainSwiperReq pageReq);

    /**
     * 更新
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新请求参数
     */
    void updateItem(MainSwiperUpdateReq updateReq);

}
