package com.zj.demo.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;

import javax.servlet.jsp.PageContext;

import com.zj.smartupload.core.MFile;
import com.zj.smartupload.core.MSmartUpload;

public class MUtil {
	public static String PATH;// 上传后的文件保存路径,外部设置

	@SuppressWarnings("deprecation")
	public <T> T upload(Class<T> clazz, PageContext pageContext) {
		T t = null;
		// 实例化上传组件
		MSmartUpload su = MSmartUpload.getInstance();
		try {
			// 实例化上传组件
			su.initialize(pageContext);
			su.upload();// 开始上传
			MFile[] files = su.getArrayFile();
			String name = null;
			Field field = null;
			String value = null;
			t = clazz.newInstance();
			if (files == null || files.length <= 0) {
				return t;
			}
			String realPath = pageContext.getRequest().getRealPath("/");
			String destFilePathName = null;
			String fileName = null;
			StringBuffer sbf = null;
			for (MFile file : files) {
				name = file.getFieldName();
				field = clazz.getDeclaredField(name);
				if (!file.isFile()) {
					value = new String(file.getData());
					// System.out.println(name + ":" + value);
					// 属性为private属性设置为true
					field.setAccessible(true);
					if (field.getType().equals(Integer.class)) {
						field.set(t, Integer.parseInt(value));
						continue;
					}
					// 为该set方法赋值（参数为对象，属性值）
					field.set(t, value);
				} else {
					// 获取tomcat在服务器中的绝对路径
					destFilePathName = null;
					fileName = null;
					sbf = new StringBuffer();
					// 避免重名加上时间戳
					fileName = PATH + "/" + new Date().getTime() + "_" + file.getFileName();
					destFilePathName = realPath + fileName;
					// 存到服务器中->获取tomcat在服务器中的绝对路径
//					System.out.println(destFilePathName);
					su.saveAs(file, destFilePathName);
					sbf.append(fileName + ",");
					field = clazz.getDeclaredField("photo");
					field.setAccessible(true);
					field.set(t, sbf.substring(0, sbf.length() - 1));
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}
}
