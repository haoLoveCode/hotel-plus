package cn.common.service.impl.biz.app;

import cn.common.repository.entity.biz.RoomBooking;
import cn.common.repository.entity.biz.RoomData;
import cn.common.repository.entity.biz.RoomImg;
import cn.common.repository.repository.biz.RoomDataRepository;
import cn.common.repository.repository.biz.RoomImgRepository;
import cn.common.req.biz.openBiz.roomData.RoomDataAddReq;
import cn.common.req.biz.openBiz.roomData.RoomDataReq;
import cn.common.req.biz.openBiz.roomData.RoomDataUpdateReq;
import cn.common.resp.biz.RoomDataResp;
import cn.common.service.biz.app.AppRoomDataService;
import cn.common.service.platform.AuthUserService;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pro.skywalking.collection.CollectionUtils;
import pro.skywalking.constants.BaseConstant;
import pro.skywalking.enums.ErrorCode;
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
@Service("appRoomDataService")
@Slf4j
public class AppRoomDataServiceImpl implements AppRoomDataService {

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
      *
      * @description: 根据房间预定ID查询房间信息
      * @author: create by singer - Singer email:singer-coder@qq.com
      * @date 2024/3/24
      * @param roomBookingId 房间预定ID
      * @return cn.common.repository.entity.biz.RoomData
      */
    @Override
    public RoomData queryRoomByBookingId(String roomBookingId){
        MPJLambdaWrapper<RoomData> roomWrapper = new MPJLambdaWrapper<>();
        roomWrapper.leftJoin(RoomBooking.class,RoomBooking::getRoomDataId,RoomData::getRoomDataId);
        roomWrapper.selectAll(RoomData.class);
        roomWrapper.eq(RoomBooking::getRoomBookingId,roomBookingId);
        RoomData roomData = roomDataRepository.selectJoinOne(RoomData.class, roomWrapper);
        return roomData;
    }

