package cn.common.service.impl.biz.platform;

import cn.common.req.biz.OrderAddressAddReq;
import cn.common.req.biz.OrderAddressReq;
import cn.common.req.biz.OrderAddressUpdateReq;
import cn.common.resp.biz.OrderAddressResp;
import cn.common.resp.biz.OrderAddressExportResp;
import cn.common.service.biz.platform.OrderAddressService;
import cn.common.repository.entity.biz.OrderAddress;
import cn.common.repository.repository.biz.OrderAddressRepository;
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
 * @Description: 订单-收货地址关联信息相关服务方法实现
 * @date 2024-03-04
 */
@Service("orderAddressService")
@Slf4j
public class OrderAddressServiceImpl implements OrderAddressService {

    @Resource
    private OrderAddressRepository orderAddressRepository;

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
     * 导出订单-收货地址关联信息数据
     * @author: Singer
     * @date 2024-03-04
     * @param pageReq
     * @return java.util.List
     */
    @Override
    public void exportData(OrderAddressReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>订单-收货地址关联信息数据导出Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<OrderAddress> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,OrderAddress::getCreateTime);
        List<OrderAddress> pageList = orderAddressRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return;
        }
        List<OrderAddressExportResp> respList =
            mapperFacade.mapAsList(pageList, OrderAddressExportResp.class);
        try {
            String fileName = "导出信息.xlsx";
            ExportExcelHandler.setExportResponse(response,fileName);
            ExcelWriter excelWriter = null;
            try {
                // 这里 需要指定写用哪个class去写
                excelWriter = EasyExcel.write(response.getOutputStream(), OrderAddressExportResp.class).build();
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
     * 新增订单-收货地址关联信息
     * @author: Singer
     * @date 2024-03-04
     * @param addReq 新增订单-收货地址关联信息Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(OrderAddressAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增订单-收货地址关联信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String mainId = SnowflakeIdWorker.uniqueMainId();
        String authUserId = authUserService.currentAuthUserId();
        OrderAddress entity = mapperFacade.map(addReq, OrderAddress.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setOrderAddressId(mainId);
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增订单-收货地址关联信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        orderAddressRepository.insert(entity);
    }

    /**
     * 批量删除订单-收货地址关联信息信息
     * @author: Singer
     * @date 2024-03-04
     * @param req 需要被删除的订单-收货地址关联信息信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<OrderAddress> entityList = orderAddressRepository.selectList(
            new LambdaQueryWrapper<OrderAddress>().in(OrderAddress::getOrderAddressId,mainIdList));
        entityList.stream().forEach(item -> {
            orderAddressRepository.deleteById(item);
        });
    }

    /**
     * 查询订单-收货地址关联信息信息
     * @author: Singer
     * @date 2024-03-04
     * @param
     * @return java.util.List
     */
    @Override
    public List<OrderAddressResp> queryOrderAddress(OrderAddressReq req){
        log.info(">>>>>>>>>>>>>>>>>查询订单-收货地址关联信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<OrderAddress> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,OrderAddress::getCreateTime);
        List<OrderAddress> entityList = orderAddressRepository.selectList(wrapper);
        if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,OrderAddressResp.class);
    }

    /**
     * 查询单个订单-收货地址关联信息信息
     * @author: Singer
     * @date 2024-03-04
     * @param
     * @return java.util.List
     */
    @Override
    public OrderAddressResp queryOneOrderAddress(OrderAddressReq req){
        log.info(">>>>>>>>>>>>>>>>>查询单个订单-收货地址关联信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<OrderAddress> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,OrderAddress::getCreateTime);
        OrderAddress entity = orderAddressRepository.selectOne(wrapper);
        if(CheckParam.isNull(entity)){
            return new OrderAddressResp();
        }
        return mapperFacade.map(entity,OrderAddressResp.class);
    }

    /**
     * 设置查询条件
     * @author: Singer
     * @date 2024-03-04
     * @param wrapper 查询条件
     * @param req 查询参数
     * @return
     */
    private void setCriteria(LambdaQueryWrapper<OrderAddress> wrapper,
                        OrderAddressReq req){

        if(!CheckParam.isNull(req.getOrderId())){
            wrapper.like(OrderAddress::getOrderId,req.getOrderId());
        }

        if(!CheckParam.isNull(req.getAuthAppUserId())){
            wrapper.like(OrderAddress::getAuthAppUserId,req.getAuthAppUserId());
        }

        if(!CheckParam.isNull(req.getTakeAddressId())){
            wrapper.like(OrderAddress::getTakeAddressId,req.getTakeAddressId());
        }
    }

    /**
     * 分页查询订单-收货地址关联信息
     * @author: Singer
     * @date 2024-03-04
     * @param  pageReq 分页查询订单-收货地址关联信息Req
     * @return Pagination
     */
    @Override
    public Pagination<OrderAddressResp> queryByPage(
        OrderAddressReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询订单-收货地址关联信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<OrderAddress> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,OrderAddress::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<OrderAddress> pageList = orderAddressRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<OrderAddressResp> respList =
            mapperFacade.mapAsList(pageList, OrderAddressResp.class);
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
     * @date 2024-03-04
     * @param pageWrapper 查询条件
     * @param pageReq 查询参数
     * @return
     */
    private void setPageCriteria(LambdaQueryWrapper<OrderAddress> pageWrapper,
                        OrderAddressReq pageReq){

        if(!CheckParam.isNull(pageReq.getOrderId())){
            pageWrapper.like(OrderAddress::getOrderId,pageReq.getOrderId());
        }

        if(!CheckParam.isNull(pageReq.getAuthAppUserId())){
            pageWrapper.like(OrderAddress::getAuthAppUserId,pageReq.getAuthAppUserId());
        }

        if(!CheckParam.isNull(pageReq.getTakeAddressId())){
            pageWrapper.like(OrderAddress::getTakeAddressId,pageReq.getTakeAddressId());
        }
    }

    /**
     * 更新订单-收货地址关联信息
     * @author: Singer
     * @date 2024-03-04
     * @param updateReq 更新订单-收货地址关联信息请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(OrderAddressUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新订单-收货地址关联信息请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getOrderAddressId();
        OrderAddress result = orderAddressRepository.selectOne(new LambdaQueryWrapper<OrderAddress>()
                    .eq(OrderAddress::getOrderAddressId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        orderAddressRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024-03-04
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(OrderAddress entity,
        OrderAddressUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getOrderId())){
            entity.setOrderId(updateReq.getOrderId());
        }
        if(!CheckParam.isNull(updateReq.getAuthAppUserId())){
            entity.setAuthAppUserId(updateReq.getAuthAppUserId());
        }
        if(!CheckParam.isNull(updateReq.getTakeAddressId())){
            entity.setTakeAddressId(updateReq.getTakeAddressId());
        }
    }
}
