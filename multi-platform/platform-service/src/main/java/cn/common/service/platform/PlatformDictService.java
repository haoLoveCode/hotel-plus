package cn.common.service.platform;


import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.req.platform.dict.PlatformDictAddReq;
import pro.skywalking.req.platform.dict.PlatformDictReq;
import pro.skywalking.req.platform.dict.PlatformDictUpdateReq;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.resp.platform.dict.PlatformDictResp;

import java.util.List;

/**
* @author Singer create by Singer email:singer-coder@qq.com
* @packageName cn.common.service
* @Description: 字典信息相关服务
* @date 2024-03-25
*/
public interface PlatformDictService {

    /**
     * 新增
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param addReq 新增Req
     */
    void addItem(PlatformDictAddReq addReq);

    /**
     * 批量删除信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param req 需要被删除的信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 根据字典类型查询字典数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param dictType 字典类型
     * @return java.util.List
     */
    List<PlatformDictResp> queryByType(String dictType);

    /**
     * 分页查询
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    Pagination<PlatformDictResp> queryByPage(
        PlatformDictReq pageReq);

    /**
     * 更新
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param updateReq 更新请求参数
     */
    void updateItem(PlatformDictUpdateReq updateReq);

}
