package com.example.my_super_agent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * 网页抓取工具
 */
public class WebScrapingTool {

    @Tool(description = "Scrape content from a web page")
    public String scrape(@ToolParam(description = "URL to scrape") String url) {
        Logger logger = Logger.getLogger("WebScraper");

        if (url == null || url.isEmpty()) {
            return "Error scraping web page: Invalid URL";
        }

        try {
            // 校验URL格式
            new URL(url).toURI();

            // 设置User-Agent和超时时间
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();
            return document.html();
        } catch (MalformedURLException e) {
            logger.severe("Invalid URL provided: " + url + ", Error: " + e.getMessage());
            return "Error scraping web page: Invalid URL";
        } catch (IOException e) {
            logger.severe("IO error occurred while scraping URL: " + url + ", Error: " + e.getMessage());
            return "Error scraping web page: Network or IO error";
        } catch (Exception e) {
            logger.severe("Unexpected error occurred while scraping URL: " + url + ", Error: " + e.getMessage());
            return "Error scraping web page: Internal error";
        }

    }


}
