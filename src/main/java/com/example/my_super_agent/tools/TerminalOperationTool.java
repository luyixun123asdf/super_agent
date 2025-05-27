package com.example.my_super_agent.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author lyx
 * @date 2023-05-05 16:05
 * @description 终端操作工具
 **/
public class TerminalOperationTool {
    /**
     * 执行命令
     *
     * @param command 命令
     * @return 命令执行结果
     */
    @Tool(description = "Execute a command in the terminal")
    public String executeCommand(@ToolParam(description = "Command to execute") String command) {
        StringBuilder output = new StringBuilder();

        try {
            // 使用 ProcessBuilder 来执行命令（更安全、灵活）
            Process process = Runtime.getRuntime().exec("cmd /c " + command);

            // 使用 try-with-resources 自动关闭流
            try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

                String line;
                while ((line = inputReader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }

                while ((line = errorReader.readLine()) != null) {
                    output.append("ERROR: ").append(line).append(System.lineSeparator());
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                output.append("Command exited with code: ").append(exitCode);
            }

        } catch (IOException e) {
            // 使用日志框架替代 printStackTrace()
            // 示例：LOGGER.error("Error executing command", e);
            output.append("Error executing command: ").append(e.getMessage());
        } catch (InterruptedException e) {
            output.append("Command execution interrupted.");
            Thread.currentThread().interrupt(); // 恢复中断状态
        }

        return output.toString();

    }
}
