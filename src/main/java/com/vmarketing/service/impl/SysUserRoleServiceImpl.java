package com.vmarketing.service.impl;

import com.vmarketing.entity.SysRole;
import com.vmarketing.entity.SysUserRole;
import com.vmarketing.mapper.SysUserRoleMapper;
import com.vmarketing.service.SysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author liudandan
 * @since 2020-07-17
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

	@Override
	public List<SysRole> getByUserId(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
