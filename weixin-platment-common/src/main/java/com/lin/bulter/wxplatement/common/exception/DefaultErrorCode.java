package com.lin.bulter.wxplatement.common.exception;

/**
 * 描述信息
 *
 * @author wangchenglin13
 * @date 2020/3/14 13:58
 */
public enum DefaultErrorCode implements ErrorCode {
	SUCCESS("0000", "OK"),
	UNKNOWN_ERROR("9999", "未知异常");

	private final String code;
	private final String description;

	private DefaultErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return this.code;
	}

	public String getDescription() {
		return this.description;
	}

	public String toString() {
		return String.format("Code:[%s], Description:[%s]. ", this.code, this.description);
	}
}
