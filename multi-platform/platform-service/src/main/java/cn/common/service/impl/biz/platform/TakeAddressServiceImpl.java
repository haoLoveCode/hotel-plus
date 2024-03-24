package cn.common.service.impl.biz.platform;

import cn.common.req.biz.TakeAddressAddReq;
import cn.common.req.biz.TakeAddressReq;
import cn.common.req.biz.TakeAddressUpdateReq;
import cn.common.resp.biz.TakeAddressResp;
import cn.common.resp.biz.TakeAddressExportResp;
import cn.common.service.biz.platform.TakeAddressService;
import cn.common.repository.entity.biz.TakeAddress;
import cn.common.repository.repository.biz.TakeAddressRepository;
import cn.common.service.platform.AuthUserService;
import cn.hutool.extra.validation.ValidationUtil;
import pro.skywalking.collection.CollectionUtils;
import pro.skywalking.constants.BaseConstant;
import pro.skywalking.enums.ErrorCode;
import pro.skywalking.enums.YesOrNoStatusEnum;
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
 * @Description: 收货地址信息相关服务方法实现
 * @date 2024-03-06
 */
@Service("takeAddressService")
@Slf4j
public class TakeAddressServiceImpl implements TakeAddressService {

    @Resource
    private TakeAddressRepository takeAddressRepository;

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
     * 导出收货地址信息数据
     * @author: Singer
     * @date 2024-03-06
     * @param pageReq
     * @return java.util.List
     */
    @Override
    public void exportData(TakeAddressReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>收货地址信息数据导出Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<TakeAddress> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,TakeAddress::getCreateTime);
        List<TakeAddress> pageList = takeAddressRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return;
        }
        List<TakeAddressExportResp> respList =
            mapperFacade.mapAsList(pageList, TakeAddressExportResp.class);
        try {
            String fileName = "导出信息.xlsx";
            ExportExcelHandler.setExportResponse(response,fileName);
            ExcelWriter excelWriter = null;
            try {
                // 这里 需要指定写用哪个class去写
                excelWriter = EasyExcel.write(response.getOutputStream(), TakeAddressExportResp.class).build();
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
     * 新增收货地址信息
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增收货地址信息Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(TakeAddressAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增收货地址信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        Integer defaultStatus = addReq.getDefaultStatus();
        String mainId = SnowflakeIdWorker.uniqueMainId();
        String authUserId = authUserService.currentAuthUserId();
        TakeAddress entity = mapperFacade.map(addReq, TakeAddress.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setTakeAddressId(mainId);
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增收货地址信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        takeAddressRepository.insert(entity);

        //如果当前新增的是默认收货地址，那么其他的收货地址是非默认地址
        if(defaultStatus.compareTo(YesOrNoStatusEnum.YES.getCode()) == 0){
            //如果当前新增的是默认收货地址，那么其他的收货地址是非默认地址
            TakeAddress update = new TakeAddress();
            update.setDefaultStatus(YesOrNoStatusEnum.NO.getCode());
            takeAddressRepository.update(update,
                    new LambdaQueryWrapper<TakeAddress>()
                            .eq(TakeAddress::getDefaultStatus, YesOrNoStatusEnum.YES.getCode()));
        }
    }

    /**
     * 批量删除收货地址信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的收货地址信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<TakeAddress> entityList = takeAddressRepository.selectList(
            new LambdaQueryWrapper<TakeAddress>().in(TakeAddress::getTakeAddressId,mainIdList));
        entityList.stream().forEach(item -> {
            takeAddressRepository.deleteById(item);
        });
    }

    /**
     * 查询收货地址信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @Override
    public List<TakeAddressResp> queryTakeAddress(TakeAddressReq req){
        log.info(">>>>>>>>>>>>>>>>>查询收货地址信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<TakeAddress> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,TakeAddress::getCreateTime);
        List<TakeAddress> entityList = takeAddressRepository.selectList(wrapper);
        if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,TakeAddressResp.class);
    }

    /**
     * 查询单个收货地址信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @Override
    public TakeAddressResp queryOneTakeAddress(TakeAddressReq req){
        log.info(">>>>>>>>>>>>>>>>>查询单个收货地址信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<TakeAddress> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,TakeAddress::getCreateTime);
        TakeAddress entity = takeAddressRepository.selectOne(wrapper);
        if(CheckParam.isNull(entity)){
            return new TakeAddressResp();
        }
        return mapperFacade.map(entity,TakeAddressResp.class);
    }

    /**
     * 设置查询条件
     * @author: Singer
     * @date 2024-03-06
     * @param wrapper 查询条件
     * @param req 查询参数
     * @return
     */
    private void setCriteria(LambdaQueryWrapper<TakeAddress> wrapper,
                        TakeAddressReq req){

        if(!CheckParam.isNull(req.getTakeAddressId())){
            wrapper.eq(TakeAddress::getTakeAddressId,req.getTakeAddressId());
        }

        if(!CheckParam.isNull(req.getAuthAppUserId())){
            wrapper.eq(TakeAddress::getAuthAppUserId,req.getAuthAppUserId());
        }

        if(!CheckParam.isNull(req.getTakerName())){
            wrapper.like(TakeAddress::getTakerName,req.getTakerName());
        }

        if(!CheckParam.isNull(req.getTakerPhone())){
            wrapper.like(TakeAddress::getTakerPhone,req.getTakerPhone());
        }

        if(!CheckParam.isNull(req.getProvinceName())){
            wrapper.like(TakeAddress::getProvinceName,req.getProvinceName());
        }

        if(!CheckParam.isNull(req.getCityName())){
            wrapper.like(TakeAddress::getCityName,req.getCityName());
        }

        if(!CheckParam.isNull(req.getDistrictName())){
            wrapper.like(TakeAddress::getDistrictName,req.getDistrictName());
        }

        if(!CheckParam.isNull(req.getPostCode())){
            wrapper.like(TakeAddress::getPostCode,req.getPostCode());
        }

        if(!CheckParam.isNull(req.getPlaceLongitude())){
            wrapper.eq(TakeAddress::getPlaceLongitude,req.getPlaceLongitude());
        }

        if(!CheckParam.isNull(req.getPlaceLatitude())){
            wrapper.eq(TakeAddress::getPlaceLatitude,req.getPlaceLatitude());
        }

        if(!CheckParam.isNull(req.getAddressDetailed())){
            wrapper.like(TakeAddress::getAddressDetailed,req.getAddressDetailed());
        }

        if(!CheckParam.isNull(req.getDefaultStatus())){
            wrapper.eq(TakeAddress::getDefaultStatus,req.getDefaultStatus());
        }
    }

    /**
     * 分页查询收货地址信息
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询收货地址信息Req
     * @return Pagination
     */
    @Override
    public Pagination<TakeAddressResp> queryByPage(
        TakeAddressReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询收货地址信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<TakeAddress> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,TakeAddress::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<TakeAddress> pageList = takeAddressRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<TakeAddressResp> respList =
            mapperFacade.mapAsList(pageList, TakeAddressResp.class);
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
    private void setPageCriteria(LambdaQueryWrapper<TakeAddress> pageWrapper,
                        TakeAddressReq pageReq){

        if(!CheckParam.isNull(pageReq.getTakeAddressId())){
            pageWrapper.eq(TakeAddress::getTakeAddressId,pageReq.getTakeAddressId());
        }

        if(!CheckParam.isNull(pageReq.getAuthAppUserId())){
            pageWrapper.eq(TakeAddress::getAuthAppUserId,pageReq.getAuthAppUserId());
        }

        if(!CheckParam.isNull(pageReq.getTakerName())){
            pageWrapper.like(TakeAddress::getTakerName,pageReq.getTakerName());
        }

        if(!CheckParam.isNull(pageReq.getTakerPhone())){
            pageWrapper.like(TakeAddress::getTakerPhone,pageReq.getTakerPhone());
        }

        if(!CheckParam.isNull(pageReq.getProvinceName())){
            pageWrapper.like(TakeAddress::getProvinceName,pageReq.getProvinceName());
        }

        if(!CheckParam.isNull(pageReq.getCityName())){
            pageWrapper.like(TakeAddress::getCityName,pageReq.getCityName());
        }

        if(!CheckParam.isNull(pageReq.getDistrictName())){
            pageWrapper.like(TakeAddress::getDistrictName,pageReq.getDistrictName());
        }

        if(!CheckParam.isNull(pageReq.getPostCode())){
            pageWrapper.like(TakeAddress::getPostCode,pageReq.getPostCode());
        }

        if(!CheckParam.isNull(pageReq.getPlaceLongitude())){
            pageWrapper.like(TakeAddress::getPlaceLongitude,pageReq.getPlaceLongitude());
        }

        if(!CheckParam.isNull(pageReq.getPlaceLatitude())){
            pageWrapper.eq(TakeAddress::getPlaceLatitude,pageReq.getPlaceLatitude());
        }

        if(!CheckParam.isNull(pageReq.getAddressDetailed())){
            pageWrapper.like(TakeAddress::getAddressDetailed,pageReq.getAddressDetailed());
        }

        if(!CheckParam.isNull(pageReq.getDefaultStatus())){
            pageWrapper.eq(TakeAddress::getDefaultStatus,pageReq.getDefaultStatus());
        }
    }

    /**
     * 更新收货地址信息
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新收货地址信息请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(TakeAddressUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新收货地址信息请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        Integer defaultStatus = updateReq.getDefaultStatus();
        String mainId = updateReq.getTakeAddressId();
        TakeAddress result = takeAddressRepository.selectOne(new LambdaQueryWrapper<TakeAddress>()
                    .eq(TakeAddress::getTakeAddressId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        takeAddressRepository.updateById(result);
        //如果当前新增的是默认收货地址，那么其他的收货地址是非默认地址
        if(defaultStatus.compareTo(YesOrNoStatusEnum.YES.getCode()) == 0){
            //如果当前新增的是默认收货地址，那么其他的收货地址是非默认地址
            TakeAddress update = new TakeAddress();
            update.setDefaultStatus(YesOrNoStatusEnum.NO.getCode());
            takeAddressRepository.update(update,
                    new LambdaQueryWrapper<TakeAddress>()
                            .eq(TakeAddress::getDefaultStatus, YesOrNoStatusEnum.YES.getCode()));
        }
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(TakeAddress entity,
        TakeAddressUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getAuthAppUserId())){
            entity.setAuthAppUserId(updateReq.getAuthAppUserId());
        }
        if(!CheckParam.isNull(updateReq.getTakerName())){
            entity.setTakerName(updateReq.getTakerName());
        }
        if(!CheckParam.isNull(updateReq.getTakerPhone())){
            entity.setTakerPhone(updateReq.getTakerPhone());
        }
        if(!CheckParam.isNull(updateReq.getProvinceName())){
            entity.setProvinceName(updateReq.getProvinceName());
        }
        if(!CheckParam.isNull(updateReq.getCityName())){
            entity.setCityName(updateReq.getCityName());
        }
        if(!CheckParam.isNull(updateReq.getDistrictName())){
            entity.setDistrictName(updateReq.getDistrictName());
        }
        if(!CheckParam.isNull(updateReq.getPostCode())){
            entity.setPostCode(updateReq.getPostCode());
        }
        if(!CheckParam.isNull(updateReq.getPlaceLongitude())){
            entity.setPlaceLongitude(updateReq.getPlaceLongitude());
        }
        if(!CheckParam.isNull(updateReq.getPlaceLatitude())){
            entity.setPlaceLatitude(updateReq.getPlaceLatitude());
        }
        if(!CheckParam.isNull(updateReq.getAddressDetailed())){
            entity.setAddressDetailed(updateReq.getAddressDetailed());
        }
        if(!CheckParam.isNull(updateReq.getDefaultStatus())){
            entity.setDefaultStatus(updateReq.getDefaultStatus());
        }
    }
}
