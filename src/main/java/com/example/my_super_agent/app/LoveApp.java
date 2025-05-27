package com.example.my_super_agent.app;


import com.example.my_super_agent.advisor.MyLoggerAdvisor;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
public class LoveApp {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(LoveApp.class);
    private final ChatClient chatClient;
    //写一个恋爱心理大师的系统预设提示词
    private static final String SYSTEM_PROMPT = """
            你是一名拥有10年经验的资深恋爱心理学专家，擅长结合依恋理论、认知行为疗法与情感教练技术，
            帮助用户解析亲密关系中的困惑。你的风格融合学术严谨性与人性化关怀，
            能以共情态度引导用户自我觉察。
            1.深度问题分析
            -通过开放式提问挖掘用户真实需求（如："你希望这段关系达到什么样的状态？"） 
            -运用「情感需求冰山模型」区分表面矛盾与核心诉求（安全感/价值感/归属感缺失）    
            2.科学工具库调用
            -根据场景自动匹配心理学模型：
                ① 关系阶段诊断→使用「Murstein恋爱过滤器理论」
                ② 沟通冲突→引用「非暴力沟通四要素」
                ③ 吸引力提升→结合「相似-互补平衡法则」
            3.定制化策略生成      
            -提供「3层级建议」：
                ① 即时情绪安抚话术
                ② 3日内可执行行动计划（如"关系重启30分钟对话清单"）
                ③ 长期关系建设指南
            """;

    public LoveApp(ChatModel dashScopeChatModel) {
        // 初始化基于文件的对话记忆
        String fileDir = System.getProperty("user.dir") + "/temp/chat-memory";
//        FileBaseChatMemory chatMemory = new FileBaseChatMemory(fileDir);
        InMemoryChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new MyLoggerAdvisor()
//                        new ReReadingAdvisor() //可以增强大模型的效果，按需开启

                )

                .build();
    }

    /**
     * 带记忆的聊天
     *
     * @param question
     * @param memoryId
     * @return String
     */

    public String doChat(String question, String memoryId) {
        ChatResponse chatResponse = chatClient.prompt()
                .user(question)
                .advisors(spe -> spe.param("chat_memory_conversation_id", memoryId)
                        .param("chat_memory_response_size", 10))
                .call()
                .chatResponse();
        return chatResponse.getResult().getOutput().getText();

    }

    /**
     * 结构化输出生成报告
     *
     * @param title
     * @param suggestions
     */
    record LoveReport(String title, List<String> suggestions) {
    }

    public LoveReport doChatWithReport(String question, String memoryId) {
        LoveReport chatResponse = chatClient.prompt()
                .user(question)
                .system(SYSTEM_PROMPT + "每次对话都要生成结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .advisors(spe -> spe.param("chat_memory_conversation_id", memoryId)
                        .param("chat_memory_response_size", 10))
                .call()
                .entity(LoveReport.class);
        log.info("LoveReport:{}", chatResponse);

        return chatResponse;

    }

    @Resource
    private ToolCallback[] allTools;

    /**
     * 带工具
     * @param question
     * @param memoryId
     * @return
     */

    public String doChatWithTools(String question, String memoryId) {
        ChatResponse response = chatClient.prompt()
                .user(question)
                .advisors(spe -> spe.param("chat_memory_conversation_id", memoryId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .tools(allTools)
                .call()
                .chatResponse();
        String text = response.getResult().getOutput().getText();
        log.info("ChatResponse:{}", text);
        return text;
    }


    /**
     * 使用mcp服务
     */

    @Resource
    private ToolCallbackProvider toolCallbackProvider;
    public String doChatWithMcp(String question, String memoryId) {
        ChatResponse response = chatClient.prompt()
                .user(question)
                .advisors(spe -> spe.param("chat_memory_conversation_id", memoryId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .tools(toolCallbackProvider)
                .call()
                .chatResponse();
        String text = response.getResult().getOutput().getText();
        log.info("ChatResponse:{}", text);
        return text;
    }

    public Flux<String> doChatWithStream(String question, String memoryId) {
        return  chatClient.prompt()
                .user(question)
                .advisors(spe -> spe.param("chat_memory_conversation_id", memoryId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .tools(allTools)
                .stream()
                .content();
    }



}
