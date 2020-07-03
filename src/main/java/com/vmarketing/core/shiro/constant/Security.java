package com.vmarketing.core.shiro.constant;

/**
 * Security配置
 * 
 */
public interface Security {

	String ACCOUNT = "doufuplus";

	String PASSWORD = "123456";

	String SIGNING_KEY = "doufuplus";

	/**
	 * 角色
	 */
	class ROLES {

		public static final String ADMIN = "ADMIN";

		public static final String USER = "USER";
	}
}
