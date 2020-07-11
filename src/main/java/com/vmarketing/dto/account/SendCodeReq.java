package com.vmarketing.dto.account;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;

import com.vmarketing.core.db.RedisClient;

import lombok.Data;

@Data
public class SendCodeReq implements Serializable {

	@Autowired
	RedisClient redis;

	private static final long serialVersionUID = 1L;

	private String finder = "code";

	@NotBlank(message = "账号不能为空")
	private String account;

	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String redis_key;

	/**
	 * 从redis获取验证码
	 * 
	 * @return
	 */
	public String getRedis_key(String account) {
		return (String) redis.get(this.finder + account);
	}

	/**
	 * 将code验证码写入到
	 * 
	 * @param redis_key
	 * @return
	 */
	public boolean setRedis_key(String account, int code) {
		return redis.set(this.finder + account, code);
	}

}
