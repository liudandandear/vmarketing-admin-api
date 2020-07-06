package com.vmarketing.service.impl;

import com.vmarketing.entity.SysPermission;
import com.vmarketing.mapper.SysPermissionMapper;
import com.vmarketing.service.SysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author liudandandear
 * @since 2020-07-03
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

	@Override
	public List<SysPermission> queryByUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}

}
