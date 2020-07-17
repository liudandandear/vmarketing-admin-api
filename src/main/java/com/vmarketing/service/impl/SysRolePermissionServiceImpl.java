package com.vmarketing.service.impl;

import com.vmarketing.entity.SysRolePermission;
import com.vmarketing.mapper.SysRolePermissionMapper;
import com.vmarketing.service.SysPermissionService;
import com.vmarketing.service.SysRolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色权限表 服务实现类
 * </p>
 *
 * @author liudandan
 * @since 2020-07-17
 */
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements SysRolePermissionService {

	@Override
	public List<SysPermissionService> selectByRoleId(Integer roleId) {
		// TODO Auto-generated method stub
		return null;
	}

}
