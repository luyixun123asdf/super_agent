
package com.example.my_super_agent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;

/**
 * 自定义日志拦截器
 *
 * @author: lyx
 */

public class MyLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MyLoggerAdvisor.class);

    public String getName() {
        log.info("MyLoggerAdvisor");
        return this.getClass().getSimpleName();
    }



    public int getOrder() {
        return 0;
    }

    private AdvisedRequest before(AdvisedRequest request) {
        log.info("request: {}", request.userText());
        return request;
    }

    private void observeAfter(AdvisedResponse advisedResponse) {
        log.info("response: {}", advisedResponse.response().getResult().getOutput().getText());
    }


    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        advisedRequest = this.before(advisedRequest);
        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);
        this.observeAfter(advisedResponse);
        return advisedResponse;
    }


    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        advisedRequest = this.before(advisedRequest);
        Flux<AdvisedResponse> advisedResponses = chain.nextAroundStream(advisedRequest);
        return (new MessageAggregator()).aggregateAdvisedResponse(advisedResponses, this::observeAfter);
    }
}
