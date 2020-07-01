package com.vmarketing.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vmarketing.core.common.dto.LoginDto;
import com.vmarketing.core.common.lang.Result;
import com.vmarketing.core.util.JwtUtils;
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
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	SysUserService sysUserService;

	/**
	 * 登录
	 * @param loginDto
	 * @param response
	 * @return
	 */
	@CrossOrigin
	@PostMapping("/login")
	public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
		SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().eq("name", loginDto.getName()));
		Assert.notNull(sysUser, "用户不存在");
		if (!sysUser.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))) {
			return Result.fail("密码错误！");
		}
		String jwt = jwtUtils.generateToken(sysUser.getId());
		response.setHeader("Authorization", jwt);
		response.setHeader("Access-Control-Expose-Headers", "Authorization");
		Object object=MapUtil.builder()
                .put("id", sysUser.getId())
                .put("name", sysUser.getName())
                .put("anable",sysUser.getEnable())
                .put("status",sysUser.getStatus())
                .map();
		return Result.succ(object);
	}
	
	/**
	 * 退出
	 * @return
	 */
	@GetMapping("/logout")
	@RequiresAuthentication
	public Result logout() {
		SecurityUtils.getSubject().logout();
		return Result.succ(null);
	}
}
