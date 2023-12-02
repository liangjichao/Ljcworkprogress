package com.ljc.workprogress.util;

import com.ljc.workprogress.domain.WpsConfig;
import com.ljc.workprogress.domain.dto.ResultDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DateComputeUtils {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(WpsConfig.dateFormatPattern);

    public static int calculateWorkingDays(String start, String end) {

        LocalDate startDay = LocalDate.parse(start + " 00:00:00", formatter);
        LocalDate endDay = LocalDate.parse(end + " 23:59:59", formatter);
        int workingDays = 0;
        LocalDate currentDate = startDay;
        while (!currentDate.isAfter(endDay)) {
            if (excludeHoliDay(currentDate)) {
                workingDays++;
            }
            currentDate = currentDate.plusDays(1);
        }

        return workingDays;
    }


    private static boolean excludeHoliDay(LocalDate currentDate) {
        String month = currentDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
        ResultDto<List<Integer>> listResultDto = RestUtils.postList(Integer.class, String.format("/wps/holiday/%s", month), null);
        if (listResultDto.isSuccess()) {
            return !listResultDto.getResultValue().contains(currentDate.getDayOfYear());
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
