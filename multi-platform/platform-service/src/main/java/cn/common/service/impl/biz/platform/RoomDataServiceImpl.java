package cn.common.service.impl.biz.platform;

import cn.common.repository.entity.biz.RoomBooking;
import cn.common.repository.entity.biz.RoomData;
import cn.common.repository.entity.biz.RoomImg;
import cn.common.repository.repository.biz.RoomDataRepository;
import cn.common.repository.repository.biz.RoomImgRepository;
import cn.common.req.biz.openBiz.roomData.RoomDataAddReq;
import cn.common.req.biz.openBiz.roomData.RoomDataReq;
import cn.common.req.biz.openBiz.roomData.RoomDataUpdateReq;
import cn.common.resp.biz.RoomDataExportResp;
import cn.common.resp.biz.RoomDataResp;
import cn.common.service.biz.platform.RoomDataService;
import cn.common.service.platform.AuthUserService;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
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
import java.util.stream.Collectors;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 房间信息相关服务方法实现
 * @date 2024/3/24
 */
@Service("roomDataService")
@Slf4j
public class RoomDataServiceImpl implements RoomDataService {

    @Resource
    private RoomDataRepository roomDataRepository;

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
     * @param roomBookingId 房间预定ID
     * @return cn.common.repository.entity.biz.RoomData
     * @description: 根据房间预定ID查询房间信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024/3/24
     */
    @Override
    public RoomData queryRoomByBookingUd(String roomBookingId) {
        MPJLambdaWrapper<RoomData> roomWrapper = new MPJLambdaWrapper<>();
        roomWrapper.leftJoin(RoomBooking.class, RoomBooking::getRoomBookingId, RoomData::getRoomDataId);
        roomWrapper.selectAll(RoomData.class);
        roomWrapper.eq(RoomBooking::getRoomBookingId, roomBookingId);
        RoomData roomData = roomDataRepository.selectJoinOne(RoomData.class, roomWrapper);
        return roomData;
    }

