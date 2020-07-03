package com.vmarketing.service;

import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vmarketing.core.common.lang.Result;
import com.vmarketing.entity.SysUser;
import com.vmarketing.entity.SysUserCacheInfo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liudandandear
 * @since 2020-06-30
 */
public interface SysUserService extends IService<SysUser> {
	
	/**
	 * 重置密码
	 *
	 * @param username
	 * @param oldpassword
	 * @param newpassword
	 * @param confirmpassword
	 * @return
	 */
	public Result resetPassword(String username, String oldpassword, String newpassword, String confirmpassword);

	/**
	 * 修改密码
	 *
	 * @param sysUser
	 * @return
	 */
	public Result changePassword(SysUser sysUser);

	/**
	 * 删除用户
	 * 
	 * @param userId
	 * @return
	 */
	public boolean deleteUser(String userId);

	/**
	 * 批量删除用户
	 * 
	 * @param userIds
	 * @return
	 */
	public boolean deleteBatchUsers(String userIds);

	/**
	 * 根据用户名获取用户信息
	 * 
	 * @param username
	 */
	public static SysUser getUserByName(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 添加用户和用户角色关系
	 * 
	 * @param user
	 * @param roles
	 */
	public void addUserWithRole(SysUser user, String roles);

	/**
	 * 修改用户和用户角色关系
	 * 
	 * @param user
	 * @param roles
	 */
	public void editUserWithRole(SysUser user, String roles);

	/**
	 * 获取用户的授权角色
	 * 
	 * @param username
	 * @return
	 */
	public List<String> getRole(String username);

	/**
	 * 查询用户信息包括 部门信息
	 * 
	 * @param username
	 * @return
	 */
	public SysUserCacheInfo getCacheUser(String username);

	/**
	 * 根据角色Id查询
	 * 
	 * @param
	 * @return
	 */
	public IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username);

	/**
	 * 通过用户名获取用户角色集合
	 *
	 * @param username 用户名
	 * @return 角色集合
	 */
	Set<String> getUserRolesSet(String username);

	/**
	 * 通过用户名获取用户权限集合
	 *
	 * @param username 用户名
	 * @return 权限集合
	 */
	Set<String> getUserPermissionsSet(String username);

	/**
	 * 根据手机号获取用户名和密码
	 */
	public SysUser getUserByPhone(String phone);

	/**
	 * 根据邮箱获取用户
	 */
	public SysUser getUserByEmail(String email);

	/**
	 * 校验用户是否有效
	 * 
	 * @param sysUser
	 * @return
	 */
	Result checkUserIsEffective(SysUser sysUser);

	/**
	 * 查询被逻辑删除的用户
	 */
	List<SysUser> queryLogicDeleted();

	/**
	 * 查询被逻辑删除的用户（可拼装查询条件）
	 */
	List<SysUser> queryLogicDeleted(LambdaQueryWrapper<SysUser> wrapper);

	/**
	 * 还原被逻辑删除的用户
	 */
	boolean revertLogicDeleted(List<String> userIds, SysUser updateEntity);

	/**
	 * 彻底删除被逻辑删除的用户
	 */
	boolean removeLogicDeleted(List<String> userIds);

	/**
	 * 更新手机号、邮箱空字符串为 null
	 */
	@Transactional(rollbackFor = Exception.class)
	boolean updateNullPhoneEmail();

	/**
	 * 保存第三方用户信息
	 * 
	 * @param sysUser
	 */
	void saveThirdUser(SysUser sysUser);
}
