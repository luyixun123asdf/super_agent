package com.example.my_super_agent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


class FileOperationToolTest {


    @Test
    void readFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String result = fileOperationTool.readFile("test.txt");
    }

    @Test
    void writeFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String result = fileOperationTool.writeFile("test.txt","Hello World");
        assertTrue(result.contains("File written successfully"));
    }
}