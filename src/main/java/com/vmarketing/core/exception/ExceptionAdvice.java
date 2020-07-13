package com.vmarketing.core.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.vmarketing.core.api.Result;
import com.vmarketing.core.api.ResultCode;

/**
 * 异常控制处理器
 */
@RestControllerAdvice
public class ExceptionAdvice {
	/**
	 * 捕捉所有Shiro异常
	 */
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(ShiroException.class)
	public Result handle401(ShiroException e) {
		return new Result(ResultCode.UNLAWFUL, "无权访问(Unauthorized):" + e.getMessage());
	}

	/**
	 * 单独捕捉Shiro(UnauthorizedException)异常 该异常为访问有权限管控的请求而该用户没有所需权限所抛出的异常
	 */
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnauthorizedException.class)
	public Result handle401(UnauthorizedException e) {
		return new Result(ResultCode.UNLAWFUL, "无权访问(Unauthorized):当前Subject没有此请求所需权限(" + e.getMessage() + ")");
	}

	/**
	 * 单独捕捉Shiro(UnauthenticatedException)异常
	 * 该异常为以游客身份访问有权限管控的请求无法对匿名主体进行授权，而授权失败所抛出的异常
	 */
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnauthenticatedException.class)
	public Result handle401(UnauthenticatedException e) {
		return new Result(ResultCode.UNLAWFUL,
				"无权访问(Unauthorized):当前Subject是匿名Subject，请先登录(This subject is anonymous.)");
	}

	/**
	 * 捕捉校验异常(BindException)
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	public Result validException(BindException e) {
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		Map<String, Object> results = this.getValidError(fieldErrors);

		return new Result(ResultCode.ERROR, results.get("errorMsg").toString(), results.get("errorList"));
	}

	/**
	 * 捕捉校验异常(MethodArgumentNotValidException)
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Result validException(MethodArgumentNotValidException e) {
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		Map<String, Object> results = this.getValidError(fieldErrors);
		return new Result(ResultCode.ERROR, results.get("errorMsg").toString(), results.get("errorList"));
	}

	/**
	 * 捕捉404异常
	 */
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoHandlerFoundException.class)
	public Result handle(NoHandlerFoundException e) {
		return new Result(ResultCode.NOT_FOUND, e.getMessage());
	}

	/**
	 * 捕捉其他所有异常
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public Result globalException(HttpServletRequest request, Throwable ex) {
		// return new JsonVo(this.getStatus(request).value(), ex.toString() + ": " +
		// ex.getMessage(), null);
		return new Result(ResultCode.SERVER_ERROR, ex.toString() + ": " + ex.getMessage());
	}

	/**
	 * 捕捉其他所有自定义异常
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(CustomException.class)
	public Result handle(CustomException e) {
		return new Result(ResultCode.SERVER_ERROR, e.getMessage());
	}

	/**
	 * 获取校验错误信息
	 */
	private Map<String, Object> getValidError(List<FieldError> fieldErrors) {
		Map<String, Object> result = new HashMap<String, Object>(16);
		List<String> errorList = new ArrayList<String>();
		StringBuffer errorMsg = new StringBuffer("校验异常(ValidException):");
		for (FieldError error : fieldErrors) {
			errorList.add(error.getField() + "-" + error.getDefaultMessage());
			errorMsg.append(error.getField() + "-" + error.getDefaultMessage() + ".");
		}
		result.put("errorList", errorList);
		result.put("errorMsg", errorMsg);
		return result;
	}
}
