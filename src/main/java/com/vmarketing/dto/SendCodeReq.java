package com.vmarketing.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.vmarketing.core.constant.CacheConstant;
import org.springframework.beans.factory.annotation.Autowired;

import com.vmarketing.core.db.RedisClient;

import lombok.Data;

//发送验证码参数校验类
@Data
public class SendCodeReq implements Serializable {

	@Autowired
	RedisClient redis;

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "账号不能为空")
	private String account;

	private int code;

	public String redis_key;

	/**
	 * 将 code 验证码写入到 redis
	 *
	 * @param account
	 * @param code
	 * @return
	 */
	public boolean setRedis_key(String account, int code) {
		return redis.set(CacheConstant.SYS_ACCOUNT_CODE + account + account, code);
	}

}
