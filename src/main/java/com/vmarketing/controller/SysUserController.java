package com.vmarketing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.vmarketing.core.common.lang.Result;
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

	@GetMapping("/{id}")
	public Object test(@PathVariable("id") Long id) {
		SysUser sysUser = sysUserService.getById(id);
		return Result.succ(sysUser);
	}
	
	/**
	 * 测试实体校验
	 * @param user
	 * @return
	 */
	@PostMapping("/save")
	public Object testUser(@Validated @RequestBody SysUser sysUser) {
	    return sysUser.toString();
	}
}
