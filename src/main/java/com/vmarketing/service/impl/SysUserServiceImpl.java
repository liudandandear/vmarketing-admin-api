package com.vmarketing.service.impl;

import com.vmarketing.core.api.Result;
import com.vmarketing.core.constant.CacheConstant;
import com.vmarketing.core.constant.CommonConstant;
import com.vmarketing.core.util.PasswordUtil;
import com.vmarketing.core.util.UUIDGenerator;
import com.vmarketing.core.util.oConvertUtils;
import com.vmarketing.entity.SysPermission;
import com.vmarketing.entity.SysRole;
import com.vmarketing.entity.SysUser;
import com.vmarketing.entity.SysUserCacheInfo;
import com.vmarketing.entity.SysUserRole;
import com.vmarketing.mapper.SysPermissionMapper;
import com.vmarketing.mapper.SysRoleMapper;
import com.vmarketing.mapper.SysUserMapper;
import com.vmarketing.mapper.SysUserRoleMapper;
import com.vmarketing.service.SysPermissionService;
import com.vmarketing.service.SysUserRoleService;
import com.vmarketing.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liudandandear
 * @since 2020-06-30
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

	@Autowired
	private Result result;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysPermissionService sysPermissionService;

	@Autowired
	private SysUserRoleService sysUserRoleService;

	/**
	 * 重置密码
	 */
	@Override
	@CacheEvict(value = { CacheConstant.SYS_USERS_CACHE }, allEntries = true)
	public Result resetPassword(String username, String oldpassword, String newpassword, String confirmpassword) {
		SysUser userinfo = SysUserService.getUserByName(username);
		String passwordEncode = PasswordUtil.encrypt(username, oldpassword, userinfo.getSalt());
		if (!userinfo.getPassword().equals(passwordEncode)) {
			result.setMsg("旧密码输入错误!");
			return result.NO();
		}
		if (StringUtils.isEmpty(newpassword)) {
			result.setMsg("新密码不允许为空!");
			return result.NO();
		}
		if (!newpassword.equals(confirmpassword)) {
			result.setMsg("两次输入密码不一致!");
			return result.NO();
		}
		String password = PasswordUtil.encrypt(username, newpassword, userinfo.getSalt());
		this.sysUserService.update(new SysUser().setPassword(password),
				new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, userinfo.getId()));
		result.setMsg("密码重置成功!");
		return result.OK();
	}

	/**
	 * 修改密码
	 */
	@Override
	@CacheEvict(value = { CacheConstant.SYS_USERS_CACHE }, allEntries = true)
	public Result changePassword(SysUser sysUser) {
		String salt = oConvertUtils.randomGen(8);
		sysUser.setSalt(salt);
		String password = sysUser.getPassword();
		String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), password, salt);
		sysUser.setPassword(passwordEncode);
		this.sysUserService.updateById(sysUser);
		result.setMsg("密码修改成功!");
		return result.OK();
	}

	/**
	 * 删除用户
	 */
	@Override
	@CacheEvict(value = { CacheConstant.SYS_USERS_CACHE }, allEntries = true)
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteUser(String userId) {
		this.removeById(userId);
		return false;
	}

	/**
	 * 批量删除用户
	 */
	@Override
	@CacheEvict(value = { CacheConstant.SYS_USERS_CACHE }, allEntries = true)
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteBatchUsers(String userIds) {
		this.removeByIds(Arrays.asList(userIds.split(",")));
		return false;
	}

	/**
	 * 添加用户和用户角色关系
	 */
	@Override
	@Transactional
	public void addUserWithRole(SysUser user, String roles) {
		this.save(user);
		if (oConvertUtils.isNotEmpty(roles)) {
			String[] arr = roles.split(",");
			for (String roleId : arr) {
				SysUserRole userRole = new SysUserRole(user.getId(), roleId);
				sysUserRoleService.saveOrUpdate(userRole);
			}
		}

	}

	/**
	 * 修改用户和用户角色关系
	 */
	@Override
	@CacheEvict(value = { CacheConstant.SYS_USERS_CACHE }, allEntries = true)
	@Transactional
	public void editUserWithRole(SysUser user, String roles) {
		this.updateById(user);
		// 先删后加
		sysUserRoleService.remove(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, user.getId()));
		if (oConvertUtils.isNotEmpty(roles)) {
			String[] arr = roles.split(",");
			for (String roleId : arr) {
				SysUserRole userRole = new SysUserRole(user.getId(), roleId);
				sysUserRoleService.saveOrUpdate(userRole);
			}
		}
	}

	/**
	 * 获取用户的授权角色 TODO::待完善
	 */
