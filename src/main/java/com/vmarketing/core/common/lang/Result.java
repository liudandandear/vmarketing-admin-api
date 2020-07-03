package com.vmarketing.core.common.lang;

import com.alibaba.fastjson.JSONObject;
import com.vmarketing.core.shiro.constant.ResultCode;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Json封装
 */
public class Result implements Serializable {

	private static final long serialVersionUID = 8178937610421199532L;

	/**
	 * 请求标识，默认为失败状态
	 */
	private boolean success = false;

	/**
	 * 状态码，默认为失败状态
	 */
	private Integer code = ResultCode.ERROR;

	/***
	 * 操作信息
	 */
	private String msg;

	/**
	 * 返回数据
	 */
	private Object obj = new JSONObject();

	/**
	 * 成功响应
	 */
	public Result OK() {
		this.success = true;
		this.code = ResultCode.SUCCESS;
		if (StringUtils.isBlank(this.msg)) {
			this.msg = "success.";
		}
		return this;
	}

	/**
	 * 请求成功，但业务逻辑处理不通过
	 */
	public Result NO() {
		this.success = true;
		this.code = ResultCode.ERROR;
		return this;
	}

	public Result() {
		super();
	}

	public Result(int code) {
		super();
		this.success = true;
		this.code = code;
	}

	public Result(int code, String msg) {
		super();
		this.success = true;
		this.code = code;
		this.msg = msg;
	}

	public Result(int code, String msg, Object obj) {
		super();
		this.success = true;
		this.code = code;
		this.msg = msg;
		this.obj = obj;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.success = true;
		this.code = code;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.success = true;
		if (obj == null) {
			obj = new JSONObject();
		}
		this.obj = obj;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ResultVo{" + "success=" + success + ", code=" + code + ", msg='" + msg + '\'' + ", obj=" + obj + '}';
	}
}