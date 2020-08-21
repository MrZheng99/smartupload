package com.zj.demo.dao;

import java.util.List;
import java.util.Map;

import com.zj.demo.entity.Student;

public class StudentInfoDao {
//INSERT INTO `tb_student` (`sname`, `birth`, `cid`, `age`, `tel`) VALUES ('天天', '2020-07-07', '102', '16', '15096096666')
	public Integer insert(Student student) {
		DBHelper db = new DBHelper();
		String sql = "INSERT INTO `tb_student` (`sname`, `birth`, `cid`, `age`, `tel`,`address`,`photo`) VALUES (?, ?, ?, ?,?,?,?)";
		return db.update(sql, student.getSname(), student.getBirth(), student.getCid(), student.getAge(),
				student.getTel(), student.getAddress(), student.getPhoto());
	}

	public List<Map<String, String>> findByPage(int page, int rows) {

		DBHelper db = new DBHelper();
		String sql = "select sid,s.cid,cname,sname,age,address,tel,birth,photo "
				+ "from tb_student s,tb_class c where s.cid = c.cid limit ?,?";
		return db.querysString(sql, (page - 1) * rows, rows);
	}

	public List<Student> findByPa(int page, int rows) {
		DBHelper db = new DBHelper();
		String sql = "select sid,s.cid,cname,sname,age,address,tel,birth,photo "
				+ "from tb_student s,tb_class c where s.cid = c.cid limit ?,?";
		return db.searchs(Student.class, sql, (page - 1) * rows, rows);
	}

	public Integer getTotal() {
		DBHelper db = new DBHelper();
		String sql = "select count(sid) from tb_student";
		return db.getQueryTotalNums(sql);
	}
}
