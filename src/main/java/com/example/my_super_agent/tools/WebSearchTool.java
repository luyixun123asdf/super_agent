package com.example.my_super_agent.tools;

import cn.hutool.http.HttpUtil;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;


import java.util.HashMap;


/**
 * 网页搜索工具
 */
public class WebSearchTool {
    private static final String BASE_URL = "https://www.searchapi.io/api/v1/search";

    private final String apiKey;

    public WebSearchTool(String apiKey) {
        this.apiKey = apiKey;
    }

    @Tool(description = "Search for information for Baidu Search Engine ")
    public String WebSearch(@ToolParam(description = "WebSearch query keyword") String query) {
        HashMap<String, Object> map = new HashMap<>(4);
        map.put("q", query);
        map.put("engine", "baidu");
        map.put("api_key", apiKey);
        try {
            String response = HttpUtil.get(BASE_URL, map);
            JSONObject entries = JSONUtil.parseObj(response);
            JSONArray jsonArray = new JSONArray();
            if (entries.containsKey("organic_results")) {
                jsonArray = entries.getJSONArray("organic_results");
            }
            if (entries.containsKey("answer_box")){
                jsonArray.put(entries.get("answer_box"));
            }
            if (jsonArray.size()<=0){
                return (String) JSONUtil.parseObj(entries.get("pagination")).get("next");
            }

            int endIndex = Math.min(jsonArray.size(), 5);
            JSONArray resultArray = new JSONArray();

            for (int i = 0; i < endIndex; i++) {
                resultArray.add(jsonArray.getJSONObject(i));
            }

            return resultArray.toString();

        } catch (JSONException e) {
            // 可选：使用日志框架记录异常
            // log.error("JSON parsing error: ", e);
            return "Error Search Engine: Invalid JSON format - " + e.getMessage();
        } catch (Exception e) {
            // log.error("Unexpected error during web search: ", e);
            return "Error Search Engine: " + e.getMessage();
        }


    }
}
