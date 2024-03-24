package cn.common.service.impl.platform;

import cn.common.repository.entity.platform.PlatformApiLog;
import cn.common.repository.repository.platform.PlatformApiLogRepository;
import cn.common.service.data.ItemCriteriaBuilder;
import cn.common.service.platform.PlatformApiLogService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import pro.skywalking.collection.CollectionUtils;
import pro.skywalking.constants.PlatformConstant;
import pro.skywalking.helper.PageBuilder;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.req.platform.log.PlatformApiLogReq;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.resp.platform.log.ApiLogResp;
import pro.skywalking.resp.platform.log.ApiLogStatisticsResp;
import pro.skywalking.resp.platform.log.PlatformApiLogResp;
import pro.skywalking.utils.CheckParam;
import pro.skywalking.utils.DateTimeUtil;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Singer create by Singer email:singer-coder@qq.com
 * @packageName cn.common.service
 * @Description: 首页分类相关服务方法实现
 * @date 2024-03-06
 */
@Service("platformApiLogService")
@Slf4j
public class PlatformApiLogServiceImpl implements PlatformApiLogService {

    @Resource
    private PlatformApiLogRepository platformApiLogRepository;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private ItemCriteriaBuilder itemCriteriaBuilder;

    /**
     * @param
     * @return
     * @description: 清空日志表
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    @Override
    @PostConstruct()
    public void clearApiLog() {
        log.info(">>>>>>>>>>>>>>>>>执行清空日志信息表 <<<<<<<<<<<<<<<<");
        platformApiLogRepository.clearApiLog();
    }

    /**
     * 批量删除信息
     *
     * @param req 需要被删除的信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req) {
        List<String> mainIdList = req.getMainIdList();
        if (CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        platformApiLogRepository.batchDeleteItem(mainIdList);
    }

    /**
     * 分页查询
     *
     * @param pageReq 分页查询Req
     * @return pro.skywalking.resp.page.Pagination
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    @Override
    public Pagination<PlatformApiLogResp> queryByPage(PlatformApiLogReq pageReq) {
        log.info(">>>>>>>>>>>>>>>>>分页查询Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<PlatformApiLog> pageWrapper = new LambdaQueryWrapper<>();
        itemCriteriaBuilder.rigidCriteria(pageWrapper, true);
        setPageCriteria(pageWrapper, pageReq);
        pageWrapper.orderBy(true, false, PlatformApiLog::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<PlatformApiLog> pageList = platformApiLogRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page, new ArrayList<>());
        }
        List<PlatformApiLogResp> respList = mapperFacade.mapAsList(pageList, PlatformApiLogResp.class);
        Integer startIndex = (pageReq.getItemsPerPage() * pageReq.getCurrentPage()) - pageReq.getItemsPerPage() + 1;
        AtomicInteger idBeginIndex = new AtomicInteger(startIndex);
        respList.stream().forEach(item -> {
            item.setId(Integer.valueOf(idBeginIndex.getAndIncrement()).longValue());
        });
        return PageBuilder.buildPageResult(page, respList);
    }

    /**
     * 根据参数统计访问量
     *
     * @param req 查询参数
     * @return java.util.List
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    @Override
    public List<ApiLogStatisticsResp> statisticsVisitCount(PlatformApiLogReq req) {
        log.info(">>>>>>>>>>>>>>>>>>统计系统日志请求参数<<<<<<<<<<<<<:" + JSON.toJSONString(req));
        List<ApiLogStatisticsResp> statisticsList = Lists.newArrayList();
        LambdaQueryWrapper<PlatformApiLog> wrapper = new LambdaQueryWrapper<>();
        setPageCriteria(wrapper, req);
        List<PlatformApiLog> platformApiLogList = platformApiLogRepository.selectList(wrapper);
        if (CollectionUtils.isEmpty(platformApiLogList)) {
            return Lists.newArrayList();
        }
        List<ApiLogResp> entityList = Lists.newArrayList();
        platformApiLogList = platformApiLogList.stream().sorted(Comparator.comparing(PlatformApiLog::getCreateTime)).collect(Collectors.toList());
        //去除时分秒
        platformApiLogList.stream().forEachOrdered(item -> {
            ApiLogResp result = new ApiLogResp();
            LocalDateTime createTime = item.getCreateTime();
            String time = DateTimeUtil.convertTimeToString(PlatformConstant.TIME_DATE_FORMAT, createTime);
            createTime.withHour(BigInteger.ZERO.intValue()).withMinute(BigInteger.ZERO.intValue()).withSecond(BigInteger.ZERO.intValue());
            result.setCreateTime(time);
            result.setId(item.getId());
            entityList.add(result);
        });
        Map<String, Long> apiLogHashMap = entityList.stream().collect(Collectors.groupingBy(ApiLogResp::getCreateTime, Collectors.counting()));
        apiLogHashMap.forEach((key, value) -> {
            ApiLogStatisticsResp resp = new ApiLogStatisticsResp();
            resp.setVisitCount(value);
            resp.setCreateTime(key);
            statisticsList.add(resp);
        });
        List<ApiLogStatisticsResp> respList = statisticsList.stream().sorted(Comparator.comparing(ApiLogStatisticsResp::getCreateTime)).collect(Collectors.toList());
        return respList;
    }

    /**
     * 设置分页条件
     *
     * @param pageWrapper 查询条件
     * @param pageReq     分页插件
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    private void setPageCriteria(LambdaQueryWrapper<PlatformApiLog> pageWrapper, PlatformApiLogReq pageReq) {

        if (!CheckParam.isNull(pageReq.getBeginTime())) {
            pageWrapper.ge(PlatformApiLog::getCreateTime, pageReq.getBeginTime());
        }

        if (!CheckParam.isNull(pageReq.getEndTime())) {
            pageWrapper.le(PlatformApiLog::getCreateTime, pageReq.getEndTime());
        }

        if (!CheckParam.isNull(pageReq.getUserName())) {
            pageWrapper.like(PlatformApiLog::getUserName, pageReq.getUserName());
        }

        if (!CheckParam.isNull(pageReq.getUserName())) {
            pageWrapper.like(PlatformApiLog::getUserName, pageReq.getUserName());
        }

        if (!CheckParam.isNull(pageReq.getOperation())) {
            pageWrapper.like(PlatformApiLog::getOperation, pageReq.getOperation());
        }

        if (!CheckParam.isNull(pageReq.getOperationTime())) {
            pageWrapper.like(PlatformApiLog::getOperationTime, pageReq.getOperationTime());
        }

        if (!CheckParam.isNull(pageReq.getMethod())) {
            pageWrapper.like(PlatformApiLog::getMethod, pageReq.getMethod());
        }

        if (!CheckParam.isNull(pageReq.getParams())) {
            pageWrapper.like(PlatformApiLog::getParams, pageReq.getParams());
        }

        if (!CheckParam.isNull(pageReq.getLocation())) {
            pageWrapper.like(PlatformApiLog::getLocation, pageReq.getLocation());
        }

        if (!CheckParam.isNull(pageReq.getRequestIp())) {
            pageWrapper.like(PlatformApiLog::getRequestIp, pageReq.getRequestIp());
        }
    }
}
