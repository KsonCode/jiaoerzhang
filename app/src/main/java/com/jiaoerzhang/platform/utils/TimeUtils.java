package com.jiaoerzhang.platform.utils;import java.text.SimpleDateFormat;import java.util.Date;public class TimeUtils {    /**     *     * @Title:getDateData     * @Description:毫秒转成日期格式     * @param newsTime     *            时间     * @return     * @author lisher     */    public static String getDate(String newsTime, String format) {        SimpleDateFormat sdf = new SimpleDateFormat(format);        long newsDateL = (Long.parseLong(newsTime)) * 1000;        String newsDateS = sdf.format(new Date(newsDateL));        return newsDateS;    }    private static final int MIN_DELAY_TIME= 1000;  // 两次点击间隔不能少于1000ms    private static long lastClickTime;    public static boolean isFastClick() {        boolean flag = true;        long currentClickTime = System.currentTimeMillis();        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {            flag = false;        }        lastClickTime = currentClickTime;        return flag;    }}