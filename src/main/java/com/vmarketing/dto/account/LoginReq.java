package com.vmarketing.dto.account;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 登录请求接口参数校验
 * 
 * @author liudandan
 *
 */
@Data
public class LoginReq implements Serializable {

	/**
	 * serialVersionUID 用来表明类的不同版本间的兼容性
	 * 当一个类实现了Serializable接口，如果没有定义serialVersionUID，Eclipse会提供这个提示功能告诉你去定义
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "账号不能为空")
	private String account;

	@NotBlank(message = "密码不能为空")
	private String password;
}
