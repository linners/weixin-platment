package com.lin.bulter.wxplatement.common.exception;

/**
 * 描述信息
 *
 * @author wangchenglin13
 * @date 2020/3/14 13:57
 */
public class BusinessException extends RuntimeException {
	private ErrorCode errorCode;

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.toString());
		this.errorCode = errorCode;
	}

	public BusinessException(ErrorCode errorCode, String errorMessage) {
		super(errorCode.toString() + " - " + errorMessage);
		this.errorCode = errorCode;
	}

	private BusinessException(ErrorCode errorCode, String errorMessage, Throwable cause) {
		super(errorCode.toString() + " - " + getMessage(errorMessage) + " - " + getMessage(cause), cause);
		this.errorCode = errorCode;
	}

	public static BusinessException asBusinessException(ErrorCode errorCode) {
		return new BusinessException(errorCode);
	}

	public static BusinessException asBusinessException(ErrorCode errorCode, String message) {
		return new BusinessException(errorCode, message);
	}

	public static BusinessException asBusinessException(ErrorCode errorCode, String message, Throwable cause) {
		return cause instanceof BusinessException ? (BusinessException) cause : new BusinessException(errorCode, message, cause);
	}

	public static BusinessException asBusinessException(ErrorCode errorCode, Throwable cause) {
		return cause instanceof BusinessException ? (BusinessException) cause : new BusinessException(errorCode, (String) null, cause);
	}

	public ErrorCode getErrorCode() {
		return this.errorCode;
	}

	private static String getMessage(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj instanceof Throwable ? ((Throwable) obj).getMessage() : obj.toString();
		}
	}
}
