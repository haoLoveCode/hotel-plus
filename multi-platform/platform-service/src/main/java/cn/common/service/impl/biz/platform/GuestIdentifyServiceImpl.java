package cn.common.service.impl.biz.platform;

import cn.common.req.biz.GuestIdentifyAddReq;
import cn.common.req.biz.GuestIdentifyReq;
import cn.common.req.biz.GuestIdentifyUpdateReq;
import cn.common.resp.biz.GuestIdentifyResp;
import cn.common.resp.biz.GuestIdentifyExportResp;
import cn.common.service.biz.platform.GuestIdentifyService;
import cn.common.repository.entity.biz.GuestIdentify;
import cn.common.repository.repository.biz.GuestIdentifyRepository;
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
 * @Description: 客户身份信息相关服务方法实现
 * @date 2024/3/24
 */
@Service("guestIdentifyService")
@Slf4j
public class GuestIdentifyServiceImpl implements GuestIdentifyService {

    @Resource
    private GuestIdentifyRepository guestIdentifyRepository;

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
     * 导出客户身份信息数据
     * @author: Singer
     * @date 2024/3/24
     * @param pageReq
     * @return java.util.List
     */
    @Override
    public void exportData(GuestIdentifyReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>客户身份信息数据导出Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<GuestIdentify> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,GuestIdentify::getCreateTime);
        List<GuestIdentify> pageList = guestIdentifyRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return;
        }
        List<GuestIdentifyExportResp> respList =
            mapperFacade.mapAsList(pageList, GuestIdentifyExportResp.class);
        try {
            String fileName = "导出信息.xlsx";
            ExportExcelHandler.setExportResponse(response,fileName);
            ExcelWriter excelWriter = null;
            try {
                // 这里 需要指定写用哪个class去写
                excelWriter = EasyExcel.write(response.getOutputStream(), GuestIdentifyExportResp.class).build();
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
     * 新增客户身份信息
     * @author: Singer
     * @date 2024/3/24
     * @param addReq 新增客户身份信息Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(GuestIdentifyAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增客户身份信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String mainId = SnowflakeIdWorker.uniqueMainId();
        String authUserId = authUserService.currentAuthUserId();
        GuestIdentify entity = mapperFacade.map(addReq, GuestIdentify.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setGuestIdentifyId(mainId);
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增客户身份信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        guestIdentifyRepository.insert(entity);
    }

    /**
     * 批量删除客户身份信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 需要被删除的客户身份信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<GuestIdentify> entityList = guestIdentifyRepository.selectList(
            new LambdaQueryWrapper<GuestIdentify>().in(GuestIdentify::getGuestIdentifyId,mainIdList));
        entityList.stream().forEach(item -> {
            guestIdentifyRepository.deleteById(item);
        });
    }

    /**
     * 查询客户身份信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @Override
    public List<GuestIdentifyResp> queryGuestIdentify(GuestIdentifyReq req){
        log.info(">>>>>>>>>>>>>>>>>查询客户身份信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<GuestIdentify> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,GuestIdentify::getCreateTime);
        List<GuestIdentify> entityList = guestIdentifyRepository.selectList(wrapper);
        if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,GuestIdentifyResp.class);
    }

    /**
     * 查询单个客户身份信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @Override
    public GuestIdentifyResp queryOneGuestIdentify(GuestIdentifyReq req){
        log.info(">>>>>>>>>>>>>>>>>查询单个客户身份信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<GuestIdentify> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,GuestIdentify::getCreateTime);
        GuestIdentify entity = guestIdentifyRepository.selectOne(wrapper);
        if(CheckParam.isNull(entity)){
            return new GuestIdentifyResp();
        }
        return mapperFacade.map(entity,GuestIdentifyResp.class);
    }

    /**
     * 设置查询条件
     * @author: Singer
     * @date 2024/3/24
     * @param wrapper 查询条件
     * @param req 查询参数
     * @return
     */
    private void setCriteria(LambdaQueryWrapper<GuestIdentify> wrapper,
                        GuestIdentifyReq req){

        if(!CheckParam.isNull(req.getRealName())){
            wrapper.like(GuestIdentify::getRealName,req.getRealName());
        }

        if(!CheckParam.isNull(req.getIdentifyNo())){
            wrapper.like(GuestIdentify::getIdentifyNo,req.getIdentifyNo());
        }

        if(!CheckParam.isNull(req.getGender())){
            wrapper.like(GuestIdentify::getGender,req.getGender());
        }

        if(!CheckParam.isNull(req.getRemark())){
            wrapper.like(GuestIdentify::getRemark,req.getRemark());
        }
    }

    /**
     * 分页查询客户身份信息
     * @author: Singer
     * @date 2024/3/24
     * @param  pageReq 分页查询客户身份信息Req
     * @return Pagination
     */
    @Override
    public Pagination<GuestIdentifyResp> queryByPage(
        GuestIdentifyReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询客户身份信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<GuestIdentify> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,GuestIdentify::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<GuestIdentify> pageList = guestIdentifyRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<GuestIdentifyResp> respList =
            mapperFacade.mapAsList(pageList, GuestIdentifyResp.class);
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
     * @date 2024/3/24
     * @param pageWrapper 查询条件
     * @param pageReq 查询参数
     * @return
     */
    private void setPageCriteria(LambdaQueryWrapper<GuestIdentify> pageWrapper,
                        GuestIdentifyReq pageReq){

        if(!CheckParam.isNull(pageReq.getRealName())){
            pageWrapper.like(GuestIdentify::getRealName,pageReq.getRealName());
        }

        if(!CheckParam.isNull(pageReq.getIdentifyNo())){
            pageWrapper.like(GuestIdentify::getIdentifyNo,pageReq.getIdentifyNo());
        }

        if(!CheckParam.isNull(pageReq.getGender())){
            pageWrapper.like(GuestIdentify::getGender,pageReq.getGender());
        }

        if(!CheckParam.isNull(pageReq.getRemark())){
            pageWrapper.like(GuestIdentify::getRemark,pageReq.getRemark());
        }
    }

    /**
     * 更新客户身份信息
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新客户身份信息请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(GuestIdentifyUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新客户身份信息请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getGuestIdentifyId();
        GuestIdentify result = guestIdentifyRepository.selectOne(new LambdaQueryWrapper<GuestIdentify>()
                    .eq(GuestIdentify::getGuestIdentifyId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        guestIdentifyRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(GuestIdentify entity,
        GuestIdentifyUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getRealName())){
            entity.setRealName(updateReq.getRealName());
        }
        if(!CheckParam.isNull(updateReq.getIdentifyNo())){
            entity.setIdentifyNo(updateReq.getIdentifyNo());
        }
        if(!CheckParam.isNull(updateReq.getGender())){
            entity.setGender(updateReq.getGender());
        }
        if(!CheckParam.isNull(updateReq.getRemark())){
            entity.setRemark(updateReq.getRemark());
        }
    }
}
