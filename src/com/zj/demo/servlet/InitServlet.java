package com.zj.demo.servlet;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.zj.demo.util.MUtil;
import com.zj.demo.util.StringUtil;

//@WebServlet(name="initServlet" value="" loadOnStartup = 1, initParams = { @WebInitParam(name = "loadupPath", value = "../files") })
public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = 8344355278879597868L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		String path = config.getInitParameter("uploadPath");
		StringUtil util = new StringUtil();
		if (util.checkNul(path)) {
			path = "../photo";
		}
		System.out.println(path);
		String temp = config.getServletContext().getRealPath("/");
		File file = new File(temp, path);
		if (!file.exists()) {
			file.mkdir();
		}
		MUtil.PATH = path;
	}
}
