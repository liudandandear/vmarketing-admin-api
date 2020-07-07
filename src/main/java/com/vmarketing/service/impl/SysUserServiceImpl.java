package com.vmarketing.service.impl;

import com.vmarketing.core.api.Result;
import com.vmarketing.core.constant.CacheConstant;
import com.vmarketing.core.constant.CommonConstant;
import com.vmarketing.core.util.PasswordUtil;
import com.vmarketing.core.util.oConvertUtils;
import com.vmarketing.entity.SysUser;
import com.vmarketing.entity.SysUserCacheInfo;
import com.vmarketing.mapper.SysUserMapper;
import com.vmarketing.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Arrays;

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
public abstract class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

	@Autowired
	private Result result;

	@Autowired
	private SysUserService sysUserService;

	/**
	 * 重置密码
	 */
	@Override
	@CacheEvict(value = { CacheConstant.SYS_USERS_CACHE }, allEntries = true)
	public Result resetPassword(String username, String oldpassword, String newpassword, String confirmpassword) {
		SysUser userinfo = this.getOne(new QueryWrapper<SysUser>().eq("username", username));
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
		String passwordEncode = PasswordUtil.encrypt(sysUser.getAccount(), password, salt);
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
	 * 获取用户信息
	 */
	@Override
	public SysUserCacheInfo getCacheUser(String username) {
		SysUserCacheInfo info = new SysUserCacheInfo();
		return info;

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
	 * 根据用户名获取用户信息
	 */
	@Override
	public SysUser getUserByName(SysUser sysUser, String username) {
		return sysUserService.getUserByEmail(username);
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

}