    /**
     * 新增房间信息
     * @author: Singer
     * @date 2024/3/24
     * @param addReq 新增房间信息Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(RoomDataAddReq addReq){
        List<String> imgList = addReq.getImgList();
        if(CollectionUtils.isEmpty(imgList)){
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
            log.error("新增房间信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
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
                log.error("新增房间图片信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
                throw new BusinessException(ErrorCode.ERROR.getCode(),
                        ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
            }
            roomImgRepository.insert(img);
        });
    }

    /**
     * 批量删除房间信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 需要被删除的房间信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<RoomData> entityList = roomDataRepository.selectList(
            new LambdaQueryWrapper<RoomData>().in(RoomData::getRoomDataId,mainIdList));
        entityList.stream().forEach(item -> {
            roomImgRepository.delete(new LambdaQueryWrapper<RoomImg>().eq(RoomImg::getRoomDataId,item.getRoomDataId()));
            roomDataRepository.deleteById(item);
        });
    }

    /**
     * 查询房间信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @Override
    public List<RoomDataResp> queryRoomData(RoomDataReq req){
        log.info(">>>>>>>>>>>>>>>>>查询房间信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<RoomData> wrapper = new LambdaQueryWrapper<>();
        //关键词查询条件
        String keyword = req.getKeyword();
        if(!CheckParam.isNull(keyword)){
            wrapper.or(or -> or.eq(RoomData::getRoomTitle, keyword));
            wrapper.or(or -> or.eq(RoomData::getBriefData, keyword));
        }
        setQueryCriteria(wrapper,req);
        wrapper.orderBy(true,false,RoomData::getCreateTime);
        List<RoomData> entityList = roomDataRepository.selectList(wrapper);
        if(CollectionUtils.isEmpty(entityList)){
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
            if(!CollectionUtils.isEmpty(imgList)){
                item.setImgList(imgList.stream().map(img -> img.getImgUrl()).collect(Collectors.toList()));
            }
        });
        return respList;
    }

    /**
     * 查询单个房间信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @Override
    public RoomDataResp queryOneRoomData(RoomDataReq req){
        log.info(">>>>>>>>>>>>>>>>>查询单个房间信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<RoomData> wrapper = new LambdaQueryWrapper<>();
        //关键词查询条件
        String keyword = req.getKeyword();
        if(!CheckParam.isNull(keyword)){
            wrapper.or(or -> or.eq(RoomData::getRoomTitle, keyword));
            wrapper.or(or -> or.eq(RoomData::getBriefData, keyword));
        }
        setQueryCriteria(wrapper,req);
        wrapper.orderBy(true,false,RoomData::getCreateTime);
        RoomData entity = roomDataRepository.selectOne(wrapper);
        if(CheckParam.isNull(entity)){
            return new RoomDataResp();
        }
        List<RoomImg> roomImgList = roomImgRepository.selectList(new LambdaQueryWrapper<RoomImg>()
                .eq(RoomImg::getRoomDataId, entity.getRoomDataId()));
        RoomDataResp resp = mapperFacade.map(entity, RoomDataResp.class);
        if(!CollectionUtils.isEmpty(roomImgList)){
            resp.setImgList(roomImgList.stream().map(img -> img.getImgUrl()).collect(Collectors.toList()));
        }
        return resp;
    }

    /**
     * 分页查询房间信息
     * @author: Singer
     * @date 2024/3/24
     * @param  pageReq 分页查询房间信息Req
     * @return Pagination
     */
    @Override
    public Pagination<RoomDataResp> queryByPage(
        RoomDataReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询房间信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<RoomData> pageWrapper = new LambdaQueryWrapper<>();
        //关键词查询条件
        String keyword = pageReq.getKeyword();
        if(!CheckParam.isNull(keyword)){
            pageWrapper.or(or -> or.eq(RoomData::getRoomTitle, keyword));
            pageWrapper.or(or -> or.eq(RoomData::getBriefData, keyword));
        }
        setQueryCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,RoomData::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<RoomData> pageList = roomDataRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
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
            if(!CollectionUtils.isEmpty(imgList)){
                item.setImgList(imgList.stream().map(img -> img.getImgUrl()).collect(Collectors.toList()));
            }
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
    private void setQueryCriteria(LambdaQueryWrapper<RoomData> pageWrapper,
                        RoomDataReq pageReq){

        if(!CheckParam.isNull(pageReq.getRoomDataId())){
            pageWrapper.eq(RoomData::getRoomDataId,pageReq.getRoomDataId());
        }

        if(!CheckParam.isNull(pageReq.getRoomTypeId())){
            pageWrapper.eq(RoomData::getRoomTypeId,pageReq.getRoomTypeId());
        }

        if(!CheckParam.isNull(pageReq.getRoomTitle())){
            pageWrapper.like(RoomData::getRoomTitle,pageReq.getRoomTitle());
        }

        if(!CheckParam.isNull(pageReq.getBriefData())){
            pageWrapper.like(RoomData::getBriefData,pageReq.getBriefData());
        }

        if(!CheckParam.isNull(pageReq.getRoomNo())){
            pageWrapper.like(RoomData::getRoomNo,pageReq.getRoomNo());
        }

        if(!CheckParam.isNull(pageReq.getMainImg())){
            pageWrapper.like(RoomData::getMainImg,pageReq.getMainImg());
        }

        if(!CheckParam.isNull(pageReq.getRoomStatus())){
            pageWrapper.eq(RoomData::getRoomStatus,pageReq.getRoomStatus());
        }

        if(!CheckParam.isNull(pageReq.getRoomFloor())){
            pageWrapper.like(RoomData::getRoomFloor,pageReq.getRoomFloor());
        }

        if(!CheckParam.isNull(pageReq.getUnitPrice())){
            pageWrapper.ge(RoomData::getUnitPrice,pageReq.getUnitPrice());
        }

        if(!CheckParam.isNull(pageReq.getRoomArea())){
            pageWrapper.ge(RoomData::getRoomArea,pageReq.getRoomArea());
        }

        if(!CheckParam.isNull(pageReq.getBedNum())){
            pageWrapper.ge(RoomData::getBedNum,pageReq.getBedNum());
        }
    }

    /**
     * 更新房间信息
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新房间信息请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(RoomDataUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新房间信息请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        List<String> imgList = updateReq.getImgList();
        if(CollectionUtils.isEmpty(imgList)){
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "图片信息不可为空");
        }
        String mainId = updateReq.getRoomDataId();
        String authUserId = authUserService.currentAuthUserId();
        RoomData result = roomDataRepository.selectOne(new LambdaQueryWrapper<RoomData>()
                    .eq(RoomData::getRoomDataId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        roomDataRepository.updateById(result);
        //删除原来的图片
        roomImgRepository.delete(new LambdaQueryWrapper<RoomImg>()
                .eq(RoomImg::getRoomDataId,mainId));
        imgList.stream().forEach(item -> {
            RoomImg img = new RoomImg();
            img.setRoomImgId(SnowflakeIdWorker.uniqueMainId());
            img.setRoomDataId(mainId);
            img.setImgUrl(item);
            try {
                BaseUtil.setFieldValueNotNull(img);
                img.setOperatorId(authUserId);
            } catch (Exception e) {
                log.error("新增房间图片信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
                throw new BusinessException(ErrorCode.ERROR.getCode(),
                        ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
            }
            roomImgRepository.insert(img);
        });
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(RoomData entity,
        RoomDataUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getRoomTypeId())){
            entity.setRoomTypeId(updateReq.getRoomTypeId());
        }
        if(!CheckParam.isNull(updateReq.getRoomTitle())){
            entity.setRoomTitle(updateReq.getRoomTitle());
        }
        if(!CheckParam.isNull(updateReq.getBriefData())){
            entity.setBriefData(updateReq.getBriefData());
        }
        if(!CheckParam.isNull(updateReq.getRoomNo())){
            entity.setRoomNo(updateReq.getRoomNo());
        }
        if(!CheckParam.isNull(updateReq.getMainImg())){
            entity.setMainImg(updateReq.getMainImg());
        }
        if(!CheckParam.isNull(updateReq.getRoomStatus())){
            entity.setRoomStatus(updateReq.getRoomStatus());
        }
        if(!CheckParam.isNull(updateReq.getRoomFloor())){
            entity.setRoomFloor(updateReq.getRoomFloor());
        }
        if(!CheckParam.isNull(updateReq.getUnitPrice())){
            entity.setUnitPrice(updateReq.getUnitPrice());
        }
        if(!CheckParam.isNull(updateReq.getRoomArea())){
            entity.setRoomArea(updateReq.getRoomArea());
        }
        if(!CheckParam.isNull(updateReq.getBedNum())){
            entity.setBedNum(updateReq.getBedNum());
        }
    }
}
