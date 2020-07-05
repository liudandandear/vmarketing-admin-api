package com.vmarketing.core.exception;

/**
 * 自定义异常(CustomException)
 *
 */
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = -6736944294947154413L;

	public CustomException(String msg) {
		super(msg);
	}

	public CustomException() {
		super();
	}

}
