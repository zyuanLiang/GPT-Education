package com.zy.ai;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;

public class ChatGPTClient {
    private static final String ENDPOINT_URL = "https://api.openai.com/v1/engines/davinci-codex/completions";
    private static final String API_KEY = "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";
    private static final String PROXY_HOST = "127.0.0.1";
    private static final int PROXY_PORT = 7890;

    public static void main(String[] args) {
        String text = "Hello, how are you?";
        try {
            String result = completeText(text);
            System.out.println("Response from API: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String completeText(String text) throws Exception {
        System.setProperty("http.proxyHost", PROXY_HOST);
        System.setProperty("http.proxyPort", String.valueOf(PROXY_PORT));

        HttpRequest request = HttpRequest.post(ENDPOINT_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .timeout(500000)
                .body("{\"prompt\": \"" + text + "\", \"max_tokens\": 50, \"temperature\": 0.5}");
        HttpResponse response = request.execute();
        if (response.getStatus() != 200) {
            throw new Exception(StrUtil.format("Request failed with status code {}", response.getStatus()));
        }
        return response.body();
    }
}
