package cn.common.service.impl.biz.app;

import cn.common.repository.entity.biz.SalesItem;
import cn.common.repository.entity.biz.SalesItemImg;
import cn.common.repository.repository.biz.SalesItemImgRepository;
import cn.common.repository.repository.biz.SalesItemRepository;
import cn.common.req.biz.openBiz.SalesItemAddReq;
import cn.common.req.biz.openBiz.SalesItemReq;
import cn.common.req.biz.openBiz.SalesItemUpdateReq;
import cn.common.resp.biz.openBiz.SalesItemResp;
import cn.common.service.biz.AuthAppUserService;
import cn.common.service.biz.app.AppSalesItemService;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
 * @Description: 商品信息相关服务方法实现
 * @date 2024-03-06
 */
@Service("appSalesItemService")
@Slf4j
public class AppSalesItemServiceImpl implements AppSalesItemService {

    @Resource
    private SalesItemRepository salesItemRepository;

    @Resource
    private SalesItemImgRepository salesItemImgRepository;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private BaseConstant baseConstant;

    @Resource
    private AuthAppUserService authAppUserService;

    @Resource
    private HttpServletResponse response;

    @Resource
    private HttpServletRequest request;

    /**
     * 新增商品信息
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增商品信息Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(SalesItemAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增商品信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        List<String> imgList = addReq.getImgList();
        if(CollectionUtils.isEmpty(imgList)){
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "图片信息不可为空");
        }
        String mainId = SnowflakeIdWorker.uniqueMainId();
        String authAppUserId = authAppUserService.authAppUserId();
        SalesItem entity = mapperFacade.map(addReq, SalesItem.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setPublisherId(authAppUserId);
            entity.setSalesItemId(mainId);
            entity.setOperatorId(authAppUserId);
        } catch (Exception e) {
            log.error("新增商品信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        salesItemRepository.insert(entity);
        imgList.stream().forEach(item -> {
            SalesItemImg img = new SalesItemImg();
            img.setSalesItemId(mainId);
            img.setSalesItemImgId(SnowflakeIdWorker.uniqueMainId());
            img.setImgUrl(item);
            try {
                BaseUtil.setFieldValueNotNull(img);
                img.setOperatorId(authAppUserId);
            } catch (Exception e) {
                log.error("新增宠物图片信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
                throw new BusinessException(ErrorCode.ERROR.getCode(),
                        ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
            }
            salesItemImgRepository.insert(img);
        });
    }

    /**
     * 批量删除商品信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的商品信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<SalesItem> entityList = salesItemRepository.selectList(
            new LambdaQueryWrapper<SalesItem>().in(SalesItem::getSalesItemId,mainIdList));
        entityList.stream().forEach(item -> {
            salesItemRepository.deleteById(item);
            //删除原来的图片信息
            salesItemImgRepository.delete(new LambdaQueryWrapper<SalesItemImg>()
                    .eq(SalesItemImg::getSalesItemId,item.getSalesItemId()));
        });
    }

    /**
     * 查询商品信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @Override
    public List<SalesItemResp> querySalesItem(SalesItemReq req){
        log.info(">>>>>>>>>>>>>>>>>查询商品信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<SalesItem> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        String keyword = req.getKeyword();
        if(!CheckParam.isNull(keyword)){
            wrapper.like(SalesItem::getItemName,keyword);
        }
        wrapper.orderBy(true,false,SalesItem::getCreateTime);
        List<SalesItem> entityList = salesItemRepository.selectList(wrapper);
        if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        List<String> salesItemIdList
                = entityList.stream().map(item -> item.getSalesItemId()).distinct().collect(Collectors.toList());
        List<SalesItemImg> salesItemImgList = salesItemImgRepository.selectList(new LambdaQueryWrapper<SalesItemImg>()
                .in(SalesItemImg::getSalesItemId, salesItemIdList));
        Map<String, List<SalesItemImg>> imgHashMap = salesItemImgList.stream()
                .collect(Collectors.groupingBy(SalesItemImg::getSalesItemId));
        List<SalesItemResp> respList = mapperFacade.mapAsList(entityList, SalesItemResp.class);
        respList.stream().forEach(item -> {
            List<SalesItemImg> imgList = imgHashMap.get(item.getSalesItemId());
            if(!CollectionUtils.isEmpty(imgList)){
                item.setImgList(imgList.stream().map(img -> img.getImgUrl()).collect(Collectors.toList()));
            }
        });
        return respList;
    }

    /**
     * 查询单个商品信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @Override
    public SalesItemResp queryOneSalesItem(SalesItemReq req){
        log.info(">>>>>>>>>>>>>>>>>查询单个商品信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<SalesItem> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,SalesItem::getCreateTime);
        SalesItem entity = salesItemRepository.selectOne(wrapper);
        if(CheckParam.isNull(entity)){
            return new SalesItemResp();
        }
        List<SalesItemImg> salesItemImgList = salesItemImgRepository.selectList(new LambdaQueryWrapper<SalesItemImg>()
                .eq(SalesItemImg::getSalesItemId, entity.getSalesItemId()));
        SalesItemResp resp = mapperFacade.map(entity, SalesItemResp.class);
        if(!CollectionUtils.isEmpty(salesItemImgList)){
            resp.setImgList(salesItemImgList.stream().map(img -> img.getImgUrl()).collect(Collectors.toList()));
        }
        return resp;
    }

    /**
     * 设置查询条件
     * @author: Singer
     * @date 2024-03-06
     * @param wrapper 查询条件
     * @param req 查询参数
     * @return
     */
    private void setCriteria(LambdaQueryWrapper<SalesItem> wrapper,
                             SalesItemReq req){

        if(!CheckParam.isNull(req.getSalesItemId())){
            wrapper.eq(SalesItem::getSalesItemId,req.getSalesItemId());
        }

        if(!CheckParam.isNull(req.getTypeItemId())){
            wrapper.eq(SalesItem::getTypeItemId,req.getTypeItemId());
        }

        if(!CheckParam.isNull(req.getPublisherId())){
            wrapper.eq(SalesItem::getPublisherId,req.getPublisherId());
        }

        if(!CheckParam.isNull(req.getItemName())){
            wrapper.like(SalesItem::getItemName,req.getItemName());
        }

        if(!CheckParam.isNull(req.getItemSummary())){
            wrapper.like(SalesItem::getItemSummary,req.getItemSummary());
        }

        if(!CheckParam.isNull(req.getItemTitle())){
            wrapper.like(SalesItem::getItemTitle,req.getItemTitle());
        }

        if(!CheckParam.isNull(req.getSalePrice())){
            wrapper.eq(SalesItem::getSalePrice,req.getSalePrice());
        }

        if(!CheckParam.isNull(req.getOriginalPrice())){
            wrapper.eq(SalesItem::getOriginalPrice,req.getOriginalPrice());
        }

        if(!CheckParam.isNull(req.getMainImg())){
            wrapper.like(SalesItem::getMainImg,req.getMainImg());
        }

        if(!CheckParam.isNull(req.getItemStatus())){
            wrapper.eq(SalesItem::getItemStatus,req.getItemStatus());
        }

        if(!CheckParam.isNull(req.getRemarkData())){
            wrapper.like(SalesItem::getRemarkData,req.getRemarkData());
        }
    }

