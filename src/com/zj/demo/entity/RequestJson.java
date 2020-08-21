package com.zj.demo.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class RequestJson {
	/**
	 * 根据类名获取一个set方法map
	 * 
	 * @param clazz
	 * @return
	 */
	public static Map<String, Method> getSetterMap(Class<?> clazz) {
		Method[] methods = clazz.getMethods();// 所有公共方法
		// 从中提取出set方法,方法名为键，键全部为小写
		Map<String, Method> setters = new HashMap<String, Method>();
		String methodName = null;
		for (Method method : methods) {
			methodName = method.getName().toLowerCase();
			if (methodName.startsWith("set")) {
				setters.put(methodName, method);
			}
		}
		return setters;
	}

	public static String getMethodName(String name) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("set").append(name.substring(0, 1).toUpperCase()).append(name.substring(1));
		return buffer.toString();
	}

	public static <T> T getParms(Class<T> clazz, HttpServletRequest req) {
		T t = null;
		try {
			t = clazz.newInstance();
			// 获取参数名
			Enumeration<String> names = req.getParameterNames();
			// 获取指定类中所有方法
			Map<String, Method> setters = getSetterMap(clazz);
			String methodName = null;
			String name = null;
			String value = null;
			Method method = null;
			Class<?>[] types = null;
			Class<?> type = null;

			while (names.hasMoreElements()) {
				name = names.nextElement();
				methodName = getMethodName(name);
				method = setters.get(methodName);
				if (method == null) {
					continue;
				}
				types = method.getParameterTypes();
				if (types == null || types.length == 0) {
					continue;
				}
				value = req.getParameter(name);
				System.out.println(name + ":" + value);
				if (types != null) {
					type = types[0];
				}
				if (Integer.TYPE == type || Integer.class == type) {
					method.invoke(t, Integer.parseInt(value));
				} else if (Float.TYPE == type || Float.class == type) {
					method.invoke(t, Float.parseFloat(value));
				} else if (Double.TYPE == type || Double.class == type) {
					method.invoke(t, Double.parseDouble(value));
				} else {
					method.invoke(t, Integer.parseInt(value));
				}
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return t;
	}
}
