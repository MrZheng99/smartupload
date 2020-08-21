package com.zj.demo.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BasicServlet extends HttpServlet {

	private static final long serialVersionUID = -668865107645471823L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		super.service(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);

	}

	public void send(HttpServletResponse resp, String msg) throws IOException {
		PrintWriter out = resp.getWriter();
		out.print(msg);
		out.flush();
	}

	public void send(HttpServletResponse resp, Integer code) throws IOException {
		PrintWriter out = resp.getWriter();
		out.print(code);
		out.flush();
	}

	public void send(HttpServletResponse resp, Object object) throws IOException {
		Gson gson = new GsonBuilder().serializeNulls().create();
		PrintWriter out = resp.getWriter();
		out.print(gson.toJson(object));
		out.flush();
	}

}
