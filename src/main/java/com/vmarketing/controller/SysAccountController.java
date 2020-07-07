package com.vmarketing.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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
import com.vmarketing.core.constant.ResultCode;
import com.vmarketing.core.db.RedisClient;
import com.vmarketing.core.util.JwtUtil;
import com.vmarketing.core.util.Md5Util;
import com.vmarketing.dto.account.LoginReq;
import com.vmarketing.entity.SysUser;
import com.vmarketing.service.impl.SysUserServiceImpl;

import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 鉴权控制器
 * 
 * @author liudandandear
 *
 */
@Slf4j
@RestController
public class SysAccountController {

	@Value("${config.refreshToken-expireTime}")
	private String refreshTokenExpireTime;

	@Autowired
	private SysUserServiceImpl sysUserService;

	@Autowired
	private RedisClient redis;

	/**
	 * 登录
	 * 
	 * @param loginDto
	 * @param response
	 * @return
	 */
	@CrossOrigin
	@PostMapping("/login")
	public Result login(@Validated @RequestBody LoginReq loginDto, HttpServletResponse response) {
		try {
			String account = loginDto.getAccount();
			String password = loginDto.getPassword();
			// 清除可能存在的shiro权限信息缓存
			if (redis.hasKey(RedisConstant.PREFIX_SHIRO_CACHE + account)) {
				redis.del(RedisConstant.PREFIX_SHIRO_CACHE + account);
			}
			SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().eq("account", loginDto.getAccount()));
			Assert.notNull(sysUser, "用户不存在");

			if (!sysUser.getPassword().equals(SecureUtil.md5(password + sysUser.getSalt()))) {
				return new Result(ResultCode.PASSWORD_ERROR, "密码不正确");
			}

			// 设置RefreshToken，时间戳为当前时间戳，直接设置即可(不用先删后设，会覆盖已有的RefreshToken)
			String currentTimeMillis = String.valueOf(System.currentTimeMillis());
			redis.set(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN + account, currentTimeMillis,
					Integer.parseInt(refreshTokenExpireTime));

			// 从Header中Authorization返回AccessToken，时间戳为当前时间戳
			String token = JwtUtil.sign(account, currentTimeMillis);
			response.setHeader("Authorization", token);
			response.setHeader("Access-Control-Expose-Headers", "Authorization");

			return new Result().OK();
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(ResultCode.ERROR, e.getMessage());
		}
	}

	/**
	 * 退出
	 * 
	 * @return
	 */
	@GetMapping("/logout")
	@RequiresAuthentication
	public Result logout(HttpServletRequest request) {
		try {
			String token = "";
			// 获取头部信息
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String key = (String) headerNames.nextElement();
				String value = request.getHeader(key);
				if ("Authorization".equalsIgnoreCase(key)) {
					token = value;
				}
			}
			// 校验token
			if (StringUtils.isBlank(token)) {
				return new Result(ResultCode.PARAM_ERROR);
			}
			String account = JwtUtil.getClaim(token, JwtConstant.ACCOUNT_KEY);
			if (StringUtils.isBlank(account)) {
				log.error("token失效或不正确.");
				return new Result(ResultCode.NOT_LOGIN, "token失效或不正确.");
			}
			// 清除shiro权限信息缓存
			if (redis.hasKey(RedisConstant.PREFIX_SHIRO_CACHE + account)) {
				redis.del(RedisConstant.PREFIX_SHIRO_CACHE + account);
			}
			// 清除RefreshToken
			redis.del(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN + account);

			return new Result().OK();
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(ResultCode.ERROR, e.getMessage());
		}
	}
}
