package com.example.my_super_agent.agent;

import ch.qos.logback.core.util.StringUtil;
import com.example.my_super_agent.agent.model.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 抽象基础代理类，用于管理代理状态和执行流程
 * <p>
 * 提供状态转换、内容管理和基于步骤的执行循环的基础功能
 * 子类必须实现step方法
 */

@Data
@Slf4j
public abstract class BaseAgent {

    // 核心属性
    private String name;

    // 提示词
    private String systemPrompt;
    private String nextPrompt;

    // 代理状态
    private AgentState state = AgentState.IDLE;

    // 执行步骤控制
    private int currentStep = 0;
    private int maxSteps = 6;

    // LLM大模型
    private ChatClient chatClient;

    // Memory记忆，自主维护
    private List<Message> messageList = new ArrayList<>(16);


    /**
     * 运行代理
     *
     * @param userPrompt 用户输入
     * @return
     */
    public String run(String userPrompt) {
        if (state != AgentState.IDLE) {
            return "Cannot run agent from state";
        }
        if (StringUtil.isNullOrEmpty(userPrompt)) {
            return "Cannot run agent with empty user prompt";
        }
        // 执行
        this.state = AgentState.RUNNING;
        // 记录信息上下文
        messageList.add(new UserMessage(userPrompt));
        // 执行结果保存
        ArrayList<String> resultList = new ArrayList<>(16);
        // 执行循环
        try {
            while (currentStep < maxSteps && state != AgentState.FINISHED) {
                currentStep++;
                log.info("Running step {}/{}", currentStep, maxSteps);
                // 单步执行
                String result = step();
                resultList.add("Step " + currentStep + ":" + result);

                if (currentStep >= maxSteps) {
                    state = AgentState.FINISHED;
                    resultList.add("Max steps reached. Stopping execution.");
                }
            }
            return String.join("\n", resultList);

        } catch (Exception e) {
            state = AgentState.ERROR;
            log.info("Error running agent: " + e.getMessage());
            return "Error running agent: " + e.getMessage();

        } finally {
            this.cleanup();
        }

    }

    /**
     * 运行代理,流式输出
     *
     * @param userPrompt 用户输入
     * @return
     */
    public SseEmitter runStream(String userPrompt) {
        SseEmitter sseEmitter = new SseEmitter(180000L);
        // 使用线程异步处理，避免阻塞主线程
        CompletableFuture.runAsync(() -> {
            try {

                if (state != AgentState.IDLE) {
//                return "Cannot run agent from state";
                    sseEmitter.send("Cannot run agent from state");
                    sseEmitter.complete();
                    return;
                }
                if (StringUtil.isNullOrEmpty(userPrompt)) {
//                return "Cannot run agent with empty user prompt";
//                throw new RuntimeException("Cannot run agent with empty user prompt");
                    sseEmitter.send(" Cannot run agent with empty user prompt");
                    sseEmitter.complete();
                    return;
                }

            } catch (Exception e) {
                sseEmitter.completeWithError(e);
            }
            // 执行
            this.state = AgentState.RUNNING;
            // 记录信息上下文
            messageList.add(new UserMessage(userPrompt));
            // 执行结果保存
            ArrayList<String> resultList = new ArrayList<>(16);
            // 执行循环
            try {
                while (currentStep < maxSteps && state != AgentState.FINISHED) {
                    currentStep++;
                    log.info("Running step {}/{}", currentStep, maxSteps);
                    // 单步执行
                    String result = step();
                    resultList.add("Step " + currentStep + ":" + result);
                    sseEmitter.send(result);

                    if (currentStep >= maxSteps) {
                        state = AgentState.FINISHED;
                        resultList.add("Max steps reached. Stopping execution.");
                        sseEmitter.send("执行结束，达到最大步数" + maxSteps);
                    }
                }
                sseEmitter.send(messageList);
                sseEmitter.send("执行结束");

            } catch (Exception e) {
                state = AgentState.ERROR;
                log.info("Error running agent: " + e.getMessage());
                try {
                    sseEmitter.send("Error running agent: " + e.getMessage());
                    sseEmitter.complete();
                } catch (IOException ex) {
                    sseEmitter.completeWithError(ex);
                }
//                return "Error running agent: " + e.getMessage();

            } finally {
                this.cleanup();
            }

        });
        // 设置超时处理
        sseEmitter.onTimeout(() -> {
            this.state = AgentState.ERROR;
            this.cleanup();
            log.info("SSE   Timeout ...");
            sseEmitter.complete();

        });
        // 设置完成处理
        sseEmitter.onCompletion(() -> {
            if (state == AgentState.RUNNING) {
                this.state = AgentState.FINISHED;
            }
            this.cleanup();
            log.info("SSE   Completion ...");
        });
        return sseEmitter;


    }

    /**
     * 定义单个步骤
     *
     * @return
     */
    public abstract String step();

    /**
     * 清理资源
     */
    protected void cleanup() {
        // 子类可以重写此方法来清理资源
        log.info("Cleaning up resources...");
    }
}
