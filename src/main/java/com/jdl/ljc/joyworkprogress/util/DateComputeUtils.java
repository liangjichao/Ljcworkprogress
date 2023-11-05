package com.jdl.ljc.joyworkprogress.util;

import com.jdl.ljc.joyworkprogress.domain.WpsConfig;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateComputeUtils {
    public static int calculateWorkingDays(String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(WpsConfig.dateFormatPattern);
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
}
