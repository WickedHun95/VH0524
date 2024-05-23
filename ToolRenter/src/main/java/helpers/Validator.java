package main.java.helpers;

public class Validator {
    public void validateDiscountPercent(int discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be a number between 0-100. Please enter a valid discount percent.");
        }
    }

    public void validateRentalDayCount(int rentalDayCount) {
        if (rentalDayCount < 1) {
            throw new IllegalArgumentException("Rental day count must be at least 1 day or more. Please enter a valid rental day count.");
        }
    }
}
