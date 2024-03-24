package cn.common.service.impl.biz.platform;

import cn.common.repository.entity.biz.RoomImg;
import cn.common.repository.repository.biz.RoomImgRepository;
import cn.common.req.biz.RoomImgAddReq;
import cn.common.req.biz.RoomImgReq;
import cn.common.req.biz.RoomImgUpdateReq;
import cn.common.resp.biz.RoomImgExportResp;
import cn.common.resp.biz.RoomImgResp;
import cn.common.service.biz.platform.RoomImgService;
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
 * @Description: 房间图片信息相关服务方法实现
 * @date 2024/3/24
 */
@Service("roomImgService")
@Slf4j
public class RoomImgServiceImpl implements RoomImgService {

    @Resource
    private RoomImgRepository roomImgRepository;

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
     * 导出房间图片信息数据
     * @author: Singer
     * @date 2024/3/24
     * @param pageReq
     * @return java.util.List
     */
    @Override
    public void exportData(RoomImgReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>房间图片信息数据导出Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<RoomImg> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,RoomImg::getCreateTime);
        List<RoomImg> pageList = roomImgRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return;
        }
        List<RoomImgExportResp> respList =
            mapperFacade.mapAsList(pageList, RoomImgExportResp.class);
        try {
            String fileName = "导出信息.xlsx";
            ExportExcelHandler.setExportResponse(response,fileName);
            ExcelWriter excelWriter = null;
            try {
                // 这里 需要指定写用哪个class去写
                excelWriter = EasyExcel.write(response.getOutputStream(), RoomImgExportResp.class).build();
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
     * 新增房间图片信息
     * @author: Singer
     * @date 2024/3/24
     * @param addReq 新增房间图片信息Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(RoomImgAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增房间图片信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String mainId = SnowflakeIdWorker.uniqueMainId();
        String authUserId = authUserService.currentAuthUserId();
        RoomImg entity = mapperFacade.map(addReq, RoomImg.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setRoomImgId(mainId);
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增房间图片信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        roomImgRepository.insert(entity);
    }

    /**
     * 批量删除房间图片信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 需要被删除的房间图片信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<RoomImg> entityList = roomImgRepository.selectList(
            new LambdaQueryWrapper<RoomImg>().in(RoomImg::getRoomImgId,mainIdList));
        entityList.stream().forEach(item -> {
            roomImgRepository.deleteById(item);
        });
    }

    /**
     * 查询房间图片信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @Override
    public List<RoomImgResp> queryRoomImg(RoomImgReq req){
        log.info(">>>>>>>>>>>>>>>>>查询房间图片信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<RoomImg> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,RoomImg::getCreateTime);
        List<RoomImg> entityList = roomImgRepository.selectList(wrapper);
        if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,RoomImgResp.class);
    }

    /**
     * 查询单个房间图片信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @Override
    public RoomImgResp queryOneRoomImg(RoomImgReq req){
        log.info(">>>>>>>>>>>>>>>>>查询单个房间图片信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<RoomImg> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,RoomImg::getCreateTime);
        RoomImg entity = roomImgRepository.selectOne(wrapper);
        if(CheckParam.isNull(entity)){
            return new RoomImgResp();
        }
        return mapperFacade.map(entity,RoomImgResp.class);
    }

    /**
     * 设置查询条件
     * @author: Singer
     * @date 2024/3/24
     * @param wrapper 查询条件
     * @param req 查询参数
     * @return
     */
    private void setCriteria(LambdaQueryWrapper<RoomImg> wrapper,
                        RoomImgReq req){

        if(!CheckParam.isNull(req.getRoomDataId())){
            wrapper.like(RoomImg::getRoomDataId,req.getRoomDataId());
        }

        if(!CheckParam.isNull(req.getImgUrl())){
            wrapper.like(RoomImg::getImgUrl,req.getImgUrl());
        }
    }

    /**
     * 分页查询房间图片信息
     * @author: Singer
     * @date 2024/3/24
     * @param  pageReq 分页查询房间图片信息Req
     * @return Pagination
     */
    @Override
    public Pagination<RoomImgResp> queryByPage(
        RoomImgReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询房间图片信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<RoomImg> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,RoomImg::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<RoomImg> pageList = roomImgRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<RoomImgResp> respList =
            mapperFacade.mapAsList(pageList, RoomImgResp.class);
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
    private void setPageCriteria(LambdaQueryWrapper<RoomImg> pageWrapper,
                        RoomImgReq pageReq){

        if(!CheckParam.isNull(pageReq.getRoomDataId())){
            pageWrapper.like(RoomImg::getRoomDataId,pageReq.getRoomDataId());
        }

        if(!CheckParam.isNull(pageReq.getImgUrl())){
            pageWrapper.like(RoomImg::getImgUrl,pageReq.getImgUrl());
        }
    }

    /**
     * 更新房间图片信息
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新房间图片信息请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(RoomImgUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新房间图片信息请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getRoomImgId();
        RoomImg result = roomImgRepository.selectOne(new LambdaQueryWrapper<RoomImg>()
                    .eq(RoomImg::getRoomImgId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        roomImgRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(RoomImg entity,
        RoomImgUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getRoomDataId())){
            entity.setRoomDataId(updateReq.getRoomDataId());
        }
        if(!CheckParam.isNull(updateReq.getImgUrl())){
            entity.setImgUrl(updateReq.getImgUrl());
        }
    }
}
