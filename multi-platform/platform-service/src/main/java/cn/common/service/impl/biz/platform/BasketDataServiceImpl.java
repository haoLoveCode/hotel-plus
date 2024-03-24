package cn.common.service.impl.biz.platform;

import cn.common.enums.BasketHandleTypeEnum;
import cn.common.req.biz.BasketDataAddReq;
import cn.common.req.biz.BasketDataReq;
import cn.common.req.biz.BasketDataUpdateReq;
import cn.common.resp.biz.BasketDataResp;
import cn.common.resp.biz.BasketDataExportResp;
import cn.common.service.biz.platform.BasketDataService;
import cn.common.repository.entity.biz.BasketData;
import cn.common.repository.repository.biz.BasketDataRepository;
import cn.common.service.platform.AuthUserService;
import cn.hutool.extra.validation.ValidationUtil;
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
import pro.skywalking.utils.SnowflakeIdWorker;
import com.google.common.collect.Lists;
import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
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
 * @Description: 购物车信息相关服务方法实现
 * @date 2024-03-06
 */
@Service("basketDataService")
@Slf4j
public class BasketDataServiceImpl implements BasketDataService {

    @Resource
    private BasketDataRepository basketDataRepository;

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
     * 导出购物车信息数据
     * @author: Singer
     * @date 2024-03-06
     * @param pageReq
     * @return java.util.List
     */
    @Override
    public void exportData(BasketDataReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>购物车信息数据导出Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<BasketData> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,BasketData::getCreateTime);
        List<BasketData> pageList = basketDataRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return;
        }
        List<BasketDataExportResp> respList =
            mapperFacade.mapAsList(pageList, BasketDataExportResp.class);
        try {
            String fileName = "导出信息.xlsx";
            ExportExcelHandler.setExportResponse(response,fileName);
            ExcelWriter excelWriter = null;
            try {
                // 这里 需要指定写用哪个class去写
                excelWriter = EasyExcel.write(response.getOutputStream(), BasketDataExportResp.class).build();
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
     * 新增购物车信息
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增购物车信息Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(BasketDataAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增购物车信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String mainId = SnowflakeIdWorker.uniqueMainId();
        String authUserId = authUserService.currentAuthUserId();
        String salesItemId = addReq.getSalesItemId();
        String authAppUserId = addReq.getAuthAppUserId();
        Integer itemNum = addReq.getItemNum();
        Integer bizType = addReq.getBizType();
        BasketData entity = basketDataRepository.selectOne(new LambdaQueryWrapper<BasketData>()
                .eq(BasketData::getSalesItemId, salesItemId)
                .eq(BasketData::getAuthAppUserId, authAppUserId));
        if(CheckParam.isNull(entity)){
            entity = mapperFacade.map(addReq, BasketData.class);
            try {
                BaseUtil.setFieldValueNotNull(entity);
                entity.setBasketDataId(mainId);
                entity.setOperatorId(authUserId);
            } catch (Exception e) {
                log.error("新增购物车信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
                throw new BusinessException(ErrorCode.ERROR.getCode(),
                        ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
            }
            basketDataRepository.insert(entity);
            return;
        }
        //增加
        if(bizType.compareTo(BasketHandleTypeEnum.ADD.getCode()) == 0){
            entity.setItemNum(entity.getItemNum()+itemNum);
        }
        //减少
        if(bizType.compareTo(BasketHandleTypeEnum.MINUS.getCode()) == 0){
            if(itemNum >= entity.getItemNum()){
                entity.setItemNum(0);
            }else{
                entity.setItemNum(entity.getItemNum()-itemNum);
            }
        }
        basketDataRepository.updateById(entity);
    }

    /**
     * 批量删除购物车信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的购物车信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<BasketData> entityList = basketDataRepository.selectList(
            new LambdaQueryWrapper<BasketData>().in(BasketData::getBasketDataId,mainIdList));
        entityList.stream().forEach(item -> {
            basketDataRepository.deleteById(item);
        });
    }

    /**
     * 查询购物车信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @Override
    public List<BasketDataResp> queryBasketData(BasketDataReq req){
        log.info(">>>>>>>>>>>>>>>>>查询购物车信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<BasketData> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,BasketData::getCreateTime);
        List<BasketData> entityList = basketDataRepository.selectList(wrapper);
        if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,BasketDataResp.class);
    }

    /**
     * 查询单个购物车信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @Override
    public BasketDataResp queryOneBasketData(BasketDataReq req){
        log.info(">>>>>>>>>>>>>>>>>查询单个购物车信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<BasketData> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,BasketData::getCreateTime);
        BasketData entity = basketDataRepository.selectOne(wrapper);
        if(CheckParam.isNull(entity)){
            return new BasketDataResp();
        }
        return mapperFacade.map(entity,BasketDataResp.class);
    }

    /**
     * 设置查询条件
     * @author: Singer
     * @date 2024-03-06
     * @param wrapper 查询条件
     * @param req 查询参数
     * @return
     */
    private void setCriteria(LambdaQueryWrapper<BasketData> wrapper,
                        BasketDataReq req){

        if(!CheckParam.isNull(req.getAuthAppUserId())){
            wrapper.like(BasketData::getAuthAppUserId,req.getAuthAppUserId());
        }

        if(!CheckParam.isNull(req.getSalesItemId())){
            wrapper.like(BasketData::getSalesItemId,req.getSalesItemId());
        }

        if(!CheckParam.isNull(req.getRemarkData())){
            wrapper.like(BasketData::getRemarkData,req.getRemarkData());
        }

        if(!CheckParam.isNull(req.getItemNum())){
            wrapper.like(BasketData::getItemNum,req.getItemNum());
        }
    }

    /**
     * 分页查询购物车信息
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询购物车信息Req
     * @return Pagination
     */
    @Override
    public Pagination<BasketDataResp> queryByPage(
        BasketDataReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询购物车信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<BasketData> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,BasketData::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<BasketData> pageList = basketDataRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<BasketDataResp> respList =
            mapperFacade.mapAsList(pageList, BasketDataResp.class);
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
    private void setPageCriteria(LambdaQueryWrapper<BasketData> pageWrapper,
                        BasketDataReq pageReq){

        if(!CheckParam.isNull(pageReq.getAuthAppUserId())){
            pageWrapper.like(BasketData::getAuthAppUserId,pageReq.getAuthAppUserId());
        }

        if(!CheckParam.isNull(pageReq.getSalesItemId())){
            pageWrapper.like(BasketData::getSalesItemId,pageReq.getSalesItemId());
        }

        if(!CheckParam.isNull(pageReq.getRemarkData())){
            pageWrapper.like(BasketData::getRemarkData,pageReq.getRemarkData());
        }

        if(!CheckParam.isNull(pageReq.getItemNum())){
            pageWrapper.like(BasketData::getItemNum,pageReq.getItemNum());
        }
    }

    /**
     * 更新购物车信息
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新购物车信息请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(BasketDataUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新购物车信息请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getBasketDataId();
        //String salesItemId = updateReq.getSalesItemId();
        //String authAppUserId = updateReq.getAuthAppUserId();
        Integer itemNum = updateReq.getItemNum();
        Integer bizType = updateReq.getBizType();
        BasketData result = basketDataRepository.selectOne(new LambdaQueryWrapper<BasketData>()
                    .eq(BasketData::getBasketDataId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        //增加
        if(bizType.compareTo(BasketHandleTypeEnum.ADD.getCode()) == 0){
            result.setItemNum(result.getItemNum()+itemNum);
        }
        //减少
        if(bizType.compareTo(BasketHandleTypeEnum.MINUS.getCode()) == 0){
            if(itemNum >= result.getItemNum()){
                result.setItemNum(0);
            }else{
                result.setItemNum(result.getItemNum()-itemNum);
            }
        }
        basketDataRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(BasketData entity,
        BasketDataUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getAuthAppUserId())){
            entity.setAuthAppUserId(updateReq.getAuthAppUserId());
        }
        if(!CheckParam.isNull(updateReq.getSalesItemId())){
            entity.setSalesItemId(updateReq.getSalesItemId());
        }
        if(!CheckParam.isNull(updateReq.getRemarkData())){
            entity.setRemarkData(updateReq.getRemarkData());
        }
        if(!CheckParam.isNull(updateReq.getItemNum())){
            entity.setItemNum(updateReq.getItemNum());
        }
    }
}
