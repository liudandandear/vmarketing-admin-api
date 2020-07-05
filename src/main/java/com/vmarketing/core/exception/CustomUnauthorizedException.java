package com.vmarketing.core.exception;

/**
 * 自定义401无权限异常(UnauthorizedException)
 */
public class CustomUnauthorizedException extends RuntimeException {

	private static final long serialVersionUID = -3993376696547776573L;

	public CustomUnauthorizedException(String msg) {
		super(msg);
	}

	public CustomUnauthorizedException() {
		super();
	}
}
