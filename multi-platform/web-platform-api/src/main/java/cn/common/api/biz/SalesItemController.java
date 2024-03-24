package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.openBiz.SalesItemAddReq;
import cn.common.req.biz.openBiz.SalesItemReq;
import cn.common.req.biz.openBiz.SalesItemUpdateReq;
import cn.common.resp.biz.openBiz.SalesItemResp;
import cn.common.service.biz.platform.SalesItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.interceptor.NeedLogin;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.base.ApiResponse;
import pro.skywalking.resp.page.Pagination;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.api.controller
 * @Description: 商品信息前端控制器
 * @date 2024-03-06
 */
@RestController
@RequestMapping(value = "api/v1/salesItem")
@Slf4j
public class SalesItemController extends BaseApiController {

    @Resource
    private SalesItemService salesItemService;

    /**
     * 新增商品信息
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增商品信息Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增商品信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid SalesItemAddReq addReq){
        salesItemService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 导出商品信息数据
     * @author: Singer
     * @date 2024-03-06
     * @param pageReq
     * @return java.util.List
    */
    @PostMapping(value = "/exportData")
    @ApiLog(value = "商品信息数据导出")
    @NeedLogin()
    public void exportData(@RequestBody(required = false) @Valid SalesItemReq pageReq){
        salesItemService.exportData(pageReq);
    }

    /**
     * 主键ID集合批量删除商品信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的商品信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除商品信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        salesItemService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新商品信息
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新商品信息请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新商品信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid SalesItemUpdateReq updateReq){
        salesItemService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询商品信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/querySalesItem")
    @ApiLog(value = "查询商品信息")
    @NeedLogin()
    public ApiResponse<List<SalesItemResp>> querySalesItem(
        @RequestBody @Valid SalesItemReq req){
        return apiResponse(salesItemService.querySalesItem(req));
    }

    /**
     * 查询单个商品信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryOneSalesItem")
    @ApiLog(value = "查询商品信息")
    @NeedLogin()
    public ApiResponse<SalesItemResp> queryOneSalesItem(
        @RequestBody @Valid SalesItemReq req){
        return apiResponse(salesItemService.queryOneSalesItem(req));
    }

    /**
     * 分页查询商品信息
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询商品信息Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询商品信息")
    @NeedLogin()
    public ApiResponse<Pagination<SalesItemResp>> queryByPage(
        @RequestBody @Valid SalesItemReq pageReq){
        return apiResponse(salesItemService.queryByPage(pageReq));
    }
}
