package com.vmarketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author liudandandear
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	@NotBlank(message = "用户名称不能为空")
	private String name;

	@NotBlank(message = "用户密码不能为空")
	private String password;

	/**
	 * 是否启用(0：否 1：是)
	 */
	private Integer enable;

	/**
	 * 用户状态(0：正常 1：锁定)
	 */
	private Integer status;

}
