package com.vmarketing.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vmarketing.core.api.Result;
import com.vmarketing.core.api.ResultCode;
import com.vmarketing.core.constant.CacheConstant;
import com.vmarketing.core.db.RedisClient;
import com.vmarketing.entity.SysUser;
import com.vmarketing.service.SysRoleService;
import com.vmarketing.service.SysUserService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author liudandandear
 * @since 2020-06-30
 */
@RestController
@RequestMapping("/sys-user")
public class SysUserController {

	@Autowired
	SysUserService sysUserService;

	@Autowired
	SysRoleService sysRoleService;

	@Autowired
	RedisClient redis;

	// 测试权限验证
	@RequiresAuthentication
	@GetMapping("info")
	public Result info() {
		Map<String, Object> result = new HashMap<>();
		String username = "admin";
		SysUser sysUser = sysUserService.getUserByName(username);
		result.put("id", sysUser.getId());
		result.put("username", sysUser.getUsername());
		result.put("phone", sysUser.getPhone());
		result.put("email", sysUser.getEmail());
		result.put("avatar", sysUser.getEmail());
		result.put("enable", sysUser.getEnable());
		result.put("status", sysUser.getStatus());
		result.put("roles", sysUser.getStatus());
		// 缓存用户信息到redis（username=>data）
		redis.set(CacheConstant.SYS_USER_INFO + sysUser.getUsername(), sysUser);
		return new Result(ResultCode.SUCCESS, "success", result);
	}

}
