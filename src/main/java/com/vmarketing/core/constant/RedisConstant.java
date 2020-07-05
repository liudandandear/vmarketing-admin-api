package com.vmarketing.core.constant;

/**
 * redis 常量配置
 * 
 */
public interface RedisConstant {

	String BASE_KEY = "doufuplus_";

	/**
	 * redis过期时间，以秒为单位，一分钟
	 */
	int EXRP_MINUTE = 60;

	/**
	 * redis过期时间，以秒为单位，一小时
	 */
	int EXRP_HOUR = 60 * 60;

	/**
	 * redis过期时间，以秒为单位，一天
	 */
	int EXRP_DAY = 60 * 60 * 24;

	/**
	 * redis-key-前缀-shiro:cache:
	 */
	String PREFIX_SHIRO_CACHE = "shiro:cache:";

	/**
	 * redis-key-前缀-shiro:access_token:
	 */
	String PREFIX_SHIRO_ACCESS_TOKEN = "shiro:access_token:";

	/**
	 * redis-key-前缀-shiro:refresh_token:
	 */
	String PREFIX_SHIRO_REFRESH_TOKEN = "shiro:refresh_token:";

}
