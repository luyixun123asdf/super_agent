package com.example.my_super_agent.controller;

import com.example.my_super_agent.agent.LuAgent;
import com.example.my_super_agent.app.LoveApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;



@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private LoveApp loveApp;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;


    /**
     * 第一种方式Flux<String>进行 流式响应
     */
    @GetMapping(value = "/chatStream/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithSse(String question, String memoryId) {
        return loveApp.doChatWithStream(question, memoryId);
    }

//    /**
//     * 第二种方式SentEvent进行 流式响应
//     */
//    @GetMapping(value = "/chatStream/sse/SentEvent")
//    public Flux<ServerSentEvent<String>> chatWithSseSentEvent(String question, String memoryId) {
//        return loveApp.doChatWithStream(question, memoryId)
//                .map(text -> ServerSentEvent.builder(text).build());
//    }
//
//
//    /**
//     * 第二种方式Emitter进行 流式响应
//     */
//    @GetMapping(value = "/chatStream/sse/SseEmitter")
//    public SseEmitter chatWithSseEmitter(String question, String memoryId) {
//        // 创建超时时间
//        SseEmitter sseEmitter = new SseEmitter(180000L);
//        loveApp.doChatWithStream(question, memoryId)
//                .subscribe(chunk -> {
//                    try {
//                        sseEmitter.send(chunk);
//                    } catch (Exception e) {
//                        sseEmitter.completeWithError(e);
//                    }
//                },sseEmitter::completeWithError,  sseEmitter::complete);
//
//        return  sseEmitter;
//    }

    /**
     * 流式调用智能体
     * @param question
     * @return
     */

    @GetMapping("/chat/agent")
    public SseEmitter chatWithAgent(String question) {
        LuAgent luAgent = new LuAgent(allTools, dashscopeChatModel);
        return luAgent.runStream(question);

    }


}