    /**
     * 导出房间信息数据
     *
     * @param pageReq
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    @Override
    public void exportData(RoomDataReq pageReq) {
        log.info(">>>>>>>>>>>>>>>>>房间信息数据导出Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<RoomData> pageWrapper = new LambdaQueryWrapper<>();
        setQueryCriteria(pageWrapper, pageReq);
        pageWrapper.orderBy(true, false, RoomData::getCreateTime);
        List<RoomData> pageList = roomDataRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return;
        }
        List<RoomDataExportResp> respList =
                mapperFacade.mapAsList(pageList, RoomDataExportResp.class);
        try {
            String fileName = "导出信息.xlsx";
            ExportExcelHandler.setExportResponse(response, fileName);
            ExcelWriter excelWriter = null;
            try {
                // 这里 需要指定写用哪个class去写
                excelWriter = EasyExcel.write(response.getOutputStream(), RoomDataExportResp.class).build();
                // 这里注意 如果同一个sheet只要创建一次
                WriteSheet writeSheet = EasyExcel.writerSheet("sheet").build();
                excelWriter.write(respList, writeSheet);
            } finally {
                // 关闭流
                if (excelWriter != null) {
                    excelWriter.finish();
                }
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>>导出数据异常:{},{}<<<<<<<<<<<<<<<<<<<<<<", e.getMessage(), e);
            ExportExcelHandler.setExportErrorResponse(response);
            Map<String, String> map = Maps.newHashMap();
            try {
                response.getWriter().println(JSON.toJSONString(map));
            } catch (IOException ioException) {
                log.error(">>>>>>>>>>>>>>>>>导出数据发生异常:{},{}<<<<<<<<<<<<<<<<<", e.getMessage(), e);
                ioException.printStackTrace();
            }
        }
    }

    /**
     * 新增房间信息
     *
     * @param addReq 新增房间信息Req
     * @author: Singer
     * @date 2024/3/24
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addItem(RoomDataAddReq addReq) {
        List<String> imgList = addReq.getImgList();
        if (CollectionUtils.isEmpty(imgList)) {
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "图片信息不可为空");
        }
        log.info(">>>>>>>>>>>>>>>>>新增房间信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String mainId = SnowflakeIdWorker.uniqueMainId();
        String authUserId = authUserService.currentAuthUserId();
        RoomData entity = mapperFacade.map(addReq, RoomData.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setRoomDataId(mainId);
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增房间信息->设置为空的属性失败 {} , {} ", e.getMessage(), e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    ErrorCode.ERROR.getMessage() + StrUtil.COLON + e.getMessage() + StrUtil.COLON + e);
        }
        roomDataRepository.insert(entity);
        imgList.stream().forEach(item -> {
            RoomImg img = new RoomImg();
            img.setRoomImgId(SnowflakeIdWorker.uniqueMainId());
            img.setRoomDataId(mainId);
            img.setImgUrl(item);
            try {
                BaseUtil.setFieldValueNotNull(img);
                img.setOperatorId(authUserId);
            } catch (Exception e) {
                log.error("新增房间图片信息->设置为空的属性失败 {} , {} ", e.getMessage(), e);
                throw new BusinessException(ErrorCode.ERROR.getCode(),
                        ErrorCode.ERROR.getMessage() + StrUtil.COLON + e.getMessage() + StrUtil.COLON + e);
            }
            roomImgRepository.insert(img);
        });
    }

    /**
     * 批量删除房间信息
     *
     * @param req 需要被删除的房间信息
     * @author: Singer
     * @date 2024/3/24
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req) {
        List<String> mainIdList = req.getMainIdList();
        if (CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<RoomData> entityList = roomDataRepository.selectList(
                new LambdaQueryWrapper<RoomData>().in(RoomData::getRoomDataId, mainIdList));
        entityList.stream().forEach(item -> {
            roomImgRepository.delete(new LambdaQueryWrapper<RoomImg>().eq(RoomImg::getRoomDataId, item.getRoomDataId()));
            roomDataRepository.deleteById(item);
        });
    }

    /**
     * 查询房间信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    @Override
    public List<RoomDataResp> queryRoomData(RoomDataReq req) {
        log.info(">>>>>>>>>>>>>>>>>查询房间信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<RoomData> wrapper = new LambdaQueryWrapper<>();
        setQueryCriteria(wrapper, req);
        wrapper.orderBy(true, false, RoomData::getCreateTime);
        List<RoomData> entityList = roomDataRepository.selectList(wrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return Lists.newArrayList();
        }
        List<String> roomDataIdList
                = entityList.stream().map(item -> item.getRoomDataId()).distinct().collect(Collectors.toList());
        List<RoomImg> roomImgList = roomImgRepository.selectList(new LambdaQueryWrapper<RoomImg>()
                .in(RoomImg::getRoomDataId, roomDataIdList));
        Map<String, List<RoomImg>> imgHashMap = roomImgList.stream()
                .collect(Collectors.groupingBy(RoomImg::getRoomDataId));
        List<RoomDataResp> respList = mapperFacade.mapAsList(entityList, RoomDataResp.class);
        respList.stream().forEach(item -> {
            List<RoomImg> imgList = imgHashMap.get(item.getRoomDataId());
            if (!CollectionUtils.isEmpty(imgList)) {
                item.setImgList(imgList.stream().map(img -> img.getImgUrl()).collect(Collectors.toList()));
            }
        });
        return respList;
    }

    /**
     * 查询单个房间信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    @Override
    public RoomDataResp queryOneRoomData(RoomDataReq req) {
        log.info(">>>>>>>>>>>>>>>>>查询单个房间信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<RoomData> wrapper = new LambdaQueryWrapper<>();
        setQueryCriteria(wrapper, req);
        wrapper.orderBy(true, false, RoomData::getCreateTime);
        RoomData entity = roomDataRepository.selectOne(wrapper);
        if (CheckParam.isNull(entity)) {
            return new RoomDataResp();
        }
        List<RoomImg> roomImgList = roomImgRepository.selectList(new LambdaQueryWrapper<RoomImg>()
                .eq(RoomImg::getRoomDataId, entity.getRoomDataId()));
        RoomDataResp resp = mapperFacade.map(entity, RoomDataResp.class);
        if (!CollectionUtils.isEmpty(roomImgList)) {
            resp.setImgList(roomImgList.stream().map(img -> img.getImgUrl()).collect(Collectors.toList()));
        }
        return resp;
    }

    /**
     * 设置查询条件
     *
     * @param wrapper 查询条件
     * @param req     查询参数
     * @return
     * @author: Singer
     * @date 2024/3/24
     */
    private void setQueryCriteria(LambdaQueryWrapper<RoomData> wrapper,
                                  RoomDataReq req) {

        if (!CheckParam.isNull(req.getRoomDataId())) {
            wrapper.eq(RoomData::getRoomDataId, req.getRoomDataId());
        }

        if (!CheckParam.isNull(req.getRoomTypeId())) {
            wrapper.eq(RoomData::getRoomTypeId, req.getRoomTypeId());
        }

        if (!CheckParam.isNull(req.getRoomTitle())) {
            wrapper.like(RoomData::getRoomTitle, req.getRoomTitle());
        }

        if (!CheckParam.isNull(req.getBriefData())) {
            wrapper.like(RoomData::getBriefData, req.getBriefData());
        }

        if (!CheckParam.isNull(req.getRoomNo())) {
            wrapper.like(RoomData::getRoomNo, req.getRoomNo());
        }

        if (!CheckParam.isNull(req.getMainImg())) {
            wrapper.like(RoomData::getMainImg, req.getMainImg());
        }

        if (!CheckParam.isNull(req.getRoomStatus())) {
            wrapper.eq(RoomData::getRoomStatus, req.getRoomStatus());
        }

        if (!CheckParam.isNull(req.getRoomFloor())) {
            wrapper.eq(RoomData::getRoomFloor, req.getRoomFloor());
        }

        if (!CheckParam.isNull(req.getUnitPrice())) {
            wrapper.ge(RoomData::getUnitPrice, req.getUnitPrice());
        }

        if (!CheckParam.isNull(req.getRoomArea())) {
            wrapper.ge(RoomData::getRoomArea, req.getRoomArea());
        }

        if (!CheckParam.isNull(req.getBedNum())) {
            wrapper.ge(RoomData::getBedNum, req.getBedNum());
        }
    }

