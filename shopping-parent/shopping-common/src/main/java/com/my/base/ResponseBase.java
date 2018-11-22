package com.my.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseBase {
	private Integer returnCode;
	private String msg;
	private Object data;
	
	
	public ResponseBase() {
		super();
	}
	public ResponseBase(Integer returnCode, String msg, Object data) {
		super();
		this.returnCode = returnCode;
		this.msg = msg;
		this.data = data;
	}
	@Override
	public String toString() {
		return "ResponseBase [returnCode=" + returnCode + ", msg=" + msg + ", data=" + data + "]";
	}
	
	
}