    /**
     * 分页查询商品信息
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询商品信息Req
     * @return Pagination
     */
    @Override
    public Pagination<SalesItemResp> queryByPage(
        SalesItemReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询商品信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<SalesItem> pageWrapper = new LambdaQueryWrapper<>();
        setCriteria(pageWrapper,pageReq);
        String keyword = pageReq.getKeyword();
        if(!CheckParam.isNull(keyword)){
            pageWrapper.like(SalesItem::getItemName,keyword);
        }
        pageWrapper.orderBy(true,false,SalesItem::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<SalesItem> pageList = salesItemRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<String> salesItemIdList
                = pageList.stream().map(item -> item.getSalesItemId()).distinct().collect(Collectors.toList());
        List<SalesItemImg> salesItemImgList = salesItemImgRepository.selectList(new LambdaQueryWrapper<SalesItemImg>()
                .in(SalesItemImg::getSalesItemId, salesItemIdList));
        Map<String, List<SalesItemImg>> imgHashMap = salesItemImgList.stream()
                .collect(Collectors.groupingBy(SalesItemImg::getSalesItemId));
        List<SalesItemResp> respList =
            mapperFacade.mapAsList(pageList, SalesItemResp.class);
        Integer startIndex = (pageReq.getItemsPerPage() * pageReq.getCurrentPage()) - pageReq.getItemsPerPage() + 1;
        AtomicInteger idBeginIndex = new AtomicInteger(startIndex);
        respList.stream().forEach(item -> {
            item.setId(Integer.valueOf(idBeginIndex.getAndIncrement()).longValue());
            List<SalesItemImg> imgList = imgHashMap.get(item.getSalesItemId());
            if(!CollectionUtils.isEmpty(imgList)){
                item.setImgList(imgList.stream().map(img -> img.getImgUrl()).collect(Collectors.toList()));
            }
        });
        return PageBuilder.buildPageResult(page,respList);
    }

    /**
     * 更新商品信息
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新商品信息请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(SalesItemUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新商品信息请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        List<String> imgList = updateReq.getImgList();
        if(CollectionUtils.isEmpty(imgList)){
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "图片信息不可为空");
        }
        String mainId = updateReq.getSalesItemId();
        String authAppUserId = authAppUserService.authAppUserId();
        SalesItem result = salesItemRepository.selectOne(new LambdaQueryWrapper<SalesItem>()
                    .eq(SalesItem::getSalesItemId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        salesItemRepository.updateById(result);
        //删除原来的图片
        salesItemImgRepository.delete(new LambdaQueryWrapper<SalesItemImg>()
                .eq(SalesItemImg::getSalesItemId,mainId));
        imgList.stream().forEach(item -> {
            SalesItemImg img = new SalesItemImg();
            img.setSalesItemId(mainId);
            img.setSalesItemImgId(SnowflakeIdWorker.uniqueMainId());
            img.setImgUrl(item);
            try {
                BaseUtil.setFieldValueNotNull(img);
                img.setOperatorId(authAppUserId);
            } catch (Exception e) {
                log.error("新增宠物图片信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
                throw new BusinessException(ErrorCode.ERROR.getCode(),
                        ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
            }
            salesItemImgRepository.insert(img);
        });
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(SalesItem entity,
                                   SalesItemUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getTypeItemId())){
            entity.setTypeItemId(updateReq.getTypeItemId());
        }
        if(!CheckParam.isNull(updateReq.getItemName())){
            entity.setItemName(updateReq.getItemName());
        }
        if(!CheckParam.isNull(updateReq.getItemSummary())){
            entity.setItemSummary(updateReq.getItemSummary());
        }
        if(!CheckParam.isNull(updateReq.getItemTitle())){
            entity.setItemTitle(updateReq.getItemTitle());
        }
        if(!CheckParam.isNull(updateReq.getSalePrice())){
            entity.setSalePrice(updateReq.getSalePrice());
        }
        if(!CheckParam.isNull(updateReq.getOriginalPrice())){
            entity.setOriginalPrice(updateReq.getOriginalPrice());
        }
        if(!CheckParam.isNull(updateReq.getMainImg())){
            entity.setMainImg(updateReq.getMainImg());
        }
        if(!CheckParam.isNull(updateReq.getItemStatus())){
            entity.setItemStatus(updateReq.getItemStatus());
        }
        if(!CheckParam.isNull(updateReq.getRemarkData())){
            entity.setRemarkData(updateReq.getRemarkData());
        }
    }
}
