package com.example.my_super_agent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.output.Response;
import jakarta.annotation.Resource;

public class Langchain4jModel {
    public static void main(String[] args) {
//        WanxImageModel wanxImageModel = WanxImageModel.builder()
//                .modelName("wanx2.1-t2i-plus")
//                .apiKey(Api_keyEnum.getValueByName("DASH_SCOPE_API_KEY"))
//                .build();
//        Response<Image> response = wanxImageModel.generate("美女");
//        System.out.println(response.content().url());
        QwenChatModel model = QwenChatModel.builder()
                .apiKey(Api_keyEnum.DASH_SCOPE_API_KEY1)
                .modelName("qwen-max")
                .build();
        String chat = model.chat("你好");
        System.out.println(chat);
    }



}
