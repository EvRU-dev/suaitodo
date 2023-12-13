package org.suai.todo.viewController;
import org.suai.todo.model.Record;
import org.suai.todo.model.Model;
import org.suai.todo.model.User;

import javax.servlet.http.*;
import javax.servlet.*;

import java.util.ArrayList;

import java.io.IOException;

public class TodoDebug extends TodoBase {
	
	@Override
	public void doGet(
			HttpServletRequest req, 
			HttpServletResponse res) 
	throws ServletException, IOException {
	
		try {
			ServletContext sc = getServletContext();
			String dFlag = sc.getInitParameter("debug");
			boolean debug = dFlag != null && dFlag.equals("true");
			
			if (!debug) {
				error(res, 404);
				return;
			}
			String uri = req.getRequestURI();
			
			String bodyStr = "<html style='font-family: monospace, monospace;'>\r\n" +
					"<p>Table <a href='debug?a=users'>users</a></p>\r\n" +
					"<p>Table <a href='debug?a=todos'>todos</a></p>\r\n" +
					"<p>Gen server <a href='debug?a=error'>error</a></p>\r\n";
			
			String action = req.getParameter("a");
			
			if (action != null) {
				if (action.equals("users")) {
					bodyStr = listUsers();
				}
				if (action.equals("todos")) {
					bodyStr = listRecords();
				}
				if (action.equals("error")) {
					bodyStr = genError();
				}
			}
			res.setStatus(200);
			res.setContentType("text/html; charset=utf-8");
			res.setHeader("Cache-Control", "no-cache");

			res.getWriter().print(bodyStr);
		} catch (Exception err) {
			throw new ServletException(err);
		}
	}
	
	private String listUsers() throws Exception {
		String b = "<table cellspacing=14em border=0 style='font-family: monospace, monospace;'>\r\n";
		Integer i = 0;
		
		b += "<thead><tr><th>id</th><th>name</th><th>salt</th>" + 
			"<th>hash</th><th>itemsPerPage</th></tr></thead>\r\n";
		
		while(true) {
			i += 1;
			User user = Model.getUser(i);
			if (user == null) {
				break;
			}
			b += "<tr><td>" + user.id + "</td><td>" + getSafe(user.name) + "</td><td>" + 
				user.salt + "</td><td>" + user.hash + "</td><td>" + 
				user.itemsPerPage + "</td></tr>\r\n";
		}
		b += "</table>";
		
		return b;
	}

	private String listRecords() throws Exception {
		String b = "<table cellspacing=14em border=0 style='font-family: monospace, monospace;'>\r\n";
		b += "<thead><tr><th>id</th><th>parent</th><th>owner</th><th>priority</th>" + 
			"<th>done</th><th>created</th><th>deadline</th><th>caption</th></tr></thead>\r\n";
		
		ArrayList<Record> recs = Model.getTodos();
		
		for (int i = 0; i < recs.size(); i++) {
			Record r = recs.get(i);
			b += "<tr><td>" + r.id + "</td><td>" + r.parent + "</td><td>" + 
				r.owner + "</td><td>" + r.priority + "</td><td>" + 
				r.done + "</td><td>" + r.created + "</td><td>" + 
				r.deadline + "</td><td>" + getSafe(r.caption) + 
				"</td></tr>\r\n";
		}
		b += "</table>";
		
		return b;
	}
	
	private static String getSafe(String s) {
		s = s.replaceAll("&", "&amp;");
		s = s.replaceAll("<", "&lt;");
		s = s.replaceAll(">", "&gt;");
		s = s.replaceAll("\"", "&quot;");
		s = s.replaceAll("'", "&apos;");
		
		return s;
	}
	
	private String genError() throws Exception {
		throw new Exception("Test Exception");
	}
}
