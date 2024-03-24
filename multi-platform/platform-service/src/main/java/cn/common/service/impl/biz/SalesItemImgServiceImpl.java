package cn.common.service.impl.biz;

import cn.common.req.biz.openBiz.SalesItemImgAddReq;
import cn.common.req.biz.openBiz.SalesItemImgReq;
import cn.common.req.biz.openBiz.SalesItemImgUpdateReq;
import cn.common.resp.biz.openBiz.SalesItemImgResp;
import cn.common.resp.biz.openBiz.SalesItemImgExportResp;
import cn.common.service.biz.SalesItemImgService;
import cn.common.repository.entity.biz.SalesItemImg;
import cn.common.repository.repository.biz.SalesItemImgRepository;
import cn.common.service.platform.AuthUserService;
import pro.skywalking.collection.CollectionUtils;
import pro.skywalking.constants.BaseConstant;
import pro.skywalking.enums.ErrorCode;
import pro.skywalking.excel.ExportExcelHandler;
import pro.skywalking.exception.BusinessException;
import pro.skywalking.helper.PageBuilder;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.utils.BaseUtil;
import pro.skywalking.utils.CheckParam;
import pro.skywalking.utils.SnowflakeIdWorker;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 商品图片信息相关服务方法实现
 * @date 2024-03-06
 */
@Service("salesItemImgService")
@Slf4j
public class SalesItemImgServiceImpl implements SalesItemImgService {

    @Resource
    private SalesItemImgRepository salesItemImgRepository;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private BaseConstant baseConstant;

    @Resource
    private AuthUserService authUserService;

    @Resource
    private HttpServletResponse response;

    @Resource
    private HttpServletRequest request;

