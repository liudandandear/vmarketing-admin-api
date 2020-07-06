package com.vmarketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vmarketing.core.api.Result;
import com.vmarketing.entity.SysUser;
import com.vmarketing.entity.SysUserCacheInfo;

/**
 * 用户服务类
 *
 * @author liudandandear
 * @since 2020-06-30
 */
public interface SysUserService extends IService<SysUser> {

	/**
	 * 重置密码
	 */
	public Result resetPassword(String username, String oldpassword, String newpassword, String confirmpassword);

	/**
	 * 修改密码
	 */
	public Result changePassword(SysUser sysUser);

	/**
	 * 删除用户
	 */
	public boolean deleteUser(String userId);

	/**
	 * 批量删除用户
	 */
	public boolean deleteBatchUsers(String userIds);

	/**
	 * 查询用户信息
	 */
	public SysUserCacheInfo getCacheUser(String username);

	/**
	 * 根据手机号获取用户名和密码
	 */
	public SysUser getUserByPhone(String phone);

	/**
	 * 根据邮箱获取用户
	 */
	public SysUser getUserByEmail(String email);

	/**
	 * 根据用户名获取用户信息
	 * 
	 * @param sysUser
	 * @param username
	 * @return
	 */
	public SysUser getUserByName(SysUser sysUser, String username);

	/**
	 * 校验用户是否有效
	 * 
	 * @param sysUser
	 * @return
	 */
	Result checkUserIsEffective(SysUser sysUser);
}
