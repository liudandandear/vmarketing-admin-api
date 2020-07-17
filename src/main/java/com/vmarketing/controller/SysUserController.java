package com.vmarketing.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vmarketing.mapper.SysUserMapper;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.vmarketing.core.api.Result;
import com.vmarketing.core.api.ResultCode;
import com.vmarketing.entity.SysRole;
import com.vmarketing.entity.SysUser;
import com.vmarketing.service.SysRoleService;
import com.vmarketing.service.SysUserService;

import java.util.List;
import java.util.Map;

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

	// 测试权限验证
	@RequiresAuthentication
	@GetMapping("info")
	public Result info()
	{
		QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>();
		SysUser sysUser = sysUserService.getById(1L);
		return new Result(ResultCode.SUCCESS, "success", sysUser);
	}

}
