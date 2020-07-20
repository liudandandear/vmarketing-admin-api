package com.vmarketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vmarketing.entity.SysUser;
import com.vmarketing.mapper.SysUserMapper;
import com.vmarketing.service.SysUserService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	private SysUserMapper sysUserMapper;

	// 根据用户名查询用户信息
	@Override
	public SysUser getUserByName(String username) {
		QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>();
		queryWrapper.eq("username", username);
		SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
		return sysUser;
	}

	@Override
	public SysUser getUserByPhone(String phone) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SysUser getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}
}
