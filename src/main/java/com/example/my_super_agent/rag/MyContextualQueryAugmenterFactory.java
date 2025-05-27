package com.example.my_super_agent.rag;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;

/**
 * 自定义上下文查询
 */
public class MyContextualQueryAugmenterFactory {

  public static ContextualQueryAugmenter create() {
      PromptTemplate promptTemplate = new PromptTemplate(
              """
              你应该输出一下内容：
              抱歉，我只能回答恋爱的问题，
              你可以询问客服 http://localhost:8080     
              """
      );
      return  ContextualQueryAugmenter.builder()
              .allowEmptyContext(true)
              .emptyContextPromptTemplate(promptTemplate) // 为空时显示的提示语
              .build();

  }
}
