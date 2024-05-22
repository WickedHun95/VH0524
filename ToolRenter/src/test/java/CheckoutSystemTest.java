package test.java;

import main.java.CheckoutSystem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckoutSystemTest {
    @Test
    void should_throw_an_IllegalArgumentException_when_discount_percent_is_not_in_range() {
        var checkoutSystem = new CheckoutSystem();
        String toolCode = "JAKR";
        String checkoutDate = "9/3/15";
        int rentalDays = 5;
        int discount = 101;

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> checkoutSystem.generateRentalAgreement(toolCode, rentalDays, discount, checkoutDate));

        assertEquals("Discount percent must be a number between 0-100. Please enter a valid discount percent.", exception.getMessage());
    }

    @Test
    void should_apply_discount_and_charge_customer_for_weekend_but_not_for_independence_day() {
        var checkoutSystem = new CheckoutSystem();
        String toolCode = "LADW";
        String checkoutDate = "7/2/20";
        int rentalDays = 3;
        int discount = 10;
        BigDecimal expectedDailyRentalCharge = new BigDecimal("1.99");
        int expectedChargeDays = 2;
        BigDecimal expectedPreDiscountCharge = expectedDailyRentalCharge.multiply(BigDecimal.valueOf(expectedChargeDays));
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(discount * 0.01)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedFinalCharge = expectedPreDiscountCharge.subtract(expectedDiscountAmount);

        checkoutSystem.generateRentalAgreement(toolCode, rentalDays, discount, checkoutDate);

        assertEquals(checkoutSystem.getDailyRentalCharge(), expectedDailyRentalCharge);
        assertEquals(CheckoutSystem.getChargeDays(), expectedChargeDays);
        assertEquals(checkoutSystem.getPreDiscountCharge(), expectedPreDiscountCharge);
        assertEquals(checkoutSystem.getDiscountAmount(), expectedDiscountAmount);
        assertEquals(checkoutSystem.getFinalCharge(), expectedFinalCharge);
    }

    @Test
    void should_apply_discount_and_charge_customer_for_weekend() {
        var checkoutSystem = new CheckoutSystem();
        String toolCode = "CHNS";
        String checkoutDate = "7/2/15";
        int rentalDays = 5;
        int discount = 25;
        BigDecimal expectedDailyRentalCharge = new BigDecimal("1.49");
        int expectedChargeDays = 3;
        BigDecimal expectedPreDiscountCharge = expectedDailyRentalCharge.multiply(BigDecimal.valueOf(expectedChargeDays));
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(discount * 0.01)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedFinalCharge = expectedPreDiscountCharge.subtract(expectedDiscountAmount);

        checkoutSystem.generateRentalAgreement(toolCode, rentalDays, discount, checkoutDate);

        assertEquals(checkoutSystem.getDailyRentalCharge(), expectedDailyRentalCharge);
        assertEquals(CheckoutSystem.getChargeDays(), expectedChargeDays);
        assertEquals(checkoutSystem.getPreDiscountCharge(), expectedPreDiscountCharge);
        assertEquals(checkoutSystem.getDiscountAmount(), expectedDiscountAmount);
        assertEquals(checkoutSystem.getFinalCharge(), expectedFinalCharge);
    }

    @Test
    void should_not_charge_customer_for_weekend_or_labor_day() {
        var checkoutSystem = new CheckoutSystem();
        String toolCode = "JAKD";
        String checkoutDate = "9/3/15";
        int rentalDays = 6;
        int discount = 0;
        BigDecimal expectedDailyRentalCharge = new BigDecimal("2.99");
        int expectedChargeDays = 3;
        BigDecimal expectedPreDiscountCharge = expectedDailyRentalCharge.multiply(BigDecimal.valueOf(expectedChargeDays));
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(discount * 0.01)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedFinalCharge = expectedPreDiscountCharge.subtract(expectedDiscountAmount);

        checkoutSystem.generateRentalAgreement(toolCode, rentalDays, discount, checkoutDate);

        assertEquals(checkoutSystem.getDailyRentalCharge(), expectedDailyRentalCharge);
        assertEquals(CheckoutSystem.getChargeDays(), expectedChargeDays);
        assertEquals(checkoutSystem.getPreDiscountCharge(), expectedPreDiscountCharge);
        assertEquals(checkoutSystem.getDiscountAmount(), expectedDiscountAmount);
        assertEquals(checkoutSystem.getFinalCharge(), expectedFinalCharge);
    }

    @Test
    void should_not_charge_customer_for_weekend_or_independence_day() {
        var checkoutSystem = new CheckoutSystem();
        String toolCode = "JAKR";
        String checkoutDate = "7/2/15";
        int rentalDays = 9;
        int discount = 0;
        BigDecimal expectedDailyRentalCharge = new BigDecimal("2.99");
        int expectedChargeDays = 5;
        BigDecimal expectedPreDiscountCharge = expectedDailyRentalCharge.multiply(BigDecimal.valueOf(expectedChargeDays));
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(discount * 0.01)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedFinalCharge = expectedPreDiscountCharge.subtract(expectedDiscountAmount);

        checkoutSystem.generateRentalAgreement(toolCode, rentalDays, discount, checkoutDate);

        assertEquals(checkoutSystem.getDailyRentalCharge(), expectedDailyRentalCharge);
        assertEquals(CheckoutSystem.getChargeDays(), expectedChargeDays);
        assertEquals(checkoutSystem.getPreDiscountCharge(), expectedPreDiscountCharge);
        assertEquals(checkoutSystem.getDiscountAmount(), expectedDiscountAmount);
        assertEquals(checkoutSystem.getFinalCharge(), expectedFinalCharge);
    }

    @Test
    void should_apply_discount_and_not_charge_customer_for_weekend_or_independence_day() {
        var checkoutSystem = new CheckoutSystem();
        String toolCode = "JAKR";
        String checkoutDate = "7/2/20";
        int rentalDays = 5;
        int discount = 50;
        BigDecimal expectedDailyRentalCharge = new BigDecimal("2.99");
        int expectedChargeDays = 2;
        BigDecimal expectedPreDiscountCharge = expectedDailyRentalCharge.multiply(BigDecimal.valueOf(expectedChargeDays));
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(discount * 0.01)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedFinalCharge = expectedPreDiscountCharge.subtract(expectedDiscountAmount);

        checkoutSystem.generateRentalAgreement(toolCode, rentalDays, discount, checkoutDate);

        assertEquals(checkoutSystem.getDailyRentalCharge(), expectedDailyRentalCharge);
        assertEquals(CheckoutSystem.getChargeDays(), expectedChargeDays);
        assertEquals(checkoutSystem.getPreDiscountCharge(), expectedPreDiscountCharge);
        assertEquals(checkoutSystem.getDiscountAmount(), expectedDiscountAmount);
        assertEquals(checkoutSystem.getFinalCharge(), expectedFinalCharge);
    }
}