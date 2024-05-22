package main.java;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

public class AvailableTools {
    private final LinkedHashMap<String, Tool> availableTools = new LinkedHashMap<>();

    public LinkedHashMap<String, Tool> getAvailableTools() {
        buildAvailableToolsTable();
        return availableTools;
    }

    private void buildAvailableToolsTable() {
        // create tools with tool charge table
        Tool[] tools = getTools();
        String[] toolCodes = {"CHNS", "LADW", "JAKD", "JAKR"};

        // create tool table using tool codes as key
        mapTools(tools, toolCodes);
    }

    private static Tool[] getTools() {
        // create charge table
        ToolCharge ladderCharge = new ToolCharge(new BigDecimal("1.99"), true, true, false);
        ToolCharge chainsawCharge = new ToolCharge(new BigDecimal("1.49"), true, false, true);
        ToolCharge jackhammerCharge = new ToolCharge(new BigDecimal("2.99"), true, false, false);

        // create available tools with their respective charge table
        Tool chainsaw = new Tool("Chainsaw", "Sthil", chainsawCharge);
        Tool ladder = new Tool("Ladder", "Werner", ladderCharge);
        Tool dwJackhammer = new Tool("Jackhammer", "DeWalt", jackhammerCharge);
        Tool rJackhammer = new Tool("Jackhammer", "Ridgid", jackhammerCharge);

        // create array of available tools and tool codes
        return new Tool[]{chainsaw, ladder, dwJackhammer, rJackhammer};
    }

    private void mapTools(Tool[] tools, String[] toolCodes) {
        for (int i = 0; i < tools.length; i++) {
            availableTools.put(toolCodes[i], tools[i]);
        }
    }
}
