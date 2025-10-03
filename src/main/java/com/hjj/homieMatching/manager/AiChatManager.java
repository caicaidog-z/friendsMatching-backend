package com.hjj.homieMatching.manager;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class AiChatManager {
    @Value("${aliyun.apiKey}")
    private String apiKey;

    // 超时时间设置(秒)
    private static final int TIMEOUT_SECONDS = 60;

    private GenerationParam buildGenerationParam(Message userMsg) {
        return GenerationParam.builder()
                .apiKey(apiKey)
                .model("deepseek-v3.2-exp")
                .enableThinking(false)
                .incrementalOutput(true)
                .resultFormat("message")
                .messages(Arrays.asList(userMsg))
                .build();
    }

    /**
     * 流式调用并收集结果
     * @param gen Generation实例
     * @param userMsg 用户消息
     * @return AI的完整响应结果
     */
    private String streamCallAndCollectResult(Generation gen, Message userMsg)
            throws NoApiKeyException, ApiException, InputRequiredException, InterruptedException {
        GenerationParam param = buildGenerationParam(userMsg);
        Flowable<GenerationResult> result = gen.streamCall(param);

        // 用于收集结果
        StringBuilder fullResponse = new StringBuilder();
        // 用于处理异步等待
        CountDownLatch latch = new CountDownLatch(1);

        result.subscribe(
                // 处理每个返回的片段
                message -> {
                    String content = message.getOutput().getChoices().get(0).getMessage().getContent();
                    if (content != null && !content.isEmpty()) {
                        fullResponse.append(content);
                    }
                },
                // 处理错误
                error -> {
                    System.err.println("Stream error: " + error.getMessage());
                    latch.countDown();
                },
                // 完成时
                latch::countDown
        );

        // 等待完成或超时
        boolean completed = latch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        if (!completed) {
            throw new RuntimeException("AI响应超时，超过" + TIMEOUT_SECONDS + "秒未完成");
        }

        return fullResponse.toString();
    }

    /**
     * 发送消息并获取AI的响应结果
     * @param question 用户的问题
     * @return AI返回的结果
     */
    public String sendMessage(String question) {
        try {
            Generation gen = new Generation();
            Message userMsg = Message.builder()
                    .role(Role.USER.getValue())
                    .content(question)
                    .build();

            // 调用并返回结果
            return streamCallAndCollectResult(gen, userMsg);

        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            System.err.println("API调用异常: " + e.getMessage());
            return "抱歉，处理您的请求时发生错误: " + e.getMessage();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("处理被中断: " + e.getMessage());
            return "抱歉，处理被中断，请重试";
        } catch (Exception e) {
            System.err.println("发生未知错误: " + e.getMessage());
            return "抱歉，发生未知错误，请稍后重试";
        }
    }
}
