package com.zj.demo.dao;

import java.util.List;
import java.util.Map;

public class ClassInfoDao {
	public List<Map<String, String>> finds() {
		DBHelper db = new DBHelper();
		String sql = "select cid,cname from tb_class";
		return db.querysString(sql);
	}
}
