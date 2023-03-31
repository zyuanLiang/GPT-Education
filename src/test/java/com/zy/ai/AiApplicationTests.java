package com.zy.ai;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.model.Model;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import retrofit2.Retrofit;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.List;

import static com.theokanning.openai.service.OpenAiService.*;

@SpringBootTest
class AiApplicationTests {
	private static final String ENDPOINT_URL = "http://api.openai.com/v1/engines/davinci-codex/completions";
	private static final String clash="https://sub-api.ohmy.cat";
	private static final String token = "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";

	private static final String PROXY_HOST = "127.0.0.1";
	private static final int PROXY_PORT = 7890;

	@Test
	void contextLoads() {
		var openAiService = new OpenAiService(token);
		List<Model> models = openAiService.listModels();
		System.out.println("models:"+models);
	}

	@Test
	void okHttp(){
//		System.setProperty("http.proxyHost", PROXY_HOST);
//		System.setProperty("http.proxyPort", String.valueOf(PROXY_PORT));
//		System.setProperty("java.net.useSystemProxies", "true"); 不生效

		System.out.println(System.getProperty("http.proxyHost"));
		System.out.println(System.getProperty("http.proxyPort"));
		ObjectMapper mapper = defaultObjectMapper();
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT));
		OkHttpClient client = defaultClient(token, Duration.ofMillis(10000))
				.newBuilder()
				.proxy(proxy)
				.build();
		Retrofit retrofit = defaultRetrofit(client, mapper);
		OpenAiApi api = retrofit.create(OpenAiApi.class);
		OpenAiService service = new OpenAiService(api);
		List<Model> models = service.listModels();
		System.out.println(models);
		System.out.println(System.getProperty("http.proxyHost"));
		System.out.println(System.getProperty("http.proxyPort"));
	}

	@Test
	void hutool(){
		String execute = HttpRequest.post(ENDPOINT_URL).setHttpProxy("127.0.0.0", 7890).body("").execute().body();
		System.out.println(execute);
	}

}
