package com.zj.smartupload.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * 模拟实现smartUpload
 * 
 * @author ZM
 * @date 2020年8月18日
 */
public class MSmartUpload {
	private static MSmartUpload instance = null;

	private volatile static Boolean existInstance = false;

	private PageContext pageContext;
	private HttpServletRequest request;
	// private HttpServletResponse response;
	/***********/
	private final int DEFAULT_SIZE = 16;
	private int REAL_SIZE = 0;
	// 输入流
	private byte[] input;
	// 存放数据部分Data开始的位置
	private int[] arrayDataStart;
	// 存储头部和数据部分奇数为头 偶数是数据
	private MFile[] arrayFile;
	// 获取所有分隔符boundary起始位置
	private List<Integer> listBoundaryStart;

	/**
	 * return a instance of MSmartUpload
	 * 
	 * @return
	 */
	public static MSmartUpload getInstance() {
		if (!existInstance) {
			synchronized (MSmartUpload.class) {
				if (instance == null) {
					instance = new MSmartUpload();
					existInstance = true;
				}
			}
		}
		return instance;
	}

	/**
	 * initialize context
	 */
	public void initialize(PageContext pageContext) {
		/**
		 * 需要当前所在服务器的环境
		 */
		this.pageContext = pageContext;
		request = (HttpServletRequest) pageContext.getRequest();
		// response = (HttpServletResponse) pageContext.getResponse();
	}

	/**
	 * 上传
	 * 
	 * @param pageContext
	 */
	public void upload() {
		int totalRead = 0;
		int totalSize = request.getContentLength();
		int readBytes = 0;
		input = new byte[totalSize];
		for (; totalRead < totalSize; totalRead += readBytes)
			try {
				request.getInputStream();
				readBytes = request.getInputStream().read(input, totalRead, totalSize - totalRead);
			} catch (Exception e) {
				e.printStackTrace();
			}
		StringBuilder sb = new StringBuilder("--");
		String contentType = request.getHeader("Content-Type");
		// 用于分割数据流符的字符串
		String boundary = sb.append(contentType.substring(contentType.indexOf("boundary=") + 9)).toString();
		// 分割字符串boundary对应的字节数组
		byte[] boundaryBytes = boundary.getBytes();
		// 分割符长度
		int boundaryLength = boundaryBytes.length;
		// 输入流总长度
		long contentLength = (long) input.length;
		// 获取所有分隔符起始位置
		kmp(input, boundaryBytes, contentLength, boundaryLength);
		getDataPositon();
		generateFiles(boundaryLength);
	}

	/***
	 * 将某个文件另存为
	 * 
	 * @param file
	 * @param destPath
	 * @throws IOException
	 */
	public void saveAs(MFile file, String destPath) throws IOException {
		if (!file.isFile())
			throw new IOException(file.getFieldName() + " not a file");
		FileOutputStream fos = null;
		try {
			byte[] data = file.getData();
			File fl = new File(destPath);
			if (fl.exists())
				throw new IOException(file.getFileName() + " already exist");
			fos = new FileOutputStream(fl);
			fos.write(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fos.close();
		}

	}

	private void getDataPositon() {
		// 获取数据开头位置
		arrayDataStart = new int[DEFAULT_SIZE];
		int start = 0;
		int end = 0;
		for (int i = 0, len = listBoundaryStart.size() - 1; i < len; i++) {
			// 获取数据的起始位置和结束位置
			start = listBoundaryStart.get(i);
			end = listBoundaryStart.get(i + 1);
			for (int j = start; j < end; j++) {
				if (input[j] == 13 && input[j + 2] == 13) {
					if (REAL_SIZE++ >= DEFAULT_SIZE) {
						resizeArray();
					}
					arrayDataStart[i] = j + 4;
					break;
				}
			}
		}
	}

	private void generateFiles(int boundaryLength) {
		// [head,data,head,data,...,head,data]
		arrayFile = new MFile[REAL_SIZE];
		MFile file = null;
		String fieldName = null;
		String fileName = null;
		String contentType = null;
		String contentDisp = null;
		boolean isFile = false;
		for (int i = 0; i < REAL_SIZE; i++) {
			int boundaryStart = listBoundaryStart.get(i);
			int dataStart = arrayDataStart[i];
			String head = new String(input, boundaryStart + boundaryLength,
					dataStart - (boundaryStart + boundaryLength));
			// System.out.println(head);
			String[] fs = head.trim().split("[\n;]");
			int index = 0;
			int end = 0;
			fieldName = null;
			fileName = null;
			contentType = null;
			contentDisp = null;
			isFile = false;
			for (String s : fs) {
				s = s.trim();
				if (s.startsWith("name")) {
					index = s.indexOf("\"");
					end = s.lastIndexOf("\"");
					fieldName = s.substring(index + 1, end);
				} else if (s.startsWith("filename")) {
					index = s.indexOf("\"");
					end = s.lastIndexOf("\"");
					fileName = s.substring(index + 1, end);
				} else if (s.startsWith("Content-Type")) {
					index = s.indexOf(":");
					contentType = s.substring(index + 2);
				} else if (s.startsWith("Content-Disposition")) {
					index = s.indexOf(":");
					contentDisp = s.substring(index + 2);
				}
			}
			if (contentType != null) {
				// 是文件
				isFile = true;
			} else {
				isFile = false;
			}
			byte[] data = new byte[listBoundaryStart.get(i + 1) - dataStart - 2];
			System.arraycopy(input, dataStart, data, 0, listBoundaryStart.get(i + 1) - dataStart - 2);
//			System.out.println("---" + new String(data) + "**");
//			System.out.println(fieldName + "-" + fileName + "-" + contentType + "-" + contentDisp + "-" + isFile + "-"
//					+ data.length);
			file = new MFile(fieldName, fileName, contentType, contentDisp, data, isFile);
			arrayFile[i] = file;
		}
//		for (MFile s : arrayFile)
//			System.out.println("数据：" + s);

	}

	/**
	 * 扩容
	 */
	private void resizeArray() {
		int[] copy = new int[REAL_SIZE << 2];
		System.arraycopy(arrayDataStart, 0, copy, 0, REAL_SIZE);
		arrayDataStart = copy;
	}

	/**
	 * 字节匹配
	 * 
	 * @param pattern 需要匹配的字节数组
	 * @param target  目标数组
	 * @return 返回起始位置，没有返回-1
	 */
	private void kmp(byte[] target, byte[] pattern, long targetLength, int patternLength) {
		int[] next = getNext(pattern, patternLength);
		int j = 0;
		int i = 0;
		listBoundaryStart = new ArrayList<Integer>();
		while (i < targetLength) {
			if (j == patternLength) {
				listBoundaryStart.add(i - j);
				j = 0;
			}
			if (j == -1 || j == 0 || target[i] == pattern[j]) {
				i++;
				j++;
			} else {
				j = next[j];
			}
		}
	}

	private int[] getNext(byte[] pattern, int patternLength) {
		int[] next = new int[patternLength];
		next[0] = -1;
		int k = -1;
		int j = 0;
		while (j < patternLength - 1) {
			if (k == -1 || pattern[j] == pattern[k]) {
				if (pattern[++j] == pattern[++k]) { // 当两个字符相等时要跳过
					next[j] = next[k];
				} else {
					next[j] = k;
				}
			} else {
				k = next[k];
			}
		}
		return next;
	}

	/*********** 返回相关属性 ***********/

	public PageContext getPageContext() {
		return pageContext;
	}

	public MFile[] getArrayFile() {
		return arrayFile;
	}

}
