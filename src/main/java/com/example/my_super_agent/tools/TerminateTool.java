package com.example.my_super_agent.tools;

import org.springframework.ai.tool.annotation.Tool;

public class TerminateTool {
    @Tool(description = """
             Terminate the interaction when the request is met OR if the assistant cannot proceed further with the task. \s
                        "When you have finished all the tasks, call this tool to end the work.
            """)
    public String doTerminate(String input) {
        return "任务结束";
    }
}
