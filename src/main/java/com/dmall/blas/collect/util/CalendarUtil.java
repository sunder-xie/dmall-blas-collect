package com.dmall.blas.collect.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wm.nb.common.DateUtils;

public class CalendarUtil {
    private static final Logger logger = LoggerFactory.getLogger(CalendarUtil.class);
    public static final int FIRST_DAY_OF_WEEK = Calendar.MONDAY; // 中国周一是一周的第一天  
	public static Date parse(String str, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(str);
		} catch (Exception e) {
			logger.error("日期格式化失败.{}", e.getMessage());
		}
		return null;
	}
	
	public static int getTodayLeftMillis() {
		long millis = parse(DateUtils.format(new Date(), "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss").getTime() - System.currentTimeMillis();
		return (int) millis;
	}

	public static Date getDayAfter(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, day);
		return calendar.getTime();
	}
	public static int getHour(Date date) {
		if (date == null) return 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	public static int getDateValue(Date date) {
		if (date == null) return 0;
		return Integer.parseInt(DateUtils.format(date, "yyyyMMdd"));
	}
	public static String getWeekUpperCase(Date date) {
		int week = getWeek(date) - 1;
		switch (week) {
		case 0:
			return "星期日";
		case 1:
			return "星期一";
		case 2:
			return "星期二";
		case 3:
			return "星期三";
		case 4:
			return "星期四";
		case 5:
			return "星期五";
		case 6:
			return "星期六";
		}
		return null;
	}
	public static int getWeek(Date date) {
		if (date == null) return 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	public static int getMonthWeek(Date date) {
		if (date == null) return 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_MONTH);
	}
	public static int getYearMonth(Date date) {
		if (date == null) return 0;
		return Integer.parseInt(DateUtils.format(date, "yyyyMM"));
	}
	public static int getYearWeek(Date date) {
		if (date == null) return 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

		return weekOfYear;
	}
	public static int getQuarter(Date date) {
		if (date == null) return 0;
		int quarter = 0;
		
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        switch (month) {
        case Calendar.JANUARY:
        case Calendar.FEBRUARY:
        case Calendar.MARCH:
            quarter = 1;
            break;
        case Calendar.APRIL:
        case Calendar.MAY:
        case Calendar.JUNE:
            quarter = 2;
            break;
        case Calendar.JULY:
        case Calendar.AUGUST:
        case Calendar.SEPTEMBER:
            quarter = 3;
            break;
        case Calendar.OCTOBER:
        case Calendar.NOVEMBER:
        case Calendar.DECEMBER:
            quarter = 4;
            break;
        default:
            break;
        }
        return quarter;
	}
	public static int getSecond(Date date) {
		if (date == null) return 0;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.SECOND);
	}
	

    public static int getWeekYear(Date date) {
		if (date == null) return 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR); // implicitly calls complete()
        

//        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
//        int maxDayOfYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
//        int minimalDays = calendar.getMinimalDaysInFirstWeek();
        int weekOfYear = getYearWeek(date);
        if (calendar.get(Calendar.MONTH) == Calendar.JANUARY && weekOfYear >= 52) {
        	--year;
        } else if (calendar.get(Calendar.MONTH) == 11 && weekOfYear == 1) {
        	++year;
        }
        return year;
    }
    
    public static int daysBetween(Date fromDate,Date toDate)    
    {    
    	try{
    		if(fromDate == null || toDate == null) return 0;
    		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
    		fromDate=sdf.parse(sdf.format(fromDate));  
    		toDate=sdf.parse(sdf.format(toDate));  
    		Calendar cal = Calendar.getInstance();    
            cal.setTime(fromDate);    
            long time1 = cal.getTimeInMillis();                 
            cal.setTime(toDate);    
            long time2 = cal.getTimeInMillis();         
            long between_days=(time2-time1)/(1000*3600*24);  
            return Integer.parseInt(String.valueOf(between_days));     
    	}catch(Exception e){
    		return 0;
    	}   
    }
    
	public static void main(String[] args) {
		Date date = new Date();
		System.out.println(DateUtils.format(date, "yyyy-MM-dd HH:mm:ss"));
		System.out.println("getHour=" + getHour(date));
		System.out.println("getDateValue=" + getDateValue(date));
		System.out.println("getWeek=" + getWeek(date));
		System.out.println("getMonthWeek=" + getMonthWeek(date));
		System.out.println("getYearWeek=" + getYearWeek(date));
		System.out.println("getQuarter=" + getQuarter(date));
		System.out.println("getSeconds=" + getSecond(date));
	}
//	payments.setOrderPayHour(Integer.parseInt(DateUtils.format(order.getOrderPayTime(), "H")));
//	payments.setOrderPayDateValue(Integer.parseInt(DateUtils.format(order.getOrderPayTime(), "yyyyMMdd")));
//	payments.setOrderPayWeek(Integer.parseInt(DateUtils.format(order.getOrderPayTime(), "E")));
//	payments.setOrderPayMonthWeek(Integer.parseInt(DateUtils.format(order.getOrderPayTime(), "W")));
//	payments.setOrderPayYearWeek(Integer.parseInt(DateUtils.format(order.getOrderPayTime(), "w")));
//	payments.setOrderPayQuarter(Integer.parseInt(DateUtils.format(order.getOrderPayTime(), "M")));//TODO 季度
//	payments.setOrderPayTimeValue(Integer.parseInt(DateUtils.format(order.getOrderPayTime(), "s")));
	
}
