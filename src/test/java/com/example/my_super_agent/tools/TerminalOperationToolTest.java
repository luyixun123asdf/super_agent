package com.example.my_super_agent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerminalOperationToolTest {

    @Test
    void executeCommand() {
        TerminalOperationTool tool = new TerminalOperationTool();
        String result = tool.executeCommand("pip list");
        System.out.println(result);
    }
}