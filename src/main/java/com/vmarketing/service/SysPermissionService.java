package com.vmarketing.service;

import com.vmarketing.entity.SysPermission;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author liudandandear
 * @since 2020-07-03
 */
public interface SysPermissionService extends IService<SysPermission> {

	List<SysPermission> queryByUser(String username);

}
