package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.openBiz.SalesItemImgAddReq;
import cn.common.req.biz.openBiz.SalesItemImgReq;
import cn.common.req.biz.openBiz.SalesItemImgUpdateReq;
import cn.common.resp.biz.openBiz.SalesItemImgResp;
import cn.common.service.biz.SalesItemImgService;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.base.ApiResponse;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.interceptor.NeedLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author Singer
 * @packageName cn.common.api.controller
 * @Description: 商品图片信息前端控制器
 * @date 2024-03-06
 */
@RestController
@RequestMapping(value = "api/v1/salesItemImg")
@Slf4j
public class SalesItemImgController extends BaseApiController {

    @Resource
    private SalesItemImgService salesItemImgService;

    /**
     * 新增商品图片信息
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增商品图片信息Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增商品图片信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid SalesItemImgAddReq addReq){
        salesItemImgService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 导出商品图片信息数据
     * @author: Singer
     * @date 2024-03-06
     * @param pageReq
     * @return java.util.List
    */
    @PostMapping(value = "/exportData")
    @ApiLog(value = "商品图片信息数据导出")
    @NeedLogin()
    public void exportData(@RequestBody(required = false) @Valid SalesItemImgReq pageReq){
        salesItemImgService.exportData(pageReq);
    }

    /**
     * 主键ID集合批量删除商品图片信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的商品图片信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除商品图片信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        salesItemImgService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新商品图片信息
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新商品图片信息请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新商品图片信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid SalesItemImgUpdateReq updateReq){
        salesItemImgService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询商品图片信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/querySalesItemImg")
    @ApiLog(value = "查询商品图片信息")
    @NeedLogin()
    public ApiResponse<List<SalesItemImgResp>> querySalesItemImg(
        @RequestBody @Valid SalesItemImgReq req){
        return apiResponse(salesItemImgService.querySalesItemImg(req));
    }

    /**
     * 查询单个商品图片信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryOneSalesItemImg")
    @ApiLog(value = "查询商品图片信息")
    @NeedLogin()
    public ApiResponse<SalesItemImgResp> queryOneSalesItemImg(
        @RequestBody @Valid SalesItemImgReq req){
        return apiResponse(salesItemImgService.queryOneSalesItemImg(req));
    }

    /**
     * 分页查询商品图片信息
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询商品图片信息Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询商品图片信息")
    @NeedLogin()
    public ApiResponse<Pagination<SalesItemImgResp>> queryByPage(
        @RequestBody @Valid SalesItemImgReq pageReq){
        return apiResponse(salesItemImgService.queryByPage(pageReq));
    }
}
