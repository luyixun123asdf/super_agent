package com.example.my_super_agent.tools;

import com.example.my_super_agent.constant.FileConstant;
import org.apache.commons.io.FileUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileOperationTool {
    private static final String DIR = FileConstant.FILE_SAVE_DIR;

    @Tool(description = "Read content from file")
    public String readFile(@ToolParam(description = "Name of a file to read") String fileName) {
        try {
            // 构建并规范化路径
            Path filePath = Paths.get(DIR).resolve(fileName).normalize();

            // 确保路径在允许的目录范围内
            if (!filePath.startsWith(DIR)) {
                return "Error: Access denied to the file path";
            }

            // 检查文件是否存在
            if (!Files.isRegularFile(filePath)) {
                return "Error: File does not exist or is not a regular file";
            }

            // 安全地读取文件内容
            return Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }


    }

    @Tool(description = "Write content to file")
    public String writeFile(@ToolParam(description = "Name of a file to write") String fileName,
                            @ToolParam(description = "Content to write") String content) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "Error:  Invalid file name";
        }

        // 简单防止路径穿越攻击
        if (fileName.contains("..") || fileName.contains(File.separator)) {
            return "Error:  File name contains invalid path characters";
        }

        try {
            File dir = new File(DIR);
            if (!dir.exists()) {
                FileUtils.forceMkdir(dir);
            }

            File targetFile = new File(dir, fileName);
            FileUtils.writeStringToFile(targetFile, content, StandardCharsets.UTF_8);

            return "Success:  File written successfully";

        } catch (IOException e) {
            // 可选：将异常记录到日志中以便后续分析
            return "Error:  File write failed - " + e.getMessage();
        }
    }
}
