package com.example.my_super_agent.app;

import jakarta.annotation.Resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoveAppTest {
    @Resource
    private LoveApp loveApp;

    @Test
    void dochat() {
        String s = loveApp.doChat("你好,我叫不想写bug", "1");
        Assertions.assertNotNull(s);

    }

    @Test
    void doChat() {
    }

    @Test
    void doChatWithReport() {
        LoveApp.LoveReport s = loveApp.doChatWithReport("你好,我叫zgb，我想让另一半（小珍珍）更喜欢我，但是不知道怎么办", "2");
        Assertions.assertNotNull(s);
    }

    @Test
    void doChatWithTools() {

//        String s = loveApp.doChatWithTools("周末想带女朋友去上海约会，推荐几个适合情侣的小众打卡地？", "2");
        // 测试 PDF 生成
//        final String s1 = loveApp.doChatWithTools("生成一份‘七夕约会计划’PDF，包含餐厅预订、活动流程和礼物清单", "2");

        loveApp.doChatWithTools("执行 Python 脚本来生成数据分析报告", "2");
//        loveApp.doChatWithTools("保存我的恋爱档案为文件", "2");


    }

    @Test
    void doChatWithMcp() {
//        String s = loveApp.doChatWithMcp("周末想带女朋友去上海静安区约会，推荐附近5公里内适合的约会地点", "2");
//        Assertions.assertNotNull(s);
        // 图片搜索mcp
        String s1 = loveApp.doChatWithMcp("帮我搜索一些哄另一半开心的图片", "2");
        Assertions.assertNotNull(s1);
    }

}