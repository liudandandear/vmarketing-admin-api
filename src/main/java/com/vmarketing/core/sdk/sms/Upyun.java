package com.vmarketing.core.sdk.sms;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import com.vmarketing.core.util.JwtUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Upyun {

    public static String token;

    @Value("${config.upyun-sms-Token}")
    public void setToken(String token) {
        Upyun.token = token;
    }

    /**
     * 发送验证码
     *
     * @param phone
     * @param vars
     * @return
     * @throws IOException
     */
    public boolean send(String phone, String vars) throws IOException {
        URL url = new URL("https://sms-api.upyun.com/api/messages");

        //设置 JSON 参数
        JSONObject object = new JSONObject();
        // 模板ID
        object.put("template_id", 5129);
        // 手机号
        object.put("mobile", phone);
        object.put("vars", vars);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置连接方式：post
        conn.setRequestMethod("POST");
        // 设置连接主机服务器的超时时间 10000毫秒
        conn.setConnectTimeout(10000);
        // 设置读取远程返回的数据时间：60000毫秒
        conn.setReadTimeout(60000);
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Authorization", token);
        conn.setRequestProperty("Content-type", "application/json");

        // 创建链接，发送请求
        conn.connect();
        OutputStream os = conn.getOutputStream();
        os.write(object.toString().getBytes("UTF-8"));

        // 又拍云返回的状态码
        int code = conn.getResponseCode();

        InputStreamReader reader = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(reader);
        char[] chars = new char[1024];
        int length = 0;
        StringBuilder result = new StringBuilder();
        while ((length = br.read(chars)) != -1) {
            result.append(chars, 0, length);
        }
        System.out.println("upyun短信返回结果：" + "code:" + code + "::" + result.toString());
        if (code == 200) {
            return true;
        }
        return false;
    }
}
