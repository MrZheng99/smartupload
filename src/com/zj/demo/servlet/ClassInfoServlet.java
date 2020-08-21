package com.zj.demo.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zj.demo.dao.ClassInfoDao;

//@WebServlet(urlPatterns = {"/task","/room"})
@WebServlet(value = "/cls")
public class ClassInfoServlet extends BasicServlet {

	private static final long serialVersionUID = 5170844770216381940L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 规定操作
		String op = req.getParameter("op");
		System.out.println(op);
		if ("findAll".equals(op)) {
			// 查询所有信息
			findAll(req, resp);
		}
	}

	private void findAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		ClassInfoDao dao = new ClassInfoDao();
		List<Map<String, String>> list = dao.finds();
		StringBuffer sbf = new StringBuffer();
		/*
		 * 浪费流量 list.forEach(item ->
		 * sbf.append("<option value='").append(item.get("cid")).append("'").append(">")
		 * .append(item.get("cname")).append("</option>"));
		 */
		// json [{"cid":"101","cname":"三年级1班"},{"cid":"101","cname":"三年级1班"}]
		sbf.append("[");
		list.forEach(
				item -> sbf.append("{\"cid\":\"" + item.get("cid") + "\",\"cname\":\"" + item.get("cname") + "\"},"));
		String data = sbf.substring(0, sbf.length() - 1);
		this.send(resp, data + "]");

	}

}
