package com.vmarketing.core.shiro;

import lombok.Data;

import java.io.Serializable;

// 登录成功返回用户的信息，相当一一个_dto载体
@Data
public class AccountProfile implements Serializable {

	private static final long serialVersionUID = 1L;

	// 用户id
	private Long id;

	// 用户name
	private String name;

	// 账号是否启用(0：否 1：是)
	private int enable;

	// 用户状态(0：正常 1：锁定)
	private int status;

}
