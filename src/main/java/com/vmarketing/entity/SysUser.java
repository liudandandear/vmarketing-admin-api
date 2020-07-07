package com.vmarketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 模型层进行实体校验
 * </p>
 *
 * @author liudandandear
 * @since 2020-07-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 手机号(账户)
	 */
	private String account;

	/**
	 * 手机号
	 */
	@NotBlank(message = "手机号不能为空")
	private String phone;

	/**
	 * 电子邮件
	 */
	@Email(message = "邮箱格式不正确")
	private String email;

	/**
	 * 密码
	 */
	@NotBlank(message = "用户密码不能为空")
	private String password;

	/**
	 * 头像
	 */
	@URL(message = "头像地址不是一个正确的URL地址")
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
	private LocalDateTime created;

	/**
	 * 更新人
	 */
	private String updateBy;

	/**
	 * 更新时间
	 */
	private LocalDateTime updated;

	/**
	 * 最后登录时间
	 */
	private LocalDateTime lastLogin;

}
