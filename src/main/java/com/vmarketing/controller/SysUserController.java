package com.vmarketing.controller;

import java.time.LocalDateTime;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vmarketing.core.common.lang.Result;
import com.vmarketing.entity.SysUser;
import com.vmarketing.service.SysUserService;

import cn.hutool.core.bean.BeanUtil;

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
	Result result;

	@Autowired
	SysUserService sysUserService;

	// 测试权限验证
	@RequiresAuthentication
	@GetMapping("test")
	public Result test() {
		SysUser sysUser = sysUserService.getById(1L);
		result.setObj(sysUser);
		return result.OK();
	}

	@GetMapping("/{id}")
	public Object test(@PathVariable("id") Long id) {
		SysUser sysUser = sysUserService.getById(id);
		result.setObj(sysUser);
		return result.OK();
	}

	/**
	 * 测试实体校验
	 */
	@PostMapping("/save")
	public Object testUser(@Validated @RequestBody SysUser sysUser) {
		return sysUser.toString();
	}

	/**
	 * 用户详情
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public Result detail(@PathVariable(name = "id") Long id) {
		SysUser sysUser = sysUserService.getById(id);
		Assert.notNull(sysUser, "用户不存在！");
		result.setObj(sysUser);
		return result.OK();
	}

	/**
	 * 用户列表
	 * 
	 * @param curInteger
	 * @return
	 */
	@RequiresAuthentication
	@GetMapping("list")
	public Result list(@RequestParam(defaultValue = "1") Integer curInteger) {
		Page<SysUser> page = new Page<SysUser>(curInteger, 5);
		IPage<SysUser> iPage = sysUserService.page(page, new QueryWrapper<SysUser>().orderByDesc("created"));
		result.setObj(iPage);
		return result.OK();
	}

	@RequiresAuthentication // 接口需要认证才能访问
	@PutMapping("/edit/{id}")
	public Result edit(@PathVariable(name = "id") Long id, @Validated @RequestBody SysUser sysUser) {
		// 先查询用户是否存在
		SysUser tmp = null;
		tmp = sysUserService.getById(id);
		Assert.notNull(tmp, "用户不存在！");
		tmp = new SysUser();

		/**
		 * 对象属性拷贝， 将源对象的属性拷贝到目标对象
		 * 
		 * 源Bean对象->目标Bean对象,后边的是不拷贝的的属性列表
		 */
		BeanUtil.copyProperties(sysUser, tmp, "id", "enable", "status", "created", "last_login");
		sysUserService.saveOrUpdate(tmp);
		return result.OK();
	}

	/**
	 * 新增用户
	 * 
	 * @param sysUser
	 * @return
	 */
	@PostMapping
	public Result add(@Validated @RequestBody SysUser sysUser) {
		SysUser tmp = null;
		tmp = new SysUser();
		tmp.setName(sysUser.getName());
		tmp.setEnable(0);
		tmp.setStatus(0);
		tmp.setCreated(LocalDateTime.now());
		sysUserService.saveOrUpdate(tmp);
		return Result.succ(null);
	}
}
