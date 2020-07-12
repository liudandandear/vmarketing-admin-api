package com.vmarketing.dto.account;

import lombok.Data;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;

import com.vmarketing.core.db.RedisClient;

import java.io.Serializable;

///注册请求接口参数校验类
@Data
public class RegisterReq implements Serializable {

	@Autowired
	static RedisClient redis;

	/**
	 * serialVersionUID 用来表明类的不同版本间的兼容性
	 * 当一个类实现了Serializable接口，如果没有定义serialVersionUID，Eclipse会提供这个提示功能告诉你去定义
	 */
	private static final long serialVersionUID = 1L;

	private String finder = "code";

	@NotBlank(message = "账号不能为空")
	private String account;

	@NotBlank(message = "验证码不能为空")
	private Integer code;

	@NotBlank(message = "密码不能为空")
	private String password;

	/**
	 * 根据 account 获取存储code验证码的 key
	 * 
	 * @param account
	 * @return
	 */
	public String getCodeKey(String account)
	{
		return (String) redis.get(this.finder + account);
	}

	/**
	 * 将 code 验证码写入到 redis
	 * 
	 * @param redis_key
	 * @return
	 */
	public boolean setRedis_key(String account, int code)
	{
		return redis.set(this.finder + account, code);
	}
}
