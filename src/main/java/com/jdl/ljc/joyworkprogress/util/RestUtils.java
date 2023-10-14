package com.jdl.ljc.joyworkprogress.util;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.jdl.ljc.joyworkprogress.domain.dto.ResultDto;
import com.jdl.ljc.joyworkprogress.domain.dto.WpsDto;
import org.apache.hc.client5.http.async.methods.SimpleBody;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

import java.io.IOException;

public class RestUtils {
    public static final String domain = "localhost";
    public static <T> ResultDto<T> post(Class<T> resultValueClazz, String requestPath, Object param) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = String.format("http://%s%s", domain, requestPath);
            HttpEntity postEntity = new StringEntity(JSON.toJSONString(param));
            ClassicHttpRequest httpPost = ClassicRequestBuilder.post(url).setEntity(postEntity).build();
            return httpClient.execute(httpPost, response -> {
                String responseText = EntityUtils.toString(response.getEntity());
                ResultDto<T> resultDto = JSON.parseObject(responseText, new TypeReference<ResultDto<T>>() {
                });
                JSONObject obj = (JSONObject) resultDto.getResultValue();
                resultDto.setResultValue(obj.toJavaObject(resultValueClazz));
                return resultDto;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 异步网络请求
     * @param requestPath
     * @param param
     * @param callback
     */
    public static void post(String requestPath, Object param, FutureCallback<SimpleHttpResponse> callback) {
        try (CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault()) {
            httpClient.start();

            String url = String.format("http://%s%s", domain, requestPath);
            SimpleHttpRequest request = SimpleRequestBuilder.post(url).build();
            if (param!=null) {
                request.setBody(JSON.toJSONString(param), ContentType.APPLICATION_JSON);
            }
            httpClient.execute(request,callback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
