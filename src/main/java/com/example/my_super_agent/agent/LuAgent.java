package com.example.my_super_agent.agent;

import com.example.my_super_agent.advisor.MyLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

/**
 * 自己的超级智能体，可以直接使用
 */
@Component
public class LuAgent extends ToolCallAgent {

    public LuAgent(ToolCallback[] toolCallbacks, ChatModel dashscopeChatModel) {
        super(toolCallbacks);
        this.setName("Lu Manus");
        String SYSTEM_PROMPT = """
                You are LuManus, an all-capable AI assistant,
                aimed at solving any task presented by the user. 
                You have various tools at your disposal that you can call upon to efficiently complete complex requests. 
                """;
        this.setSystemPrompt(SYSTEM_PROMPT);
        String NEXT_PROMPT = """
                Based on user needs, proactively select the most appropriate tool or combination of tools. 
                For complex tasks, you can break down the problem and use different tools step by step to solve it. 
                After using each tool, clearly explain the execution results and suggest the next steps.      
                If you want to stop the interaction at any point, use the `terminate` tool/function call.""";
        this.setNextPrompt(NEXT_PROMPT);
        this.setMaxSteps(11);
        ChatClient client = ChatClient.builder(dashscopeChatModel)
                .defaultAdvisors(new MyLoggerAdvisor())
                .build();
        this.setChatClient(client);
    }
}
