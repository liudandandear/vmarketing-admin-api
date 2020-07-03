package com.vmarketing.mapper;

import com.vmarketing.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Mapper 接口
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
	SysUser findByAccount(@Param("account") String account);
}
