package com.vmarketing.core.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginDto implements Serializable {

	/**
	 * serialVersionUID 用来表明类的不同版本间的兼容性
	 * 当一个类实现了Serializable接口，如果没有定义serialVersionUID，Eclipse会提供这个提示功能告诉你去定义
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "用户名不能为空")
	private String name;

	@NotBlank(message = "密码不能为空")
	private String password;
}
