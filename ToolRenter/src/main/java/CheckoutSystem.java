package main.java;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class CheckoutSystem {
    private String toolCode;
    private String toolType;
    private String toolBrand;
    private int rentalDayCount;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private BigDecimal dailyRentalCharge;
    private static int chargeDays;
    private BigDecimal preDiscountCharge;
    private int discountPercent;
    private BigDecimal discountAmount;
    private BigDecimal finalCharge;
    private final AvailableTools tools = new AvailableTools();
    private LinkedHashMap<String, Tool> availableTools = new LinkedHashMap<>();
    private static Tool tool;

    public void generateRentalAgreement(String providedToolCode, int providedRentalDayCount, int providedDiscountPercent, String dateString) {
        // populate tools with given data
        availableTools = tools.getAvailableTools();

        // setters
        setToolCode(providedToolCode);
        setToolWithToolCode(toolCode);
        setToolType();
        setToolBrand();
        setRentalDayCount(providedRentalDayCount);
        setDiscountPercent(providedDiscountPercent);
        setCheckoutDate(convertStringToDate(dateString));
        setDailyRentalCharge(tool.getToolCharge());

        // validate rental day count and discount percent
        validateDiscountPercent(providedDiscountPercent);
        validateRentalDayCount(rentalDayCount);

        // calculations
        calculateChargeDaysAndDueDate(checkoutDate, rentalDayCount);
        calculateDiscountAndCharges(chargeDays, dailyRentalCharge, discountPercent);

        // display rental agreement to console
        displayRentalAgreement();
    }

    public BigDecimal getDailyRentalCharge() {
        return dailyRentalCharge;
    }

    public static int getChargeDays() {
        return chargeDays;
    }

    public BigDecimal getPreDiscountCharge() {
        return preDiscountCharge;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getFinalCharge() {
        return finalCharge;
    }

    private void displayRentalAgreement() {
        System.out.println("Tool code: " + toolCode);
        System.out.println("Tool type: " + toolType);
        System.out.println("Tool brand: " + toolBrand);
        System.out.println("Rental days: " + rentalDayCount);
        System.out.println("Checkout date: " + convertDateToString(checkoutDate));
        System.out.println("Due date: " + convertDateToString(dueDate));
        System.out.println("Daily rental charge: " + "$" + dailyRentalCharge);
        System.out.println("Charge days: " + chargeDays);
        System.out.println("Pre-discount charge: " + "$" + preDiscountCharge);
        System.out.println("Discount percent: " + discountPercent + "%");
        System.out.println("Discount amount: " + "$" + discountAmount);
        System.out.println("Final charge: " + "$" + finalCharge);
    }

    private void validateDiscountPercent(int discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be a number between 0-100. Please enter a valid discount percent.");
        }
    }

    private void validateRentalDayCount(int rentalDayCount) {
        if (rentalDayCount < 1) {
            throw new IllegalArgumentException("Rental day count must be at least 1 day or more. Please enter a valid rental day count.");
        }
    }

    private void calculateDiscountAndCharges(int chargeDays, BigDecimal dailyRentalCharge, int discountPercent) {
        // charge days x daily charge rounded half up to cents
        preDiscountCharge = dailyRentalCharge.multiply(BigDecimal.valueOf(chargeDays)).setScale(2, RoundingMode.HALF_UP);
        // pre discount charge x discount percent rounded half up to cents
        discountAmount = preDiscountCharge.multiply(BigDecimal.valueOf(discountPercent * 0.01)).setScale(2, RoundingMode.HALF_UP);
        // pre discount charge - discount amount
        finalCharge = preDiscountCharge.subtract(discountAmount);
    }

    private LocalDate convertStringToDate(String date) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("M/d/yy");
        return LocalDate.parse(date, inputFormatter);
    }

    private String convertDateToString(LocalDate date) {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        return date.format(outputFormatter);
    }

    private void calculateChargeDaysAndDueDate(LocalDate checkoutDate, int count) {
        // add holidays to calendar
        Set<LocalDate> holidays = addHolidaysToCalendarForDateRange();

        // calculate due date and charge days based on rental days, weekends, and holidays
        dueDate = calculateDueDateAndChargeDays(checkoutDate, count, holidays);
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
        LocalDate septemberOne = LocalDate.of(year, Month.SEPTEMBER, 1);
        LocalDate firstMonday = septemberOne.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        holidays.add(firstMonday);
    }

    private static void addIndependenceDayToHolidayCalender(int year, Set<LocalDate> holidays) {
        LocalDate independenceDay = LocalDate.of(year, Month.JULY, 4);
        // set holiday to friday if it falls on a saturday
        if (independenceDay.getDayOfWeek() == DayOfWeek.SATURDAY) {
            independenceDay = LocalDate.of(year, Month.JULY, 3);
        }
        // set holiday to the next monday if it falls on a sunday
        if (independenceDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
            independenceDay = LocalDate.of(year, Month.JULY, 5);
        }

        holidays.add(independenceDay);
    }

    private static LocalDate calculateDueDateAndChargeDays(LocalDate checkoutDate, int daysToAdd, Set<LocalDate> holidays) {
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
        return date;
    }

    private static boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    private void setToolWithToolCode(String toolCode) {
        tool = availableTools.get(toolCode);
    }

    private void setToolType() {
        this.toolType = tool.getToolType();
    }

    private void setToolBrand() {
        this.toolBrand = tool.getToolBrand();
    }

    private void setRentalDayCount(int rentalDayCount) {
        this.rentalDayCount = rentalDayCount;
    }

    private void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    private void setCheckoutDate(LocalDate dateString) {
        this.checkoutDate = dateString;
    }

    private void setDailyRentalCharge(BigDecimal dailyRentalCharge) {
        this.dailyRentalCharge = dailyRentalCharge;
    }
}
