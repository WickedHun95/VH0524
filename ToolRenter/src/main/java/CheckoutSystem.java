package main.java;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedHashMap;

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
    private LinkedHashMap<String, Tool> availableTools = new LinkedHashMap<>();
    private static Tool tool;
    private final AvailableTools tools = new AvailableTools();
    private final Validator validator = new Validator();
    private final DateCalculator dateCalculator = new DateCalculator();

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
        setCheckoutDate(dateCalculator.convertStringToDate(dateString));
        setDailyRentalCharge(tool.getToolCharge());

        // validate rental day count and discount percent
        validator.validateDiscountPercent(providedDiscountPercent);
        validator.validateRentalDayCount(rentalDayCount);

        // date calculations
        DateCalculator.setTool(tool);
        dateCalculator.calculateChargeDaysAndDueDate(checkoutDate, rentalDayCount);
        chargeDays = dateCalculator.getChargeDays();
        dueDate = dateCalculator.getDueDate();

        // discount and charge calculations
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
        System.out.println("Checkout date: " + dateCalculator.convertDateToString(checkoutDate));
        System.out.println("Due date: " + dateCalculator.convertDateToString(dueDate));
        System.out.println("Daily rental charge: " + "$" + dailyRentalCharge);
        System.out.println("Charge days: " + chargeDays);
        System.out.println("Pre-discount charge: " + "$" + preDiscountCharge);
        System.out.println("Discount percent: " + discountPercent + "%");
        System.out.println("Discount amount: " + "$" + discountAmount);
        System.out.println("Final charge: " + "$" + finalCharge);
    }

    private void calculateDiscountAndCharges(int chargeDays, BigDecimal dailyRentalCharge, int discountPercent) {
        // charge days x daily charge rounded half up to cents
        preDiscountCharge = dailyRentalCharge.multiply(BigDecimal.valueOf(chargeDays)).setScale(2, RoundingMode.HALF_UP);
        // pre discount charge x discount percent rounded half up to cents
        discountAmount = preDiscountCharge.multiply(BigDecimal.valueOf(discountPercent * 0.01)).setScale(2, RoundingMode.HALF_UP);
        // pre discount charge - discount amount
        finalCharge = preDiscountCharge.subtract(discountAmount);
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