    /**
     * 分页查询房间信息
     *
     * @param pageReq 分页查询房间信息Req
     * @return Pagination
     * @author: Singer
     * @date 2024/3/24
     */
    @Override
    public Pagination<RoomDataResp> queryByPage(
            RoomDataReq pageReq) {
        log.info(">>>>>>>>>>>>>>>>>分页查询房间信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<RoomData> pageWrapper = new LambdaQueryWrapper<>();
        //关键词查询条件
        String keyword = pageReq.getKeyword();
        if (!CheckParam.isNull(keyword)) {
            pageWrapper.or(or -> or.eq(RoomData::getRoomTitle, keyword));
            pageWrapper.or(or -> or.eq(RoomData::getBriefData, keyword));
        }
        setQueryCriteria(pageWrapper, pageReq);
        pageWrapper.orderBy(true, false, RoomData::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<RoomData> pageList = roomDataRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page, new ArrayList<>());
        }
        List<String> roomDataIdList
                = pageList.stream().map(item -> item.getRoomDataId()).distinct().collect(Collectors.toList());
        List<RoomImg> roomImgList = roomImgRepository.selectList(new LambdaQueryWrapper<RoomImg>()
                .in(RoomImg::getRoomDataId, roomDataIdList));
        Map<String, List<RoomImg>> imgHashMap = roomImgList.stream()
                .collect(Collectors.groupingBy(RoomImg::getRoomDataId));
        List<RoomDataResp> respList =
                mapperFacade.mapAsList(pageList, RoomDataResp.class);
        Integer startIndex = (pageReq.getItemsPerPage() * pageReq.getCurrentPage()) - pageReq.getItemsPerPage() + 1;
        AtomicInteger idBeginIndex = new AtomicInteger(startIndex);
        respList.stream().forEach(item -> {
            item.setId(Integer.valueOf(idBeginIndex.getAndIncrement()).longValue());
            List<RoomImg> imgList = imgHashMap.get(item.getRoomDataId());
            if (!CollectionUtils.isEmpty(imgList)) {
                item.setImgList(imgList.stream().map(img -> img.getImgUrl()).collect(Collectors.toList()));
            }
        });
        return PageBuilder.buildPageResult(page, respList);
    }

