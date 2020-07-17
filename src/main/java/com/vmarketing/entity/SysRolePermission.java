package com.vmarketing.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色权限表 角色与权限中间类
 * </p>
 *
 * @author liudandan
 * @since 2020-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRolePermission implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	/**
	 * 角色id
	 */
	private String roleId;

	/**
	 * 权限id
	 */
	private String permissionId;

}
