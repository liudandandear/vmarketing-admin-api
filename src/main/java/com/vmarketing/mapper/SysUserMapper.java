package com.vmarketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vmarketing.entity.SysUser;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author liudandan
 * @since 2020-07-07
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
	SysUser findByAccount(@Param("account") String account);
}
