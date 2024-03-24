package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.openBiz.SalesItemTypeAddReq;
import cn.common.req.biz.openBiz.SalesItemTypeReq;
import cn.common.req.biz.openBiz.SalesItemTypeUpdateReq;
import cn.common.resp.biz.openBiz.SalesItemTypeResp;
import cn.common.service.biz.platform.SalesItemTypeService;
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
 * @Description: 商品类型信息前端控制器
 * @date 2024-03-06
 */
@RestController
@RequestMapping(value = "api/v1/salesItemType")
@Slf4j
public class SalesItemTypeController extends BaseApiController {

    @Resource
    private SalesItemTypeService salesItemTypeService;

    /**
     * 新增商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增商品类型信息Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增商品类型信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid SalesItemTypeAddReq addReq){
        salesItemTypeService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 导出商品类型信息数据
     * @author: Singer
     * @date 2024-03-06
     * @param pageReq
     * @return java.util.List
    */
    @PostMapping(value = "/exportData")
    @ApiLog(value = "商品类型信息数据导出")
    @NeedLogin()
    public void exportData(@RequestBody(required = false) @Valid SalesItemTypeReq pageReq){
        salesItemTypeService.exportData(pageReq);
    }

    /**
     * 主键ID集合批量删除商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的商品类型信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除商品类型信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        salesItemTypeService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新商品类型信息请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新商品类型信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid SalesItemTypeUpdateReq updateReq){
        salesItemTypeService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/querySalesItemType")
    @ApiLog(value = "查询商品类型信息")
    @NeedLogin()
    public ApiResponse<List<SalesItemTypeResp>> querySalesItemType(
        @RequestBody @Valid SalesItemTypeReq req){
        return apiResponse(salesItemTypeService.querySalesItemType(req));
    }

    /**
     * 查询单个商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryOneSalesItemType")
    @ApiLog(value = "查询商品类型信息")
    @NeedLogin()
    public ApiResponse<SalesItemTypeResp> queryOneSalesItemType(
        @RequestBody @Valid SalesItemTypeReq req){
        return apiResponse(salesItemTypeService.queryOneSalesItemType(req));
    }

    /**
     * 分页查询商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询商品类型信息Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询商品类型信息")
    @NeedLogin()
    public ApiResponse<Pagination<SalesItemTypeResp>> queryByPage(
        @RequestBody @Valid SalesItemTypeReq pageReq){
        return apiResponse(salesItemTypeService.queryByPage(pageReq));
    }
}
