package com.example.yilaoapp.utils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigUtil {
    /**
     *
     * 判断字符串格式是否为手机号格式
     * 处理字符串时去掉首尾空格 判断依据
     * java-正则表达式判断手机号
     * 要更加准确的匹配手机号码只匹配11位数字是不够的，比如说就没有以144开始的号码段，
     * 故先要整清楚现在已经开放了多少个号码段，国家号码段分配如下：
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186
     * 电信：133、153、180、189、（1349卫通）
     * @param phone_num
     * @return true:是 false:不是
     */
    public static boolean isPhoneNum(String phone_num) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(14[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(phone_num);
        return m.matches();
    }

    /**
     * 计算距离上次获得页面的时间是多少秒
     * @param before
     * @return
     */
    public static int GetTime(Date before)
    {
        Date curDate = new Date(System.currentTimeMillis());
        long beforeTime=before.getTime();
        long curTime=curDate.getTime();
        return (int) ((curTime-beforeTime)/1000);
    }
}
