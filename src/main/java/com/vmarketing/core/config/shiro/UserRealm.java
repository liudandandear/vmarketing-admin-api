package com.vmarketing.core.config.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vmarketing.core.config.jwt.JwtToken;
import com.vmarketing.core.constant.JwtConstant;
import com.vmarketing.core.constant.RedisConstant;
import com.vmarketing.core.db.RedisClient;
import com.vmarketing.core.util.JwtUtil;
import com.vmarketing.entity.SysUser;
import com.vmarketing.mapper.SysUserMapper;
import com.vmarketing.service.SysUserService;
import com.vmarketing.service.impl.SysUserServiceImpl;

/**
 * 自定义Realm Realm 能做的工作有三个方面
 * 
 * ①身份验证：getAuthenticationInfo 方法，验证账户和密码，并返回相关信息
 * 
 * ②权限获取：getAuthorizationInfo 方法，获取指定身份的权限，并返回相关信息
 * 
 * ③令牌支持：supports 方法 ，判断该令牌（token）是否被支持
 * 
 * 令牌类型：HostAuthenticationToken【主机验证令牌】，UsernamePasswordToken【用户密码验证令牌】
 */
@Service
public class UserRealm extends AuthorizingRealm {

	@Autowired
	private RedisClient redis;

	@Autowired
	private SysUserService sysUserService;

	/**
	 * 大坑，必须重写此方法，不然Shiro会报错
	 */
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JwtToken;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

	/**
	 * 身份验证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
		String token = (String) auth.getCredentials();
		if (StringUtils.isBlank(token)) {
			throw new AuthenticationException("token 不能为空（token cannot be empty.）");
		}

		// 解密获得username，用于和数据库进行对比
		String username = JwtUtil.getClaim(token, JwtConstant.ACCOUNT_KEY);

		// 帐号为空
		if (StringUtils.isBlank(username)) {
			throw new AuthenticationException("token中用户名为空(The username in Token is empty.)");
		}

		// 查询用户是否存在
		SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().eq("username", username));
		if (sysUser == null) {
			throw new AuthenticationException("该用户不存在(The username does not exist.)");
		}

		// 开始认证，要AccessToken认证通过，且Redis中存在RefreshToken，且两个Token时间戳一致
		if (JwtUtil.verify(token) && redis.hasKey(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN + username)) {
			// 获取RefreshToken的时间戳
			String currentTimeMillisRedis = redis.get(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN + username).toString();
			// 获取AccessToken时间戳，与RefreshToken的时间戳对比
			if (JwtUtil.getClaim(token, JwtConstant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
				return new SimpleAuthenticationInfo(token, token, "userRealm");
			}
		}
		// 错误抛出异常即可
		throw new AuthenticationException("令牌过期或不正确(token expired or incorrect.)");
	}
}
