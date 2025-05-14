package com.example.my_super_agent.app;


import com.example.my_super_agent.advisor.MyLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoveApp {


    private final ChatClient chatClient;
    //写一个恋爱心理大师的系统预设提示词
    private static final String SYSTEM_PROMPT= """
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

    public LoveApp(ChatModel dashScopeChatModel){
        InMemoryChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new MyLoggerAdvisor()
                )
                .build();
    }

    public String dochat(String question,  String memoryId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(question)
                .advisors(spe -> spe.param("chat_memory_conversation_id", memoryId)
                        .param("chat_memory_response_size", 10))
                .call()
                .chatResponse();
        return chatResponse.getResult().getOutput().getText();

    }




}
