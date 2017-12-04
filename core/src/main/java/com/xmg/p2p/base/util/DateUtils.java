package com.xmg.p2p.base.util;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date getStartDate(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
          //  calendar.setTime(new Date());//这句话把我坑死了
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            return calendar.getTime();
        }
        return null;
    }
    public  static Date getEndDate(Date date){
        if(date!=null){
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY,23);
            calendar.set(Calendar.MINUTE,59);
            calendar.set(Calendar.MINUTE,59);
            return  calendar.getTime();
        }
        return null;
    }
    public static  long  getBetweenTime(Date date1,Date date2){
        return  Math.abs((date1.getTime()-date2.getTime())/1000);//获取时间的间隔 单位s
    }
}
