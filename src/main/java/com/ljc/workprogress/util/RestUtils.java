package com.ljc.workprogress.util;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.ljc.workprogress.domain.dto.ResultDto;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RestUtils {
    public static String get(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            ClassicHttpRequest httpRequest = ClassicRequestBuilder.get(url).build();
            httpRequest.addHeader("Content-Type", ContentType.APPLICATION_JSON);
            return httpClient.execute(httpRequest, response -> EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> ResultDto<T> post(Class<T> resultValueClazz, String requestPath, Object param) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = String.format("http://%s%s", AppUtils.getDomain(), requestPath);
            HttpEntity postEntity = new StringEntity(JSON.toJSONString(param), StandardCharsets.UTF_8);
            ClassicHttpRequest httpPost = ClassicRequestBuilder.post(url).setEntity(postEntity).build();
            httpPost.addHeader("Content-Type", ContentType.APPLICATION_JSON);
            return httpClient.execute(httpPost, response -> {
                String responseText = EntityUtils.toString(response.getEntity());
                return JSON.parseObject(responseText, new TypeReference<ResultDto<T>>(resultValueClazz) {
                });
            });
        } catch (IOException e) {
            return ResultDto.fail(e.getMessage());
        }
    }


    @SuppressWarnings("unused")
    public static <T> ResultDto<List<T>> postList(Class<T> resultValueClazz, String requestPath, Object param) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = String.format("http://%s%s", AppUtils.getDomain(), requestPath);
            HttpEntity postEntity = new StringEntity(JSON.toJSONString(param), StandardCharsets.UTF_8);
            ClassicHttpRequest httpPost = ClassicRequestBuilder.post(url).setEntity(postEntity).build();
            httpPost.addHeader("Content-Type", ContentType.APPLICATION_JSON);
            return httpClient.execute(httpPost, response -> {
                String responseText = EntityUtils.toString(response.getEntity());
                return JSON.parseObject(responseText, new TypeReference<ResultDto<List<T>>>(resultValueClazz) {
                });
            });
        } catch (IOException e) {
            return ResultDto.fail(e.getMessage());
        }
    }

}
