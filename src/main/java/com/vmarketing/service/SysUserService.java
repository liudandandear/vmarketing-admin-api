package com.vmarketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vmarketing.entity.SysUser;

/**
 * 用户服务类
 *
 * @author liudandandear
 * @since 2020-06-30
 */
public interface SysUserService extends IService<SysUser> {
	/**
	 * 根据用户名查询用户信息
	 * 
	 * @param username
	 * @return
	 */
	SysUser getUserByName(String username);

	/**
	 * 根据用户手机号查询用户信息
	 * 
	 * @param phone
	 * @return
	 */
	SysUser getUserByPhone(String phone);

	/**
	 * 根据用户邮箱查询用户信息
	 * 
	 * @param phone
	 * @return
	 */
	SysUser getUserByEmail(String email);
}
