package main.java;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.Set;

public class DateCalculator {
    private int chargeDays;
    private LocalDate dueDate;
    private static Tool tool;

    public static void setTool(Tool tool) {
        DateCalculator.tool = tool;
    }

    public int getChargeDays() {
        return chargeDays;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate convertStringToDate(String date) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("M/d/yy");
        return LocalDate.parse(date, inputFormatter);
    }

    public String convertDateToString(LocalDate date) {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        return date.format(outputFormatter);
    }

    public void calculateChargeDaysAndDueDate(LocalDate checkoutDate, int count) {
        // add holidays to calendar
        Set<LocalDate> holidays = addHolidaysToCalendarForDateRange();

        // calculate due date and charge days based on rental days, weekends, and holidays
        calculateDueDateAndChargeDays(checkoutDate, count, holidays);
    }

    private static Set<LocalDate> addHolidaysToCalendarForDateRange() {
        Set<LocalDate> holidays = new HashSet<>();

        for (int year = 2015; year <= 2020; year++) {
            addLaborDayToHolidayCalendar(year, holidays);
            addIndependenceDayToHolidayCalender(year, holidays);
        }

        return holidays;
    }

    private static void addLaborDayToHolidayCalendar(int year, Set<LocalDate> holidays) {
        LocalDate septemberOne = java.time.LocalDate.of(year, Month.SEPTEMBER, 1);
        LocalDate firstMonday = septemberOne.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        holidays.add(firstMonday);
    }

    private static void addIndependenceDayToHolidayCalender(int year, Set<LocalDate> holidays) {
        LocalDate independenceDay = java.time.LocalDate.of(year, Month.JULY, 4);
        // set holiday to friday if it falls on a saturday
        if (independenceDay.getDayOfWeek() == DayOfWeek.SATURDAY) {
            independenceDay = java.time.LocalDate.of(year, Month.JULY, 3);
        }
        // set holiday to the next monday if it falls on a sunday
        if (independenceDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
            independenceDay = java.time.LocalDate.of(year, Month.JULY, 5);
        }

        holidays.add(independenceDay);
    }

    private void calculateDueDateAndChargeDays(LocalDate checkoutDate, int daysToAdd, Set<LocalDate> holidays) {
        LocalDate date = checkoutDate;
        int days = 0;
        boolean hasWeekendCharge = tool.getHasWeekendCharge();
        boolean hasHolidayCharge = tool.getHasHolidayCharge();

        for (int i = 0; i < daysToAdd; i++) {
            // start counting charge days the day after checkout
            date = date.plusDays(1);
            // always charge for weekdays
            if (!isWeekend(date) && !holidays.contains(date)) {
                days++;
            }
            // charge for weekend if there is a weekend charge
            if (isWeekend(date) && hasWeekendCharge) {
                days++;
            }
            // charge for holidays if there is a holiday charge
            if (holidays.contains(date) && hasHolidayCharge) {
                days++;
            }
        }

        chargeDays = days;
        dueDate = date;
    }

    private static boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
