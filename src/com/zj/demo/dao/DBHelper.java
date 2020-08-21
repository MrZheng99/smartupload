package com.zj.demo.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBHelper {

	/**
	 * ��������
	 */
	/*
	 * static { try {
	 * Class.forName(RederConfig.getInstance().getProperty("sqlClassName")); } catch
	 * (ClassNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * }
	 */

	/**
	 * 从连接池中获取一个空闲连接
	 * 
	 * @param url
	 * @param userName
	 * @param password
	 * @return
	 * @throws NamingException
	 */
	private Connection getConnection() {
		Connection con = null;
		try {
			// con = DriverManager.getConnection(url, userName, password);
			Context context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup("java:comp/env/demo");
			con = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return con;
	}

	/**
	 * 
	 * @param pstmt
	 * @param rs
	 * @param con
	 */
	private void colse(PreparedStatement pstmt, ResultSet rs, Connection con) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * ��Ԥ����������ò���
	 * 
	 * @param pstmt
	 * @param pramas
	 * @see #setPramas(PreparedStatement, String...)
	 */
	private void setPramas(PreparedStatement pstmt, Object... parmas) {
		if (pstmt == null || parmas.length <= 0) {
			return;
		}
		for (int i = 0, len = parmas.length; i < len; i++) {
			try {
				pstmt.setObject(i + 1, parmas[i]);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * ִ�и���
	 * 
	 * @param sql
	 * @param pramas
	 * @return ��Ӱ�������
	 */
	public int update(String sql, Object... parmas) {
		int rs = -1;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			this.setPramas(pstmt, parmas);
			rs = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.colse(pstmt, null, con);
		}
		return rs;
	}

	/**
	 * ��ȡ�����������
	 * 
	 * @param rs
	 * @return
	 */
	private String[] getResultColNames(ResultSet rs) {
		// ��ȡ���в�ѯ��������
		ResultSetMetaData rsmd;
		String[] colNames = null;
		try {
			rsmd = rs.getMetaData();
			int colNum = rsmd.getColumnCount();
			colNames = new String[colNum];
			for (int i = 1; i <= colNum; i++) {
				colNames[i - 1] = rsmd.getColumnName(i).toLowerCase();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return colNames;

	}

	/**
	 *
	 * 
	 * @param sql
	 * @param parmas
	 * @return List<Map<String, Object>>
	 * @see #query(String, String...)
	 */
	public List<Map<String, Object>> querys(String sql, Object... parmas) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			this.setPramas(pstmt, parmas);
			rs = pstmt.executeQuery();
			String[] colNames = getResultColNames(rs);
			Map<String, Object> map = null;
			while (rs.next()) {
				map = new HashMap<String, Object>();
				for (String colName : colNames) {
					map.put(colName, rs.getObject(colName));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.colse(pstmt, rs, con);
		}
		return list;
	}

	/**
	 * 
	 * 
	 * @param sql
	 * @param parmas
	 * @return List<Map<String, String>>
	 * 
	 */
	public List<Map<String, String>> querysString(String sql, Object... parmas) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			this.setPramas(pstmt, parmas);
			rs = pstmt.executeQuery();
			String[] colNames = getResultColNames(rs);
			Map<String, String> map = null;
			while (rs.next()) {
				map = new HashMap<String, String>();
				for (String colName : colNames) {
					map.put(colName, rs.getString(colName));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.colse(pstmt, rs, con);
		}
		return list;
	}

	public List<Object> querysString(Class<?> clazz, String sql, Object... parmas) {
		List<Object> list = new ArrayList<Object>();
		clazz.getTypeName();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			this.setPramas(pstmt, parmas);
			rs = pstmt.executeQuery();
			String[] colNames = getResultColNames(rs);
			Map<String, String> map = null;
			Field field = null;
			String value = null;
			Object object = null;
			while (rs.next()) {
				object = clazz.newInstance();
				for (String colName : colNames) {
					field = ((Class<?>) object).getDeclaredField(colName);
					value = rs.getString(colName);
					// 属性为private属性设置为true
					field.setAccessible(true);
					if (field.getType().equals(Integer.class)) {
						field.set(object, Integer.parseInt(value));
						continue;
					}
					field.set(object, value);
				}
				list.add(object);
			}
		} catch (SQLException | InstantiationException | IllegalAccessException | NoSuchFieldException
				| SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.colse(pstmt, rs, con);
		}
		return list;
	}

	/**
	 * 
	 * 
	 * @param sql
	 * @param parmas
	 * @return
	 * @see #queryString(String, Object...)
	 */
	public Map<String, Object> query(String sql, Object... parmas) {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			this.setPramas(pstmt, parmas);
			rs = pstmt.executeQuery();
			String[] colNames = getResultColNames(rs);

			while (rs.next()) {
				for (String colName : colNames) {
					map.put(colName, rs.getObject(colName));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.colse(pstmt, rs, con);
		}
		return map;
	}

	/**
	 * ��ѯ���м�¼
	 * 
	 * @param sql
	 * @param parmas
	 * @return
	 */
	public Map<String, String> queryString(String sql, Object... parmas) {
		Map<String, String> map = new HashMap<String, String>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			this.setPramas(pstmt, parmas);
			rs = pstmt.executeQuery();
			String[] colNames = getResultColNames(rs);

			while (rs.next()) {
				for (String colName : colNames) {
					map.put(colName, rs.getString(colName));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.colse(pstmt, rs, con);
		}
		return map;
	}

	/**
	 * ���ؼ�¼������
	 * 
	 * @param sql
	 * @param parmas
	 * @return
	 */
	public int getQueryTotalNums(String sql, Object... parmas) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int total = 0;
		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			this.setPramas(pstmt, parmas);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.colse(pstmt, rs, con);
		}
		return total;
	}

	/**
	 * 基于实体类查询 泛型：参数化类型 ：由原来的具体类型参数化 变成一个变量 E T ?
	 * 
	 * @param <T>
	 * @param sql
	 * @param params
	 * @return
	 */
	public <T> List<T> searchs(Class<T> clazz, String sql, Object... parmas) {
		List<T> list = new ArrayList<T>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		T t = null;
		try {
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			this.setPramas(pstmt, parmas);
			rs = pstmt.executeQuery();
			String[] colNames = getResultColNames(rs);
			// 从中提取出set方法,方法名为键，键全部为小写
			Map<String, Method> setters = getSetterMap(clazz);
			Method method = null;
			Class<?>[] types = null;
			Class<?> type = null;
			String methodName = null;
			while (rs.next()) {
				// 一行数据对应一个实体类
				t = clazz.newInstance();
				for (String colName : colNames) {
					methodName = "set" + colName;
					method = setters.get(methodName);
					if (method != null) {
						types = method.getParameterTypes();
						if (types != null) {
							type = types[0];
						}
						if (Integer.TYPE == type || Integer.class == type) {
							method.invoke(t, rs.getInt(colName));
						} else if (Float.TYPE == type || Float.class == type) {
							method.invoke(t, rs.getFloat(colName));
						} else if (Double.TYPE == type || Double.class == type) {
							method.invoke(t, rs.getDouble(colName));
						} else {
							method.invoke(t, rs.getString(colName));
						}
					}
				}
				list.add(t);
			}
		} catch (SQLException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		} finally {
			this.colse(pstmt, rs, con);
		}
		return list;
	}

	/**
	 * 根据类名获取一个set方法map
	 * 
	 * @param clazz
	 * @return
	 */
	public Map<String, Method> getSetterMap(Class<?> clazz) {
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

	public <T> T search(Class<T> clazz, String sql, Object... parmas) {
		T t = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			t = clazz.newInstance();
			con = this.getConnection();
			pstmt = con.prepareStatement(sql);
			this.setPramas(pstmt, parmas);
			rs = pstmt.executeQuery();
			String[] colNames = getResultColNames(rs);
			Map<String, Method> setters = getSetterMap(clazz);
			Method method = null;
			String methodName = null;
			Class<?>[] types = null;
			Class<?> type = null;
			if (rs.next()) {
				// 一行数据对应一个实体类
				t = clazz.newInstance();
				for (String colName : colNames) {
					methodName = "set" + colName;
					method = setters.get(methodName);
					if (method != null) {
						types = method.getParameterTypes();
						if (types != null) {
							type = types[0];
						}
						if (Integer.TYPE == type || Integer.class == type) {
							method.invoke(t, rs.getInt(colName));
						} else if (Float.TYPE == type || Float.class == type) {
							method.invoke(t, rs.getFloat(colName));
						} else if (Double.TYPE == type || Double.class == type) {
							method.invoke(t, rs.getDouble(colName));
						} else {
							method.invoke(t, rs.getString(colName));
						}
					}
				}
			}
			return t;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return t;
	}
}