    /**
     * 导出商品图片信息数据
     * @author: Singer
     * @date 2024-03-06
     * @param pageReq
     * @return java.util.List
     */
    @Override
    public void exportData(SalesItemImgReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>商品图片信息数据导出Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<SalesItemImg> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,SalesItemImg::getCreateTime);
        List<SalesItemImg> pageList = salesItemImgRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return;
        }
        List<SalesItemImgExportResp> respList =
            mapperFacade.mapAsList(pageList, SalesItemImgExportResp.class);
        try {
            String fileName = "导出信息.xlsx";
            ExportExcelHandler.setExportResponse(response,fileName);
            ExcelWriter excelWriter = null;
            try {
                // 这里 需要指定写用哪个class去写
                excelWriter = EasyExcel.write(response.getOutputStream(), SalesItemImgExportResp.class).build();
                // 这里注意 如果同一个sheet只要创建一次
                WriteSheet writeSheet = EasyExcel.writerSheet("sheet").build();
                excelWriter.write(respList,writeSheet);
            } finally {
                // 关闭流
                if (excelWriter != null) {
                    excelWriter.finish();
                }
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>>导出数据异常:{},{}<<<<<<<<<<<<<<<<<<<<<<",e.getMessage(),e);
            ExportExcelHandler.setExportErrorResponse(response);
            Map<String, String> map = Maps.newHashMap();
            try {
                response.getWriter().println(JSON.toJSONString(map));
            } catch (IOException ioException) {
                log.error(">>>>>>>>>>>>>>>>>导出数据发生异常:{},{}<<<<<<<<<<<<<<<<<",e.getMessage(),e);
                ioException.printStackTrace();
            }
        }
    }

    /**
     * 新增商品图片信息
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增商品图片信息Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(SalesItemImgAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增商品图片信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String mainId = SnowflakeIdWorker.uniqueMainId();
        String authUserId = authUserService.currentAuthUserId();
        SalesItemImg entity = mapperFacade.map(addReq, SalesItemImg.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setSalesItemImgId(mainId);
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增商品图片信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        salesItemImgRepository.insert(entity);
    }

    /**
     * 批量删除商品图片信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的商品图片信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<SalesItemImg> entityList = salesItemImgRepository.selectList(
            new LambdaQueryWrapper<SalesItemImg>().in(SalesItemImg::getSalesItemImgId,mainIdList));
        entityList.stream().forEach(item -> {
            salesItemImgRepository.deleteById(item);
        });
    }

    /**
     * 查询商品图片信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @Override
    public List<SalesItemImgResp> querySalesItemImg(SalesItemImgReq req){
        log.info(">>>>>>>>>>>>>>>>>查询商品图片信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<SalesItemImg> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,SalesItemImg::getCreateTime);
        List<SalesItemImg> entityList = salesItemImgRepository.selectList(wrapper);
        if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,SalesItemImgResp.class);
    }

    /**
     * 查询单个商品图片信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @Override
    public SalesItemImgResp queryOneSalesItemImg(SalesItemImgReq req){
        log.info(">>>>>>>>>>>>>>>>>查询单个商品图片信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<SalesItemImg> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,SalesItemImg::getCreateTime);
        SalesItemImg entity = salesItemImgRepository.selectOne(wrapper);
        if(CheckParam.isNull(entity)){
            return new SalesItemImgResp();
        }
        return mapperFacade.map(entity,SalesItemImgResp.class);
    }

    /**
     * 设置查询条件
     * @author: Singer
     * @date 2024-03-06
     * @param wrapper 查询条件
     * @param req 查询参数
     * @return
     */
    private void setCriteria(LambdaQueryWrapper<SalesItemImg> wrapper,
                        SalesItemImgReq req){

        if(!CheckParam.isNull(req.getSalesItemId())){
            wrapper.like(SalesItemImg::getSalesItemId,req.getSalesItemId());
        }

        if(!CheckParam.isNull(req.getImgUrl())){
            wrapper.like(SalesItemImg::getImgUrl,req.getImgUrl());
        }
    }

    /**
     * 分页查询商品图片信息
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询商品图片信息Req
     * @return Pagination
     */
    @Override
    public Pagination<SalesItemImgResp> queryByPage(
        SalesItemImgReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询商品图片信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<SalesItemImg> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,SalesItemImg::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<SalesItemImg> pageList = salesItemImgRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<SalesItemImgResp> respList =
            mapperFacade.mapAsList(pageList, SalesItemImgResp.class);
        Integer startIndex = (pageReq.getItemsPerPage() * pageReq.getCurrentPage()) - pageReq.getItemsPerPage() + 1;
        AtomicInteger idBeginIndex = new AtomicInteger(startIndex);
        respList.stream().forEach(item -> {
            item.setId(Integer.valueOf(idBeginIndex.getAndIncrement()).longValue());
        });
        return PageBuilder.buildPageResult(page,respList);
    }

    /**
     * 设置分页条件
     * @author: Singer
     * @date 2024-03-06
     * @param pageWrapper 查询条件
     * @param pageReq 查询参数
     * @return
     */
    private void setPageCriteria(LambdaQueryWrapper<SalesItemImg> pageWrapper,
                        SalesItemImgReq pageReq){

        if(!CheckParam.isNull(pageReq.getSalesItemId())){
            pageWrapper.like(SalesItemImg::getSalesItemId,pageReq.getSalesItemId());
        }

        if(!CheckParam.isNull(pageReq.getImgUrl())){
            pageWrapper.like(SalesItemImg::getImgUrl,pageReq.getImgUrl());
        }
    }

    /**
     * 更新商品图片信息
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新商品图片信息请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(SalesItemImgUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新商品图片信息请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getSalesItemImgId();
        SalesItemImg result = salesItemImgRepository.selectOne(new LambdaQueryWrapper<SalesItemImg>()
                    .eq(SalesItemImg::getSalesItemImgId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        salesItemImgRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(SalesItemImg entity,
        SalesItemImgUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getSalesItemId())){
            entity.setSalesItemId(updateReq.getSalesItemId());
        }
        if(!CheckParam.isNull(updateReq.getImgUrl())){
            entity.setImgUrl(updateReq.getImgUrl());
        }
    }
}
