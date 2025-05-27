package com.example.my_super_agent.tools;

import cn.hutool.core.io.FileUtil;
import com.example.my_super_agent.constant.FileConstant;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfType0Font;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;
import java.io.IOException;

/**
 * pdf生成工具
 */
public class PDFGenerationTool {
    private final static String BASE_PATH = FileConstant.PDF_FILE_DIR;

    @Tool(description = "Generate a PDF file")
    public String generatePDF(@ToolParam(description = "Content to generate PDF") String content,
                              @ToolParam(description = "File name", required = false) String fileName) {
        // 默认字体常量
        final String FONT_FAMILY = "STSongStd-Light";
        final String ENCODING = "UniGB-UCS2-H";

        // 校验文件名合法性
        if (fileName == null || fileName.trim().isEmpty() || fileName.contains("..") || fileName.contains("/")) {
            fileName = "default_" + System.currentTimeMillis() + ".pdf";
        }

        String filePath = BASE_PATH + fileName;

        try {
            // 判断目录是否存在，避免重复创建
            File dir = new File(BASE_PATH);
            if (!dir.exists()) {
                FileUtil.mkdir(BASE_PATH);
            }

            // 创建 PdfWriter 和 PdfDocument 对象
            try (PdfWriter writer = new PdfWriter(filePath);
                 PdfDocument pdf = new PdfDocument(writer);
                 Document document = new Document(pdf)) {

                // 使用内置中文字体
                PdfFont font = PdfFontFactory.createFont(FONT_FAMILY, ENCODING);

                document.setFont(font);

                // 创建段落并添加内容
                Paragraph paragraph = new Paragraph(content);
                document.add(paragraph);
            }

            return "PDF generated successfully to: " + filePath;
        } catch (IOException e) {
            // 安全地隐藏具体错误信息，同时建议记录日志
            // logger.error("Error generating PDF", e);
            return "Error generating PDF: An internal error occurred.";
        }
//        String filePath = BASE_PATH + fileName;
//        try {
//            // 创建目录
//            FileUtil.mkdir(BASE_PATH);
//            // 创建 PdfWriter 和 PdfDocument 对象
//            try (PdfWriter writer = new PdfWriter(filePath);
//                 PdfDocument pdf = new PdfDocument(writer);
//                 Document document = new Document(pdf)) {
//                // 自定义字体（需要人工下载字体文件到特定目录）
////                String fontPath = Paths.get("src/main/resources/static/fonts/simsun.ttf")
////                        .toAbsolutePath().toString();
////                PdfFont font = PdfFontFactory.createFont(fontPath,
////                        PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
//                // 使用内置中文字体
//                PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
//                document.setFont(font);
//                // 创建段落
//                Paragraph paragraph = new Paragraph(content);
//                // 添加段落并关闭文档
//                document.add(paragraph);
//            }
//            return "PDF generated successfully to: " + filePath;
//        } catch (IOException e) {
//            return "Error generating PDF: " + e.getMessage();
//        }


    }
}
