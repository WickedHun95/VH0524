package main.java;

import java.math.BigDecimal;

public record ToolCharge(BigDecimal dailyCharge, boolean hasWeekdayCharge, boolean hasWeekendCharge,
                         boolean hasHolidayCharge) {
}
