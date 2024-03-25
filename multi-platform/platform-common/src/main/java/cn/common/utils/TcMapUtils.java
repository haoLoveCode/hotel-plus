package cn.common.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import pro.skywalking.collection.CollectionUtils;
import pro.skywalking.enums.ErrorCode;
import pro.skywalking.exception.BusinessException;
import pro.skywalking.utils.CheckParam;
import pro.skywalking.utils.HttpTookit;
import cn.common.utils.interaction.IpPlaceData;
import cn.common.utils.interaction.TcPlaceData;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName multi-platform
 * @Description:
 * @date 2024-03-25
 */
@Slf4j
public class TcMapUtils {

    //地点建议请求
    private static String placeSuggestion = "https://apis.map.qq.com/ws/place/v1/suggestion";

    //通过IP地址定位
    private static String ipTcLocation = "https://apis.map.qq.com/ws/location/v1/ip";

    //逆地址解析结果封装
    private static String GEO_CODER_URL = "https://apis.map.qq.com/ws/geocoder/v1";

    //腾讯地图的key
    private static String TC_MAP_KET = "BSVBZ-SMTKV-TUPPE-5QAL4-HBOSZ-WLFMS";


    public static void main(String[] args) {
        ipAddressData("183.214.207.182");
        geoCoder("30.57447","103.92377");
    }


    /**
     * @param latitude 纬度
     * @param longitude 经度
     * @return cn.common.utils.TcMapUtils.TcPlaceData
     * @description: 搜索地点
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2023/11/1
     */
    public static IpPlaceData geoCoder(String latitude, String longitude) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("key", TC_MAP_KET);
        //经纬度（GCJ02坐标系），格式：
        //location=lat<纬度>,lng<经度>
        paramMap.put("location", latitude+","+longitude);

        String result = HttpTookit.getRequest(GEO_CODER_URL, paramMap, StandardCharsets.UTF_8.name());
        log.info(">>>>>>>>>>>>请求地图位置信息直接结果:{}<<<<<<<<<<<<<", result);

//        if (CheckParam.isNull(result)) {
//            return new IpPlaceData();
//        }
//        IpPlaceData ipPlaceData;
//        JSONObject resultObject;
//        try {
//            resultObject = JSON.parseObject(result);
//            ipPlaceData = resultObject.getObject("result", new TypeReference<IpPlaceData>() {
//            });
//        } catch (Exception e) {
//            throw new BusinessException(ErrorCode.ERROR.getCode(),
//                    ErrorCode.ERROR.getMessage() + StrUtil.COLON + e.getMessage() + StrUtil.COLON + e);
//        }
//        if (CheckParam.isNull(ipPlaceData)) {
//            return new IpPlaceData();
//        }
//        log.info(">>>>>>>>>>>>请求地图位置信息结果:{}<<<<<<<<<<<<<", JSON.toJSONString(ipPlaceData));
//        return ipPlaceData;
        return null;
    }

    /**
     * @param ip IP地址
     * @return cn.common.utils.TcMapUtils.TcPlaceData
     * @description: 搜索地点
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2023/11/1
     */
    public static IpPlaceData ipAddressData(String ip) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("key", TC_MAP_KET);
        paramMap.put("ip", ip);

        String result = HttpTookit.getRequest(ipTcLocation, paramMap, StandardCharsets.UTF_8.name());
        log.info(">>>>>>>>>>>>请求地图位置信息直接结果:{}<<<<<<<<<<<<<", result);
        if (CheckParam.isNull(result)) {
            return new IpPlaceData();
        }
        IpPlaceData ipPlaceData;
        JSONObject resultObject;
        try {
            resultObject = JSON.parseObject(result);
            ipPlaceData = resultObject.getObject("result", new TypeReference<IpPlaceData>() {
            });
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    ErrorCode.ERROR.getMessage() + StrUtil.COLON + e.getMessage() + StrUtil.COLON + e);
        }
        if (CheckParam.isNull(ipPlaceData)) {
            return new IpPlaceData();
        }
        log.info(">>>>>>>>>>>>请求地图位置信息结果:{}<<<<<<<<<<<<<", JSON.toJSONString(ipPlaceData));
        return ipPlaceData;
    }

    /**
     * @param keyword 关键词
     * @return cn.common.utils.TcMapUtils.TcPlaceData
     * @description: 搜索地点
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2023/11/1
     */
    public static TcPlaceData[] queryPlace(String keyword) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("key", TC_MAP_KET);
        paramMap.put("keyword", keyword);

        String result = HttpTookit.getRequest(placeSuggestion, paramMap, StandardCharsets.UTF_8.name());
        log.info(">>>>>>>>>>>>请求地图位置信息直接请求结果:{}<<<<<<<<<<<<<", result);
        if (CheckParam.isNull(result)) {
            return new TcPlaceData[10];
        }
        TcPlaceData[] TcPlaceDataArray;
        JSONObject resultObject;
        try {
            resultObject = JSON.parseObject(result);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    ErrorCode.ERROR.getMessage() + StrUtil.COLON + e.getMessage() + StrUtil.COLON + e);
        }
        JSONArray dataArray = resultObject.getJSONArray("data");
        if (CollectionUtils.isEmpty(dataArray)) {
            return new TcPlaceData[10];
        }
        TcPlaceDataArray = dataArray.toArray(TcPlaceData.class);
        log.info(">>>>>>>>>>>>请求地图位置信息结果:{}<<<<<<<<<<<<<", JSON.toJSONString(TcPlaceDataArray));
        return TcPlaceDataArray;
    }


}
