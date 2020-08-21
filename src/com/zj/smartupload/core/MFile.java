package com.zj.smartupload.core;

import java.util.Arrays;

public class MFile {
	private String fieldName;
	private String fileName;
	private String contentType;
	private String contentDisp;
	private byte[] data;
	private boolean isFile = false;

	public MFile(String fieldName, String fileName, String contentType, String contentDisp, byte[] data,
			boolean isFile) {
		super();
		this.fieldName = fieldName;
		this.fileName = fileName;
		this.contentType = contentType;
		this.contentDisp = contentDisp;
		this.data = data;
		this.isFile = isFile;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentDisp() {
		return contentDisp;
	}

	public void setContentDisp(String contentDisp) {
		this.contentDisp = contentDisp;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public boolean isFile() {
		return isFile;
	}

	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentDisp == null) ? 0 : contentDisp.hashCode());
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + (isFile ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MFile other = (MFile) obj;
		if (contentDisp == null) {
			if (other.contentDisp != null)
				return false;
		} else if (!contentDisp.equals(other.contentDisp))
			return false;
		if (contentType == null) {
			if (other.contentType != null)
				return false;
		} else if (!contentType.equals(other.contentType))
			return false;
		if (!Arrays.equals(data, other.data))
			return false;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;

		if (isFile != other.isFile)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MFile [fieldName=" + fieldName + ", fileName=" + fileName + ", contentType=" + contentType
				+ ", contentDisp=" + contentDisp + ", data=" + Arrays.toString(data) + ", isFile=" + isFile + "]";
	}

}
