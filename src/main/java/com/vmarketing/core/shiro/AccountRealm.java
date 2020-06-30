package com.vmarketing.core.shiro;

import cn.hutool.core.bean.BeanUtil;

import com.vmarketing.core.util.JwtUtils;
import com.vmarketing.entity.SysUser;
import com.vmarketing.service.SysUserService;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//shiro 进行登录或者权限校验的核心逻辑
@Component
public class AccountRealm extends AuthorizingRealm {

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	SysUserService sysUserService;

	// 使Relam支持jwt的凭证校验
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JwtToken;
	}

	// 权限校验
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

	// 登录认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		// 第一步：通过jwt获取到用户信息
		JwtToken jwtToken = (JwtToken) token;

		String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();

		SysUser sysUser = sysUserService.getById(Long.valueOf(userId));

		// 第二步：判断用户状态（如果异常就抛出对应的异常信息）
		if (sysUser == null) {
			throw new UnknownAccountException("账户不存在");
		}

		if (sysUser.getStatus() == -1) {
			throw new LockedAccountException("账户已被锁定");
		}

		AccountProfile profile = new AccountProfile();
		BeanUtil.copyProperties(sysUser, profile);

		// 正常就封装成SimpleAuthenticationInfo返回给shiro
		return new SimpleAuthenticationInfo(profile, jwtToken.getCredentials(), getName());
	}
}
