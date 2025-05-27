package com.example.my_super_agent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PDFGenerationToolTest {

    @Test
    void generatePDF() {

        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();
        String result = pdfGenerationTool.generatePDF("你好pdf", "test1.pdf");
    }
}