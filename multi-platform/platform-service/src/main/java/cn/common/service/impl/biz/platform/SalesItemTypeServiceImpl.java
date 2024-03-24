package cn.common.service.impl.biz.platform;

import cn.common.repository.entity.biz.SalesItemType;
import cn.common.repository.repository.biz.SalesItemTypeRepository;
import cn.common.req.biz.openBiz.SalesItemTypeAddReq;
import cn.common.req.biz.openBiz.SalesItemTypeReq;
import cn.common.req.biz.openBiz.SalesItemTypeUpdateReq;
import cn.common.resp.biz.openBiz.SalesItemTypeExportResp;
import cn.common.resp.biz.openBiz.SalesItemTypeResp;
import cn.common.service.biz.platform.SalesItemTypeService;
import cn.common.service.platform.AuthUserService;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 商品类型信息相关服务方法实现
 * @date 2024-03-06
 */
@Service("salesItemTypeService")
@Slf4j
public class SalesItemTypeServiceImpl implements SalesItemTypeService {

    @Resource
    private SalesItemTypeRepository salesItemTypeRepository;

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
     * 导出商品类型信息数据
     * @author: Singer
     * @date 2024-03-06
     * @param pageReq
     * @return java.util.List
     */
    @Override
    public void exportData(SalesItemTypeReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>商品类型信息数据导出Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<SalesItemType> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,SalesItemType::getCreateTime);
        List<SalesItemType> pageList = salesItemTypeRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return;
        }
        List<SalesItemTypeExportResp> respList =
            mapperFacade.mapAsList(pageList, SalesItemTypeExportResp.class);
        try {
            String fileName = "导出信息.xlsx";
            ExportExcelHandler.setExportResponse(response,fileName);
            ExcelWriter excelWriter = null;
            try {
                // 这里 需要指定写用哪个class去写
                excelWriter = EasyExcel.write(response.getOutputStream(), SalesItemTypeExportResp.class).build();
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
     * 新增商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增商品类型信息Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(SalesItemTypeAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增商品类型信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String mainId = SnowflakeIdWorker.uniqueMainId();
        String authUserId = authUserService.currentAuthUserId();
        SalesItemType entity = mapperFacade.map(addReq, SalesItemType.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setSalesItemTypeId(mainId);
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增商品类型信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        salesItemTypeRepository.insert(entity);
    }

    /**
     * 批量删除商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的商品类型信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<SalesItemType> entityList = salesItemTypeRepository.selectList(
            new LambdaQueryWrapper<SalesItemType>().in(SalesItemType::getSalesItemTypeId,mainIdList));
        entityList.stream().forEach(item -> {
            salesItemTypeRepository.deleteById(item);
        });
    }

    /**
     * 查询商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @Override
    public List<SalesItemTypeResp> querySalesItemType(SalesItemTypeReq req){
        log.info(">>>>>>>>>>>>>>>>>查询商品类型信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<SalesItemType> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,SalesItemType::getCreateTime);
        List<SalesItemType> entityList = salesItemTypeRepository.selectList(wrapper);
        if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,SalesItemTypeResp.class);
    }

    /**
     * 查询单个商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @Override
    public SalesItemTypeResp queryOneSalesItemType(SalesItemTypeReq req){
        log.info(">>>>>>>>>>>>>>>>>查询单个商品类型信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<SalesItemType> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,SalesItemType::getCreateTime);
        SalesItemType entity = salesItemTypeRepository.selectOne(wrapper);
        if(CheckParam.isNull(entity)){
            return new SalesItemTypeResp();
        }
        return mapperFacade.map(entity,SalesItemTypeResp.class);
    }

    /**
     * 设置查询条件
     * @author: Singer
     * @date 2024-03-06
     * @param wrapper 查询条件
     * @param req 查询参数
     * @return
     */
    private void setCriteria(LambdaQueryWrapper<SalesItemType> wrapper,
                        SalesItemTypeReq req){

        if(!CheckParam.isNull(req.getTypeName())){
            wrapper.like(SalesItemType::getTypeName,req.getTypeName());
        }

        if(!CheckParam.isNull(req.getRemarkData())){
            wrapper.like(SalesItemType::getRemarkData,req.getRemarkData());
        }

        if(!CheckParam.isNull(req.getDataStatus())){
            wrapper.like(SalesItemType::getDataStatus,req.getDataStatus());
        }
    }

    /**
     * 分页查询商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询商品类型信息Req
     * @return Pagination
     */
    @Override
    public Pagination<SalesItemTypeResp> queryByPage(
        SalesItemTypeReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询商品类型信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<SalesItemType> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,SalesItemType::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<SalesItemType> pageList = salesItemTypeRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<SalesItemTypeResp> respList =
            mapperFacade.mapAsList(pageList, SalesItemTypeResp.class);
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
    private void setPageCriteria(LambdaQueryWrapper<SalesItemType> pageWrapper,
                        SalesItemTypeReq pageReq){

        if(!CheckParam.isNull(pageReq.getTypeName())){
            pageWrapper.like(SalesItemType::getTypeName,pageReq.getTypeName());
        }

        if(!CheckParam.isNull(pageReq.getRemarkData())){
            pageWrapper.like(SalesItemType::getRemarkData,pageReq.getRemarkData());
        }

        if(!CheckParam.isNull(pageReq.getDataStatus())){
            pageWrapper.like(SalesItemType::getDataStatus,pageReq.getDataStatus());
        }
    }

    /**
     * 更新商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新商品类型信息请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(SalesItemTypeUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新商品类型信息请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getSalesItemTypeId();
        SalesItemType result = salesItemTypeRepository.selectOne(new LambdaQueryWrapper<SalesItemType>()
                    .eq(SalesItemType::getSalesItemTypeId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        salesItemTypeRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(SalesItemType entity,
        SalesItemTypeUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getTypeName())){
            entity.setTypeName(updateReq.getTypeName());
        }
        if(!CheckParam.isNull(updateReq.getRemarkData())){
            entity.setRemarkData(updateReq.getRemarkData());
        }
        if(!CheckParam.isNull(updateReq.getDataStatus())){
            entity.setDataStatus(updateReq.getDataStatus());
        }
    }
}
