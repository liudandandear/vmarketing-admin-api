package com.vmarketing.core.sdk.sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class upyun {

	public void send() throws IOException {
		URL url = new URL("https://sms-api.upyun.com/api/messages");

		//设置 JSON 参数
		JSONObject object = new JSONObject();
		object.put("template_id", 1);
		object.put("mobile", "135xxxxxxxx");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置必要参数
		conn.setConnectTimeout(10000);
		conn.setUseCaches(false);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Authorization", "<YOUR_TOKEN>");
		conn.setRequestProperty("Content-type", "application/json");

		// 创建链接
		conn.connect();
		OutputStream os = conn.getOutputStream();
		os.write(object.toString().getBytes("UTF-8"));

		//Gets the status code from an HTTP response message
		int code = conn.getResponseCode();

		InputStreamReader reader = new InputStreamReader(conn.getInputStream());
		BufferedReader br = new BufferedReader(reader);
		char[] chars = new char[1024];
		int length = 0;
		StringBuilder result = new StringBuilder();
		while ((length = br.read(chars)) != -1) {
		    result.append(chars, 0, length);
		}
		System.out.println("code:" + code + "::" + result.toString());
	}
}
