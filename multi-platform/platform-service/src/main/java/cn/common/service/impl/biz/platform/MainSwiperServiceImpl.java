package cn.common.service.impl.biz.platform;

import cn.common.repository.entity.biz.MainSwiper;
import cn.common.repository.repository.biz.MainSwiperRepository;
import cn.common.req.platform.MainSwiperAddReq;
import cn.common.req.platform.MainSwiperReq;
import cn.common.req.platform.MainSwiperUpdateReq;
import cn.common.resp.platform.MainSwiperResp;
import cn.common.service.biz.platform.MainSwiperService;
import cn.common.service.platform.AuthUserService;
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
import pro.skywalking.enums.ErrorCode;
import pro.skywalking.exception.BusinessException;
import pro.skywalking.helper.PageBuilder;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.utils.BaseUtil;
import pro.skywalking.utils.CheckParam;
import pro.skywalking.utils.SnowflakeIdWorker;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 首页轮播图相关服务方法实现
 * @date 2024-03-06
 */
@Service("mainSwiperService")
@Slf4j
public class MainSwiperServiceImpl implements MainSwiperService {

    @Resource
    private MainSwiperRepository mainSwiperRepository;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private AuthUserService authUserService;

    /**
     * 新增
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(MainSwiperAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        MainSwiper entity = mapperFacade.map(addReq, MainSwiper.class);
        try {
            String authUserId = authUserService.currentAuthUserId();
            BaseUtil.setFieldValueNotNull(entity);
            entity.setMainSwiperId(SnowflakeIdWorker.uniqueMainId());
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    ErrorCode.ERROR.getMessage()+ StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        mainSwiperRepository.insert(entity);
    }

    /**
     * 批量删除信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<MainSwiper> entityList = mainSwiperRepository.selectList(
            new LambdaQueryWrapper<MainSwiper>().in(MainSwiper::getMainSwiperId,mainIdList));
        entityList.stream().forEach(item -> {
            mainSwiperRepository.deleteById(item);
        });
    }

    /**
     * 查询所有信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @Override
    public List<MainSwiperResp> queryAllMainSwiper(){
        List<MainSwiper> entityList = mainSwiperRepository.selectList(new LambdaQueryWrapper<>());
            if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,MainSwiperResp.class);
    }

    /**
     * 分页查询
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    @Override
    public Pagination<MainSwiperResp> queryByPage(
        MainSwiperReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<MainSwiper> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,MainSwiper::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<MainSwiper> pageList = mainSwiperRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<MainSwiperResp> respList =
            mapperFacade.mapAsList(pageList, MainSwiperResp.class);
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
     * @param pageReq 分页插件
     * @return
     */
    private void setPageCriteria(LambdaQueryWrapper<MainSwiper> pageWrapper, MainSwiperReq pageReq){

        if(!CheckParam.isNull(pageReq.getMainUrl())){
            pageWrapper.like(MainSwiper::getMainUrl,pageReq.getMainUrl());
        }

        if(!CheckParam.isNull(pageReq.getMainTitle())){
            pageWrapper.like(MainSwiper::getMainTitle,pageReq.getMainTitle());
        }

        if(!CheckParam.isNull(pageReq.getRouterUrl())){
            pageWrapper.like(MainSwiper::getRouterUrl,pageReq.getRouterUrl());
        }

        if(!CheckParam.isNull(pageReq.getItemId())){
            pageWrapper.like(MainSwiper::getItemId,pageReq.getItemId());
        }
    }

    /**
     * 更新
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(MainSwiperUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getMainSwiperId();
        MainSwiper result = mainSwiperRepository.selectOne(new LambdaQueryWrapper<MainSwiper>()
                    .eq(MainSwiper::getMainSwiperId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        mainSwiperRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新参数
     * @param entity 产业
     */
    private void setNeedUpdateItem(MainSwiper entity,
        MainSwiperUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getMainUrl())){
            entity.setMainUrl(updateReq.getMainUrl());
        }
        if(!CheckParam.isNull(updateReq.getMainTitle())){
            entity.setMainTitle(updateReq.getMainTitle());
        }
        if(!CheckParam.isNull(updateReq.getRouterUrl())){
            entity.setRouterUrl(updateReq.getRouterUrl());
        }
        if(!CheckParam.isNull(updateReq.getItemId())){
            entity.setItemId(updateReq.getItemId());
        }
    }
}
