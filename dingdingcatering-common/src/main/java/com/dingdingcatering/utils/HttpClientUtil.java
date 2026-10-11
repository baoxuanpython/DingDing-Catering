package com.dingdingcatering.utils;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HTTP 请求工具类
 * <p>
 * 封装 Apache HttpClient，提供 GET、POST（表单）和 POST（JSON）三种请求方式，
 * 统一超时配置为 5 秒，适用于调用微信 API 等外部 HTTP 接口。
 */
@Slf4j
public class HttpClientUtil {

    /** 请求超时时间（毫秒） */
    static final int TIMEOUT_SEC = 5 * 1000;

    /**
     * 发送 GET 请求，参数拼接在 URL 后
     *
     * @param url      请求地址
     * @param paramMap 请求参数映射
     * @return 响应体内容，请求失败时返回空字符串
     */
    public static String doGet(String url, Map<String, String> paramMap) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        CloseableHttpResponse response = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            if (paramMap != null) {
                for (String key : paramMap.keySet()) {
                    builder.addParameter(key, paramMap.get(key));
                }
            }
            URI uri = builder.build();
            //创建GET请求
            HttpGet httpGet = new HttpGet(uri);
            //发送请求
            response = httpClient.execute(httpGet);
            //判断响应状态
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            log.error("http请求失败", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                log.info("关闭httpclient失败", e);
            }
        }
        return result;
    }

    /**
     * 发送 POST 请求，参数以表单形式编码（application/x-www-form-urlencoded）
     *
     * @param url      请求地址
     * @param paramMap 请求参数映射
     * @return 响应体内容
     * @throws IOException 请求或读取失败时抛出
     */
    public static String doPost(String url, Map<String, String> paramMap) throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString;
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (paramMap != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (Map.Entry<String, String> param : paramMap.entrySet()) {
                    paramList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            httpPost.setConfig(builderRequestConfig());
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
        return resultString;
    }

    /**
     * 发送 POST 请求，参数以 JSON 格式编码（application/json）
     *
     * @param url      请求地址
     * @param paramMap 请求参数映射，将被转换为 JSON 对象
     * @return 响应体内容
     * @throws IOException 请求或读取失败时抛出
     */
    public static String doPost4Json(String url, Map<String, String> paramMap) throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString;
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);

            if (paramMap != null) {
                //构造json格式数据
                JSONObject jsonObject = new JSONObject();
                jsonObject.putAll(paramMap);
                StringEntity entity = new StringEntity(jsonObject.toString(), "utf-8");
                //设置请求编码
                entity.setContentEncoding("utf-8");
                //设置数据类型
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            httpPost.setConfig(builderRequestConfig());
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return resultString;
    }

    private static RequestConfig builderRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(TIMEOUT_SEC)
                .setConnectionRequestTimeout(TIMEOUT_SEC)
                .setSocketTimeout(TIMEOUT_SEC).build();
    }
}
