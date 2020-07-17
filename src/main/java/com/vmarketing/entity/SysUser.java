package com.vmarketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表 用户实体类
 * </p>
 *
 * @author liudandan
 * @since 2020-07-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 电子邮件
	 */
	private String email;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 性别(0-默认未知,1-男,2-女)
	 */
	private Boolean sex;

	/**
	 * md5密码盐
	 */
	private String salt;

	/**
	 * 第三方登录的唯一标识
	 */
	private String thirdId;

	/**
	 * 第三方类型
	 */
	private String thirdType;

	/**
	 * 是否启用(0：否 1：是)
	 */
	private Integer enable;

	/**
	 * 用户状态(0：正常 1：锁定)
	 */
	private Integer status;

	/**
	 * 注册时间
	 */
	private Integer created;

	/**
	 * 更新人
	 */
	private String updateBy;

	/**
	 * 更新时间
	 */
	private Integer updated;

	/**
	 * 最后登录时间
	 */
	private Integer lastLogin;

	/**
	 * 用户对应的角色集合
	 */
	private Set<SysRole> roles;

}
