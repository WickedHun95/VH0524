package main.java;

import java.util.Scanner;

public class ToolRental {
    public static void main(String[] args) {
        //scanner object for user input
        Scanner scanner = new Scanner(System.in);
        //checkoutSystem object to call its methods
        CheckoutSystem checkoutSystem = new CheckoutSystem();
        // take all user inputs
        System.out.println("Please enter tool code: ");
        String toolCode = scanner.nextLine();
        System.out.println("Please enter rental day count: ");
        int rentalDayCount = scanner.nextInt();
        System.out.println("Please enter discount percent: ");
        int discountPercent = scanner.nextInt();
        System.out.println("Please enter checkout date (m/d/yy): ");
        String dateString = scanner.next();
        // generate rental agreement
        checkoutSystem.generateRentalAgreement(toolCode, rentalDayCount, discountPercent, dateString);
    }
}
