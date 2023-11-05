package com.jdl.ljc.joyworkprogress.util;

import com.alibaba.fastjson.JSON;
import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import com.jdl.ljc.joyworkprogress.domain.WpsConfig;
import com.jdl.ljc.joyworkprogress.domain.vo.HolidayResultVo;
import com.jdl.ljc.joyworkprogress.domain.vo.HolidayVo;
import com.jdl.ljc.joyworkprogress.enums.HolidayTypeEnum;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class DateComputeUtils {
    private static Map<String, List<Integer>> holidayMap = Maps.newHashMap();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(WpsConfig.dateFormatPattern);
    public static int calculateWorkingDays(String start, String end) {

        LocalDate startDay = LocalDate.parse(start + " 00:00:00", formatter);
        LocalDate endDay = LocalDate.parse(end + " 23:59:59", formatter);
        int workingDays = 0;
        LocalDate currentDate = startDay;
        while (!currentDate.isAfter(endDay)) {
            if (excludeDay(currentDate)) {
                workingDays++;
            }
            currentDate = currentDate.plusDays(1);
        }

        return workingDays;
    }

    private static boolean excludeDay(LocalDate currentDate) {
        return currentDate.getDayOfWeek() != DayOfWeek.SATURDAY &&
                currentDate.getDayOfWeek() != DayOfWeek.SUNDAY;
    }
    private static boolean excludeHoliDay(LocalDate currentDate) {
        String month = currentDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
        List<Integer> holidayList = Lists.newArrayList();
        if (!holidayMap.containsKey(month)) {
            holidayMap.put(month, holidayList);
            String holidayDataByMonth = DateComputeUtils.getHolidayDataByMonth(month);
            HolidayResultVo resultVo = JSON.parseObject(holidayDataByMonth, HolidayResultVo.class);
            if (resultVo.getCode().equals(1)&&resultVo.getData() != null) {
                for (HolidayVo datum : resultVo.getData()) {
                    if (HolidayTypeEnum.REST_DAY.getCode() == datum.getType() || HolidayTypeEnum.HOLIDAY.getCode() == datum.getType()) {
                        holidayList.add(datum.getDayOfYear());
                    }
                }
            }
        }else{
            holidayList = holidayMap.get(month);
        }
        if (holidayList.contains(currentDate.getDayOfYear())) {
            return false;
        }
        return true;
    }

    /**
     * 按月获取节假日数据
     * 202311
     *
     * @param date
     * @return
     */
    public static String getHolidayDataByMonth(String date) {
        String appid = "rdljlmkrknuisw8m";
        String appSecret = "y0XdiM1E6G4fLDxD8ErcW0X1sgH70sBP";
        String url = String.format("https://www.mxnzp.com/api/holiday/list/month/%s?ignoreHoliday=false&app_id=%s&app_secret=%s", date, appid, appSecret);
        return RestUtils.get(url);
    }
}
