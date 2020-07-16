package com.vmarketing.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.vmarketing.core.api.Result;
import com.vmarketing.core.api.ResultCode;
import com.vmarketing.entity.SysUser;
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

	// 测试权限验证
	@RequiresAuthentication
	@GetMapping("info")
	public Result info()
	{
		SysUser sysUser = sysUserService.getById(1L);
		JSONObject json = new JSONObject();
		// 用户信息
		json.put("userinfo", sysUser);
		// 用户角色
		json.put("roles", "");
		return new Result(ResultCode.SUCCESS, "success", json);
	}

}
