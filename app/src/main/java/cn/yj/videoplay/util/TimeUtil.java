package cn.yj.videoplay.util;

/**
 * Created by yangjie on 2019/3/11.
 */

public class TimeUtil {
    // 时间戳转时间
    public static String getTime(int time) {
        time = time/1000;
        long days = time / 86400;//转换天数
        time = time % 86400;//剩余秒数
        long hours = time / 3600;//转换小时数
        time = time % 3600;//剩余秒数
        long minutes = time / 60;//转换分钟
        time = time % 60;//剩余秒数

        String strHour = String.valueOf(hours);
        while (strHour.length()<2) {
            strHour = "0"+strHour;
        }
        String strMin = String.valueOf(minutes);
        while (strMin.length()<2) {
            strMin = "0"+strMin;
        }
        String strSec = String.valueOf(time);
        while (strSec.length()<2) {
            strSec = "0"+strSec;
        }

        return strHour+":"+strMin+":"+strSec+"";
    }
}
