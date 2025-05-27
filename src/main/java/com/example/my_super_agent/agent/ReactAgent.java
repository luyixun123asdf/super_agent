package com.example.my_super_agent.agent;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基于 React(reasoning and acting)模式 的 Agent
 * 实现了思考-行动循环
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class ReactAgent extends BaseAgent {

    /**
     * 处理当前状态并决定下一步行动
     *
     * @return 是否需要执行行动，true表示需要执行，false表示不需要执行
     */
    public abstract boolean thinking();

    /**
     * 执行决定的行动
     *
     * @return 行动执行结果
     */
    public abstract String act();

    @Override
    public String step() {
        try {
            if (thinking()) {
                return act();
            }
            return "思考完成，无需行动";

        } catch (Exception e) {
            return "执行异常：" + e.getMessage();
        }finally {
            this.cleanup();
        }

    }

}