//	@Override
//	public List<String> getRole(String username) {
//		return sysUserRoleService.getRoleByUserName(username);
//	}

	/**
	 * 获取用户信息 TODO::待完善
	 */
	@Override
	public SysUserCacheInfo getCacheUser(String username) {
		SysUserCacheInfo info = new SysUserCacheInfo();
		return info;

	}

	/**
	 * 根据角色Id查询
	 */
	@Override
	public IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username) {
		return sysUserService.getUserByRoleId(page, roleId, username);
	}

	@Override
	public Set<String> getUserRolesSet(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 通过用户名获取用户权限集合 TODO::待完善
	 */
	@Override
	public Set<String> getUserPermissionsSet(String username) {
		Set<String> permissionSet = new HashSet<>();
		List<SysPermission> permissionList = sysPermissionService.queryByUser(username);
		for (SysPermission po : permissionList) {
//			// TODO URL规则有问题？
//			if (oConvertUtils.isNotEmpty(po.getUrl())) {
//				permissionSet.add(po.getUrl());
//			}
			if (oConvertUtils.isNotEmpty(po.getPerms())) {
				permissionSet.add(po.getPerms());
			}
		}
		return permissionSet;
	}

	/**
	 * 根据手机号获取用户名和密码
	 */
	@Override
	public SysUser getUserByPhone(String phone) {
		return sysUserService.getUserByPhone(phone);
	}

	/**
	 * 根据邮箱获取用户
	 */
	@Override
	public SysUser getUserByEmail(String email) {
		return sysUserService.getUserByEmail(email);
	}

	/**
	 * 校验用户是否有效
	 */
	@Override
	public Result checkUserIsEffective(SysUser sysUser) {
		// 情况1：根据用户信息查询，该用户不存在
		if (sysUser == null) {
			result.setMsg("该用户不存在，请注册");
			return result;
		}
		// 情况3：根据用户信息查询，该用户已冻结
		if (CommonConstant.USER_FREEZE.equals(sysUser.getStatus())) {
			result.setMsg("该用户已冻结");
			return result;
		}
		return result;
	}

	/**
	 * 查询被逻辑删除的用户
	 */
	@Override
	public List<SysUser> queryLogicDeleted() {
		return this.queryLogicDeleted(null);
	}

	/**
	 * 查询被逻辑删除的用户（可拼装查询条件）
	 */
	@Override
	public List<SysUser> queryLogicDeleted(LambdaQueryWrapper<SysUser> wrapper) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 还原被逻辑删除的用户
	 */
	@Override
	public boolean revertLogicDeleted(List<String> userIds, SysUser updateEntity) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 彻底删除被逻辑删除的用户
	 */
	@Override
	public boolean removeLogicDeleted(List<String> userIds) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 更新手机号、邮箱空字符串为 null
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateNullPhoneEmail() {
		sysUserService.updateNullByEmptyString("email");
		sysUserService.updateNullByEmptyString("phone");
		return true;
	}

	/**
	 * 保存第三方用户信息
	 */
	@Override
	public void saveThirdUser(SysUser sysUser) {
		// 保存用户
		String userid;
		baseMapper.insert(sysUser);
		// 获取第三方角色
		SysRole sysRole = sysRoleMapper
				.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, "third_role"));
		// 保存用户角色
		SysUserRole userRole = new SysUserRole();
		userRole.setRoleId(sysRole.getId());
		userRole.setUserId(userid);
		sysUserRoleMapper.insert(userRole);

	}

}