    /**
     * 更新房间信息
     *
     * @param updateReq 更新房间信息请求参数
     * @author: Singer
     * @date 2024/3/24
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateItem(RoomDataUpdateReq updateReq) {
        log.info(">>>>>>>>>>>>>>>>>更新房间信息请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        List<String> imgList = updateReq.getImgList();
        if (CollectionUtils.isEmpty(imgList)) {
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "图片信息不可为空");
        }
        String mainId = updateReq.getRoomDataId();
        String authUserId = authUserService.currentAuthUserId();
        RoomData result = roomDataRepository.selectOne(new LambdaQueryWrapper<RoomData>()
                .eq(RoomData::getRoomDataId, mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result, updateReq);
        roomDataRepository.updateById(result);
        //删除原来的图片
        roomImgRepository.delete(new LambdaQueryWrapper<RoomImg>()
                .eq(RoomImg::getRoomDataId, mainId));
        imgList.stream().forEach(item -> {
            RoomImg img = new RoomImg();
            img.setRoomImgId(SnowflakeIdWorker.uniqueMainId());
            img.setRoomDataId(mainId);
            img.setImgUrl(item);
            try {
                BaseUtil.setFieldValueNotNull(img);
                img.setOperatorId(authUserId);
            } catch (Exception e) {
                log.error("新增房间图片信息->设置为空的属性失败 {} , {} ", e.getMessage(), e);
                throw new BusinessException(ErrorCode.ERROR.getCode(),
                        ErrorCode.ERROR.getMessage() + StrUtil.COLON + e.getMessage() + StrUtil.COLON + e);
            }
            roomImgRepository.insert(img);
        });
    }

    /**
     * 设置需要更新的字段
     *
     * @param updateReq 更新参数
     * @param entity    实体
     * @author: Singer
     * @date 2024/3/24
     */
    private void setNeedUpdateItem(RoomData entity,
                                   RoomDataUpdateReq updateReq) {
        if (!CheckParam.isNull(updateReq.getRoomTypeId())) {
            entity.setRoomTypeId(updateReq.getRoomTypeId());
        }
        if (!CheckParam.isNull(updateReq.getRoomTitle())) {
            entity.setRoomTitle(updateReq.getRoomTitle());
        }
        if (!CheckParam.isNull(updateReq.getBriefData())) {
            entity.setBriefData(updateReq.getBriefData());
        }
        if (!CheckParam.isNull(updateReq.getRoomNo())) {
            entity.setRoomNo(updateReq.getRoomNo());
        }
        if (!CheckParam.isNull(updateReq.getMainImg())) {
            entity.setMainImg(updateReq.getMainImg());
        }
        if (!CheckParam.isNull(updateReq.getRoomStatus())) {
            entity.setRoomStatus(updateReq.getRoomStatus());
        }
        if (!CheckParam.isNull(updateReq.getRoomFloor())) {
            entity.setRoomFloor(updateReq.getRoomFloor());
        }
        if (!CheckParam.isNull(updateReq.getUnitPrice())) {
            entity.setUnitPrice(updateReq.getUnitPrice());
        }
        if (!CheckParam.isNull(updateReq.getRoomArea())) {
            entity.setRoomArea(updateReq.getRoomArea());
        }
        if (!CheckParam.isNull(updateReq.getBedNum())) {
            entity.setBedNum(updateReq.getBedNum());
        }
    }
}
