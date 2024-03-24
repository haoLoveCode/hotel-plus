package ${basePackageName}.service.impl.biz.platform;

import ${basePackageName}.req.biz.${className}AddReq;
import ${basePackageName}.req.biz.${className}Req;
import ${basePackageName}.req.biz.${className}UpdateReq;
import ${basePackageName}.resp.biz.${className}Resp;
import ${basePackageName}.resp.biz.${className}ExportResp;
import ${basePackageName}.service.biz.platform.${className}Service;
import ${basePackageName}.repository.entity.biz.${className};
import ${basePackageName}.repository.repository.biz.${className}Repository;
import ${basePackageName}.service.platform.AuthUserService;
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
 * @packageName ${basePackageName}.service
 * @Description: ${entityDesc}相关服务方法实现
 * @date 2024/3/24
 */
@Service("${camelCaseEntityName}Service")
@Slf4j
public class ${className}ServiceImpl implements ${className}Service {

    @Resource
    private ${className}Repository ${camelCaseEntityName}Repository;

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
     * 导出${entityDesc}数据
     * @author: Singer
     * @date 2024/3/24
     * @param pageReq
     * @return java.util.List
     */
    @Override
    public void exportData(${className}Req pageReq){
        log.info(">>>>>>>>>>>>>>>>>${entityDesc}数据导出Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<${className}> pageWrapper = new LambdaQueryWrapper<>();
        setQueryCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,${className}::getCreateTime);
        List<${className}> pageList = ${camelCaseEntityName}Repository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return;
        }
        List<${className}ExportResp> respList =
            mapperFacade.mapAsList(pageList, ${className}ExportResp.class);
        try {
            String fileName = "导出信息.xlsx";
            ExportExcelHandler.setExportResponse(response,fileName);
            ExcelWriter excelWriter = null;
            try {
                // 这里 需要指定写用哪个class去写
                excelWriter = EasyExcel.write(response.getOutputStream(), ${className}ExportResp.class).build();
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
     * 新增${entityDesc}
     * @author: Singer
     * @date 2024/3/24
     * @param addReq 新增${entityDesc}Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(${className}AddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增${entityDesc}Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String mainId = SnowflakeIdWorker.uniqueMainId();
        String authUserId = authUserService.currentAuthUserId();
        ${className} entity = mapperFacade.map(addReq, ${className}.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.set${className}Id(mainId);
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增${entityDesc}->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        ${camelCaseEntityName}Repository.insert(entity);
    }

    /**
     * 批量删除${entityDesc}信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 需要被删除的${entityDesc}信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<${className}> entityList = ${camelCaseEntityName}Repository.selectList(
            new LambdaQueryWrapper<${className}>().in(${className}::get${className}Id,mainIdList));
        entityList.stream().forEach(item -> {
            ${camelCaseEntityName}Repository.deleteById(item);
        });
    }

    /**
     * 查询${entityDesc}信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @Override
    public List<${className}Resp> query${className}(${className}Req req){
        log.info(">>>>>>>>>>>>>>>>>查询${entityDesc}Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<${className}> wrapper = new LambdaQueryWrapper<>();
        setQueryCriteria(wrapper,req);
        wrapper.orderBy(true,false,${className}::getCreateTime);
        List<${className}> entityList = ${camelCaseEntityName}Repository.selectList(wrapper);
        if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,${className}Resp.class);
    }

    /**
     * 查询单个${entityDesc}信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @Override
    public ${className}Resp queryOne${className}(${className}Req req){
        log.info(">>>>>>>>>>>>>>>>>查询单个${entityDesc}Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<${className}> wrapper = new LambdaQueryWrapper<>();
        setQueryCriteria(wrapper,req);
        wrapper.orderBy(true,false,${className}::getCreateTime);
        ${className} entity = ${camelCaseEntityName}Repository.selectOne(wrapper);
        if(CheckParam.isNull(entity)){
            return new ${className}Resp();
        }
        return mapperFacade.map(entity,${className}Resp.class);
    }

    /**
     * 设置查询条件
     * @author: Singer
     * @date 2024/3/24
     * @param wrapper 查询条件
     * @param req 查询参数
     * @return
     */
    private void setQueryCriteria(LambdaQueryWrapper<${className}> wrapper,
                        ${className}Req req){

        if(!CheckParam.isNull(req.get${className}Id())){
            wrapper.eq(${className}::get${className}Id,req.get${className}Id());
        }
        <#-- list指令 迭代循环 -->
        <#list columnList as column>

        if(!CheckParam.isNull(req.get${column.setOrSetName}())){
            wrapper.like(${className}::get${column.setOrSetName},req.get${column.setOrSetName}());
        }
        </#list>
    }

    /**
     * 分页查询${entityDesc}
     * @author: Singer
     * @date 2024/3/24
     * @param  pageReq 分页查询${entityDesc}Req
     * @return Pagination
     */
    @Override
    public Pagination<${className}Resp> queryByPage(
        ${className}Req pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询${entityDesc}Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<${className}> pageWrapper = new LambdaQueryWrapper<>();
        setQueryCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,${className}::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<${className}> pageList = ${camelCaseEntityName}Repository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<${className}Resp> respList =
            mapperFacade.mapAsList(pageList, ${className}Resp.class);
        Integer startIndex = (pageReq.getItemsPerPage() * pageReq.getCurrentPage()) - pageReq.getItemsPerPage() + 1;
        AtomicInteger idBeginIndex = new AtomicInteger(startIndex);
        respList.stream().forEach(item -> {
            item.setId(Integer.valueOf(idBeginIndex.getAndIncrement()).longValue());
        });
        return PageBuilder.buildPageResult(page,respList);
    }

    /**
     * 更新${entityDesc}
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新${entityDesc}请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(${className}UpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新${entityDesc}请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.get${className}Id();
        ${className} result = ${camelCaseEntityName}Repository.selectOne(new LambdaQueryWrapper<${className}>()
                    .eq(${className}::get${className}Id,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        ${camelCaseEntityName}Repository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(${className} entity,
        ${className}UpdateReq updateReq){
        <#list columnList as column>
        if(!CheckParam.isNull(updateReq.get${column.setOrSetName}())){
            entity.set${column.setOrSetName}(updateReq.get${column.setOrSetName}());
        }
        </#list>
    }
}
