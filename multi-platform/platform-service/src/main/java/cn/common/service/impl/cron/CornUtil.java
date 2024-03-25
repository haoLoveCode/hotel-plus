package cn.common.service.impl.cron;

import org.quartz.CronExpression;
import pro.skywalking.utils.CheckParam;
import pro.skywalking.utils.DateTimeUtil;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName multi-platform
 * @Description:
 * @date 2024-03-25
 */
public class CornUtil {

    public static boolean isNull(Object obj) {
        return null == obj || isNull(obj.toString());
    }

    public static boolean isNull(String str) {
        return null == str || "".equals(str.trim()) || "null".equals(str.trim()) || "undefined".equals(str.trim());
    }

    /**
     * 校验在当前时间是否满足cron时间规则表达式
     *
     * @param cron
     * @param time 时间
     * @return java.lang.Boolean Cron表达式是否会执行
     * @throws
     */
    private static Boolean matchCron(String cron, String time) {
        if (CheckParam.isNull(cron) || CheckParam.isNull(time)) {
            return false;
        }
        CronExpression exp = null;
        try {
            exp = new CronExpression(cron);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Date cornCompareData = Date.from(DateTimeUtil.stringToLocalDateTime(time).atZone(ZoneId.systemDefault()).toInstant());
        //核心代码，判断是否执行
        Boolean needExec = exp.isSatisfiedBy(cornCompareData);
        return needExec;
    }

    public static void main(String[] args) throws ParseException {
        String cron = "* * * 3/5 * ? ";
        System.out.println("是否会执行:"+matchCron(cron, "2024-03-25 14:21:08"));
    }

}
