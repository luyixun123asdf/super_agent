package com.example.my_super_agent.agent;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.example.my_super_agent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.internal.StringUtil;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 处理工具调用的基础代理类，具体实现了 think 和 act 方法，可以用作创建实例的父类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ToolCallAgent extends ReactAgent {

    // 可用的工具
    private final ToolCallback[] availableTools;
    // 保存工具调用信息的响应结果（需要调用哪些工具）
    private ChatResponse toolCallChatResponse;

    // 工具调用管理者
    private final ToolCallingManager toolCallManager;
    // 禁用spring ai 内置的工具调用，自己来维护
    private final ChatOptions chatOptions;

    public ToolCallAgent(ToolCallback[] toolCallbacks) {
        super();
        this.availableTools = toolCallbacks;
        this.toolCallManager = ToolCallingManager.builder().build();
        this.chatOptions = DashScopeChatOptions.builder()
                .withProxyToolCalls(true)
                .build();
    }

    /**
     * 处理当前状态，并决定是否进行下一步行动
     *
     * @return 是否进行下一步行动
     */

    @Override
    public boolean thinking() {
        // 1.校验提示词，如果提示词不为空，则添加到消息列表中
        if (!StringUtil.isBlank(getNextPrompt())) {
            getMessageList().add(new UserMessage(getNextPrompt()));
        }
        // 2.调用大模型工具
        Prompt prompt = new Prompt(getMessageList(), chatOptions); // 防止框架管理工具添加chatOptions
        try {
            ChatResponse chatResponse = getChatClient().prompt(prompt)
                    .system(getSystemPrompt())
                    .tools(availableTools)
                    .call()
                    .chatResponse();
            // 记录响应结果
            this.toolCallChatResponse = chatResponse;
            // 3.解析响应结果，得到需要调用的工具
            // 助手信息
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
            // 需要调用的工具列表
            List<AssistantMessage.ToolCall> toolCalls = assistantMessage.getToolCalls();
            log.info("assistantMessage:{}", assistantMessage.getText());
            log.info("需要调用{}个 tool:", toolCalls.size());
            // 拼接工具调用信息
            String toolCallInfo = toolCalls.stream()
                    .map(toolCall -> String.format("工具名称: %s， 工具参数：%s", toolCall.name(), toolCall.arguments()))
                    .collect(Collectors.joining("\n"));
            // 如果不需要调用工具，则返回false
            if (toolCallInfo.isEmpty()) {
                // 只有不调用工具，才记录助手信息
                getMessageList().add(assistantMessage);
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            log.error(getName() + "的思考过程遇到了问题: " + e.getMessage());
            getMessageList().add(
                    new AssistantMessage("处理时遇到错误: " + e.getMessage()));
            return false;
        }

    }


    /**
     * 执行工具调用并处理结果
     *
     * @return 执行结果
     */

    @Override
    public String act() {
        //　１、校验是否有工具
        if (!toolCallChatResponse.hasToolCalls()) {
            return "没有工具调用";
        }
        // 执行工具
        Prompt prompt = new Prompt(getMessageList(), chatOptions);
        ToolExecutionResult toolExecutionResult = toolCallManager.executeToolCalls(prompt, toolCallChatResponse);
        // 记录消息上下文
        setMessageList(toolExecutionResult.conversationHistory());
        // 获取工具执行完成后的返回信息
        ToolResponseMessage last = (ToolResponseMessage) CollUtil.getLast(toolExecutionResult.conversationHistory());
        // 判断是否执行了终止工具
        boolean match = last.getResponses().stream()
                .anyMatch(response -> "doTerminate".equals(response.name()));
        if (match) {
            log.info("终止工具调用");
            setState(AgentState.FINISHED);
            return "终止工具调用";
        }
        String results = last.getResponses().stream()
                .map(toolResponse -> "工具" + toolResponse.name() + " 完成了它的任务！结果:" + toolResponse.responseData())
                .collect(Collectors.joining("\n"));
        log.info("工具执行结果：{}", results);
        return results;
    }
}
