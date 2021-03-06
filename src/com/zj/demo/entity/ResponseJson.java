package com.zj.demo.entity;

public class ResponseJson {
	private boolean success;
	private String msg;
	private Object data;

	public ResponseJson() {
		this(false, null);
	}

	public ResponseJson(boolean success) {
		this(success, null);
	}

	public ResponseJson(boolean success, String msg) {
		this(success, msg, null);
	}

	public ResponseJson(boolean success, String msg, Object data) {
		this.success = success;
		this.msg = msg;
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "{\"success\":\"" + success + "\",\" msg\":\"" + msg + "\", \"data\":\"" + data + "\"}";
	}
}
