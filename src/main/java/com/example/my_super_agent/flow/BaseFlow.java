package com.example.my_super_agent.flow;

import com.example.my_super_agent.agent.BaseAgent;
import lombok.Data;
import org.springframework.ai.model.Model;


import java.util.*;


@Data
public abstract class BaseFlow {

    protected final Map<String, BaseAgent> agents;
    protected List<Object> tools;
    protected String primaryAgentKey;

    // 处理不同构造参数的重载构造方法
    public BaseFlow(BaseAgent agent) {
        this.agents = new LinkedHashMap<>();
        this.agents.put("default", agent);
        this.primaryAgentKey = "default";
        this.tools = new ArrayList<>();
    }

    public BaseFlow(List<BaseAgent> agents) {
        this.agents = new LinkedHashMap<>();
        for (int i = 0; i < agents.size(); i++) {
            this.agents.put("agent_" + i, agents.get(i));
        }
        if (!agents.isEmpty()) {
            this.primaryAgentKey = this.agents.keySet().iterator().next();
        }
        this.tools = new ArrayList<>();
    }

    public BaseFlow(Map<String, BaseAgent> agents) {
        this.agents = new LinkedHashMap<>(agents);
        if (!agents.isEmpty()) {
            this.primaryAgentKey = agents.keySet().iterator().next();
        }
        this.tools = new ArrayList<>();
    }

    // 获取指定代理
    public BaseAgent getAgent(String key) {
        return agents.get(key);
    }

    // 添加新代理
    public void addAgent(String key, BaseAgent agent) {
        agents.put(key, agent);
        if (primaryAgentKey == null) {
            primaryAgentKey = key;
        }
    }

    // 异步执行方法
    public abstract String execute(String inputText);


    // 工具配置（可选扩展）
    public void setTools(List<Object> tools) {
        this.tools = tools;
    }

    public List<Object> getTools() {
        return Collections.unmodifiableList(tools);
    }



}
