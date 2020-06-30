package com.vmarketing.core.shiro;

import org.apache.shiro.authc.AuthenticationToken;

// shiro默认是supports的是UsernamePasswordToken,因为我们采用的是jwt的方式，所以在这里自定义一个JwtToken，
// 来完成shiro的supports方法
public class JwtToken implements AuthenticationToken {

	private static final long serialVersionUID = 1L;
	
	private String token;

	public JwtToken(String jwt) {
		this.token = jwt;
	}

	@Override
	public Object getPrincipal() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}
}
