package com.vmarketing.mapper;

import com.vmarketing.entity.SysRolePermission;
import com.vmarketing.service.SysPermissionService;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 角色权限表 Mapper 接口
 * </p>
 *
 * @author liudandan
 * @since 2020-07-17
 */
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {
	List<SysPermissionService> selectByRoleId(Integer roleId);
}
