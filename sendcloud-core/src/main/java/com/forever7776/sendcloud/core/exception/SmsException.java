package com.forever7776.sendcloud.core.exception;

/**
 * @author kz
 * @date 2018-03-11
 */
public class SmsException extends Throwable implements SCException {
	private static final long serialVersionUID = 1L;
	private static final int errorCode = 401;

	public SmsException(String message) {
		super(message);
	}

	public String getMessage() {
		return String.format("code:%d,message:%s", errorCode, super.getMessage());
	}
}
