package com.jdl.ljc.joyworkprogress.util;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.jdl.ljc.joyworkprogress.domain.dto.ResultDto;
import com.jdl.ljc.joyworkprogress.domain.dto.WpsDto;
import com.jdl.ljc.joyworkprogress.domain.vo.WpsQueryDto;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

import java.io.IOException;
import java.util.List;

public class RestUtils {
    public static final String domain = "localhost";
    public static <T> ResultDto<T> post(Class<T> resultValueClazz, String requestPath, Object param) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = String.format("http://%s%s", domain, requestPath);
            HttpEntity postEntity = new StringEntity(JSON.toJSONString(param));
            ClassicHttpRequest httpPost = ClassicRequestBuilder.post(url).setEntity(postEntity).build();
            httpPost.addHeader("Content-Type",ContentType.APPLICATION_JSON);
            return httpClient.execute(httpPost, response -> {
                String responseText = EntityUtils.toString(response.getEntity());
                ResultDto<T> resultDto = JSON.parseObject(responseText, new TypeReference<ResultDto<T>>(resultValueClazz) {
                });
                return resultDto;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> ResultDto<List<T>> postList(Class<T> resultValueClazz, String requestPath, Object param) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = String.format("http://%s%s", domain, requestPath);
            HttpEntity postEntity = new StringEntity(JSON.toJSONString(param));
            ClassicHttpRequest httpPost = ClassicRequestBuilder.post(url).setEntity(postEntity).build();
            httpPost.addHeader("Content-Type",ContentType.APPLICATION_JSON);
            return httpClient.execute(httpPost, response -> {
                String responseText = EntityUtils.toString(response.getEntity());
                ResultDto<List<T>> resultDto = JSON.parseObject(responseText, new TypeReference<ResultDto<List<T>>>(resultValueClazz) {
                });
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
            request.addHeader("Content-Type",ContentType.APPLICATION_JSON);
            if (param!=null) {
                request.setBody(JSON.toJSONString(param), ContentType.APPLICATION_JSON);
            }
            httpClient.execute(request,callback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
