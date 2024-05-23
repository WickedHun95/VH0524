package main.java.data;

import java.math.BigDecimal;

public class Tool {
    private final String toolType;
    private final String toolBrand;
    private final ToolCharge toolCharge;

    public Tool(String toolType, String toolBrand, ToolCharge toolCharge) {
        this.toolType = toolType;
        this.toolBrand = toolBrand;
        this.toolCharge = toolCharge;
    }

    public String getToolType() {
        return toolType;
    }

    public String getToolBrand() {
        return toolBrand;
    }

    public BigDecimal getToolCharge() {
        return toolCharge.dailyCharge();
    }

    public boolean getHasWeekendCharge() {
        return toolCharge.hasWeekendCharge();
    }

    public boolean getHasHolidayCharge() {
        return toolCharge.hasHolidayCharge();
    }
}
