package com.vmarketing.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vmarketing.core.api.Result;
import com.vmarketing.core.constant.JwtConstant;
import com.vmarketing.core.constant.RedisConstant;
import com.vmarketing.core.db.RedisClient;
import com.vmarketing.core.shiro.constant.StatusCode;
import com.vmarketing.core.util.JwtUtil;
import com.vmarketing.core.util.Md5Util;
import com.vmarketing.dto.account.LoginReq;
import com.vmarketing.entity.SysUser;
import com.vmarketing.service.SysUserService;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;

/**
 * 账号相关
 * 
 * @author liudandandear
 *
 */
@RestController
public class AccountController {

	@Value("${config.refreshToken-expireTime}")
	private String refreshTokenExpireTime;

	@Autowired
	SysUser sysUser;

	@Autowired
	SysUserService sysUserService;

	/**
	 * 登录
	 * 
	 * @param loginDto
	 * @param response
	 * @return
	 */
	@CrossOrigin
	@PostMapping("/login")
	public Result login(@Validated @RequestBody LoginReq loginDto, HttpServletResponse response)
	{
		Result result = new Result();
		RedisClient redis = new RedisClient();
		sysUserService.getOne(new QueryWrapper<SysUser>().eq("username", loginDto.getPhone()));
		Assert.notNull(sysUser, "请输入正确的用户名或密码！");// 如果用户不存在
		// Md5加密
		String inputPassword = Md5Util.encode(loginDto.getPassword() + sysUser.getSalt());// 用户密码=输入的密码+密码盐
		// 将数据库里边的密码和输入的密码（inputPassword）进行比较
		if (!sysUser.getPassword().equals(inputPassword))
		{
			result.setCode(StatusCode.NOT_FOUND);
			result.setMsg("用户名与密码不匹配！");
			return result;

		}
//		// 清除可能存在的shiro权限信息缓存
//		if (redis.hasKey(RedisConstant.PREFIX_SHIRO_CACHE + loginDto.account)) {
//			redis.del(RedisConstant.PREFIX_SHIRO_CACHE + loginDto.account);
//		}

		// 设置RefreshToken，时间戳为当前时间戳，直接设置即可(不用先删后设，会覆盖已有的RefreshToken)
		String currentTimeMillis = String.valueOf(System.currentTimeMillis());
		redis.set(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN + JwtConstant.ACCOUNT, currentTimeMillis,
				Integer.parseInt(refreshTokenExpireTime));
		// 从Header中Authorization返回AccessToken，时间戳为当前时间戳
		String token = JwtUtil.sign(JwtConstant.ACCOUNT, currentTimeMillis);
		response.setHeader("Authorization", token);
		response.setHeader("Access-Control-Expose-Headers", "Authorization");
		result.OK();
		return result;
	}

	/**
	 * 获取当前登录用户
	 */
	public SysUser getCurrent()
	{
		try
		{
			Subject subject = SecurityUtils.getSubject();
			if (subject != null)
			{
				String token = (String) subject.getPrincipal();
				if (StringUtils.isNotBlank(token))
				{
					String account = JwtUtil.getClaim(token, JwtConstant.ACCOUNT);
					if (StringUtils.isNotBlank(account))
					{
						return sysUserService.getOne(queryWrapper);
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 退出
	 * 
	 * @return
	 */
	@GetMapping("/logout")
	@RequiresAuthentication
	public Result logout()
	{
		Result result = new Result();
		SecurityUtils.getSubject().logout();
		result.OK();
		return result;
	}
}
