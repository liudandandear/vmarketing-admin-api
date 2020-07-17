package com.vmarketing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vmarketing.entity.SysUser;
import com.vmarketing.mapper.SysUserMapper;
import com.vmarketing.service.SysUserService;

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

	@Override
	public SysUser getUserByName(String username) {
		// TODO Auto-generated method stub
		return null;
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
