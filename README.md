# My Super Agent

## 简介

My Super Agent 是一个基于 **Spring AI** 构建的超级智能体项目。它集成了 **Model Context Protocol (MCP)**、**RAG (Retrieval-Augmented Generation)** 以及多种实用工具，旨在提供强大的自动化和智能辅助能力。

本项目不仅支持通过大模型进行对话，还具备文件操作、PDF 生成、网络搜索与抓取、终端命令执行等能力，是一个功能丰富的 AI Agent 实现。

## 功能特性

- **核心 AI 能力**: 基于 Spring AI 和 Alibaba DashScope (通义千问) 模型。
- **RAG (检索增强生成)**: 使用 PostgreSQL + PGVector 实现向量存储和知识检索。
- **MCP (Model Context Protocol)**: 支持 MCP 客户端，可扩展连接多种 MCP 服务器。
- **内置工具集 (Tools)**:
    - **文件操作**: 读写文件、管理目录。
    - **PDF 生成**: 基于 iText 生成 PDF 文档。
    - **网络能力**: Web 搜索 (Search API) 和 网页抓取 (JSoup)。
    - **系统操作**: 终端命令执行 (Terminal Operation)。
    - **资源下载**: 下载网络资源。
- **API 文档**: 集成 Knife4j / Swagger，提供友好的接口调试界面。

## 技术栈

- **编程语言**: Java 21
- **框架**: Spring Boot 3.4.5, Spring AI 1.0.0-M6
- **数据库**: PostgreSQL (pgvector 插件)
- **AI 模型服务**: Alibaba DashScope
- **工具库**:
    - iText (PDF 处理)
    - JSoup (HTML 解析)
    - GitHub API
    - Hutool, Lombok
- **文档**: Knife4j, SpringDoc

## 快速开始

### 前置要求

1.  **JDK 21+**: 确保本地安装了 Java 21 或更高版本。
2.  **Maven 3.x**: 用于项目构建。
3.  **PostgreSQL**: 安装 PostgreSQL 并启用 `vector` 插件。
    ```sql
    CREATE EXTENSION vector;
    ```
4.  **API Keys**:
    - Alibaba DashScope API Key
    - Search API Key (如果使用了搜索功能)

### 配置说明

修改 `src/main/resources/application.yaml` 文件，配置数据库和 API Key：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/my_agent # 替换为你的数据库地址
    username: your_username
    password: your_password
  ai:
    dashscope:
      api-key: ${spring.ai.dashscope.api-key} # 或直接填入 key
    mcp:
      client:
        stdio:
          servers-configuration: classpath:mcp-servers.json # MCP 服务器配置

searchApi:
  apiKey: ${searchApi.apiKey} # 搜索 API Key
```

### 运行项目

在项目根目录下执行：

```bash
mvn spring-boot:run
```

或者使用 IDE (IntelliJ IDEA) 直接运行 `MySuperAgentApplication` 类。

### 访问接口文档

项目启动后 (默认端口 8123)，访问以下地址查看 API 文档：

- Knife4j 文档: `http://localhost:8123/doc.html`
- Swagger UI: `http://localhost:8123/swagger-ui.html`

## 目录结构

```
src/main/java/com/example/my_super_agent
├── advisor       # Spring AI Advisors
├── agent         # Agent 核心逻辑
├── chatmemory    # 对话记忆实现
├── config        # 配置类
├── controller    # Web 接口 (AiController)
├── rag           # RAG 相关 (向量存储、检索)
├── tools         # Agent 工具集 (File, PDF, Web, Terminal 等)
└── MySuperAgentApplication.java # 启动类
```

## 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

## 许可证

[MIT License](LICENSE)
