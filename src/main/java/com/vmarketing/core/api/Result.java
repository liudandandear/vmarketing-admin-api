package com.vmarketing.core.api;

import org.json.JSONObject;

/**
 * Result返回消息实体
 */
public class Result {
	private boolean success = false;
	private Integer code = null;
	private String msg = null;
	private Object data = new JSONObject();

	/**
	 * 成功响应
	 */
	public Result OK()
	{
		this.success = true;
		this.code = ResultCode.SUCCESS;
		if (this.msg == null)
		{
			this.msg = "success.";
		}
		return this;
	}

	/**
	 * 请求成功，但业务逻辑处理不通过
	 */
	public Result NO()
	{
		this.success = false;
		this.code = ResultCode.ERROR;
		return this;
	}

	public Result()
	{
		super();
	}

	public Result(int code)
	{
		super();
		this.success = false;
		this.code = code;
	}

	public Result(int code, String msg)
	{
		super();
		this.success = false;
		this.code = code;
		this.msg = msg;
	}

	public Result(int code, String msg, Object data)
	{
		super();
		this.success = true;
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	public Integer getCode()
	{
		return code;
	}

	public void setCode(Integer code)
	{
		this.code = code;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public Object getData()
	{
		return data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}

	@Override
	public String toString()
	{
		return "Result{" + "success=" + success + ", code=" + code + ", msg='" + msg + '\'' + ", res=" + data + '}';
	}
}