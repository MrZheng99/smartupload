package com.zj.demo.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;

import com.zj.demo.dao.StudentInfoDao;
import com.zj.demo.entity.ResponseJson;
import com.zj.demo.entity.Student;
import com.zj.demo.util.MUtil;

@WebServlet("/stu")
public class StudentInfoServlet extends BasicServlet {

	private static final long serialVersionUID = 6249436327418985860L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String op = req.getParameter("op");
		// System.out.println(op);
		if ("addStudent".equals(op)) {
			// 添加学生
			addStudent(req, resp);
		} else if ("finds".equals(op)) {
			// 添加学生
			finds(req, resp);
		} else if ("findByPage".equals(op)) {
			// 添加学生
			findByPage(req, resp);
		}

	}

	private void findByPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		finds(req, resp);
	}

	private void finds(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// 第一个数据是总记录数
		int page = Integer.valueOf(req.getParameter("page"));
		int rows = Integer.valueOf(req.getParameter("rows"));
		StudentInfoDao dao = new StudentInfoDao();
		int total = dao.getTotal();
		List<Student> list = dao.findByPa(page, rows);
		// System.out.println(list);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", list);
		this.send(resp, map);
	}

	private void addStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		MUtil fuu = new MUtil();
		PageContext pageContext = JspFactory.getDefaultFactory().getPageContext(this, req, resp, null, true, 2048,
				true);
		Student student = fuu.upload(Student.class, pageContext);
		ResponseJson responseJson = new ResponseJson();

		if (student.getPhoto() == null || student.getPhoto() == "") {
			responseJson.setSuccess(false);
			this.send(resp, responseJson.toString());
			return;
		}
		StudentInfoDao dao = new StudentInfoDao();

		// System.out.println(student.toString());
		Integer row = dao.insert(student);
		if (row != null && row > 0) {
			responseJson.setSuccess(true);
			this.send(resp, responseJson.toString());
			return;
		}
		responseJson.setSuccess(false);
		this.send(resp, responseJson.toString());

	}
}
