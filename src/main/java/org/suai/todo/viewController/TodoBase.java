package org.suai.todo.viewController;
import org.suai.todo.model.*;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.*;

import java.util.HashMap;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class TodoBase extends HttpServlet {
	public void doGet(
			HttpServletRequest req, 
			HttpServletResponse res)
	throws ServletException, IOException {
	
		error(res, 404);
	}
	
	public void doPost(
			HttpServletRequest req, 
			HttpServletResponse res)
	throws ServletException, IOException {
	
		error(res, 405);
	}

	public void redirect(HttpServletResponse res, String uri) throws IOException {
		res.sendRedirect(uri);
	}
	
	public void error(HttpServletResponse res, Integer status) throws IOException {
		if (status < 400) {
			status = 400;
		}
		res.setStatus(status);
		String msg = "Error " + status;

	    if (status == 400) {
	    	msg = "400 Bad Request";
	    }
	    if (status == 403) {
	    	msg = "403 Forbidden";
	    }
	    if (status == 404) {
	    	msg = "404 Not Found";
	    }
	    if (status == 405) {
	    	msg = "405 Method Not Allowed";
	    }
	    if (status == 503) {
	    	msg = "503 Service Unavailable";
	    }
	    HashMap<String, String>vars = new HashMap<String, String>();
	    vars.put("msg", msg);

		render(res, "error", vars);
	}
	
	public void render(
			HttpServletResponse res, 
			String viewName,
			HashMap<String, String> vars) 
	throws IOException {
	
		res.setContentType("text/html; charset=utf-8");
		res.setHeader("Cache-Control", "no-cache");

		String body = fillView(viewName, vars);
		PrintWriter out = res.getWriter();
		out.print(body);
	}
	
	public String fillView(
			String viewName,
			HashMap<String, String> vars) 
	throws IOException {
		
		String view = readView(viewName);
		String[] a = view.split("\\{\\{", 0);
		
		for (int i = 1; i < a.length; i++) {
			a[i] = fillVar(a[i], vars);
		}
		return String.join("", a);
	}
	
	private String fillVar (
			String s, 
			HashMap<String, String> vars) 
	throws IOException {
	
		String[] a = s.split("}}", 2);
		if (a.length < 2) {
			String e = "{{" + s;
			if (e.length() > 60) {
				e = e.substring(0, 60) + "...";
			}
			throw new IOException("Render: Broken template: '" + e + "'");
		}
		String key = a[0].strip();
		String tail = a[1];
		a = key.split("\\s+", 2);
		
		if (a.length == 1) {
			return getSafeVar(key, vars) + tail;
		}
		// command mode
		String cmd = a[0].strip().toLowerCase();
		String opt = a[1].strip();
		
		if (cmd.equals("include")) {
			return fillView(opt, vars) + tail;
		}
		if (cmd.equals("unsafe")) {
			return getUnsafeVar(opt, vars) + tail;
		}
		throw new IOException("Render: Unknown command: '" + cmd + " " + opt + "'");
	}

	private String readView(String viewName) throws IOException {
		String viewPath = "/home/evru/Documents/todoData/views/" + viewName + ".html";
		Path p = Paths.get(viewPath);
		System.out.println("Trying to read view from path: " + p.toString());

		try (InputStream is = Files.newInputStream(p)) {
			StringBuilder out = new StringBuilder();
			InputStreamReader in = new InputStreamReader(is, StandardCharsets.UTF_8);
			char[] buffer = new char[8 * 1024];

			for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
				out.append(buffer, 0, numRead);
			}

			return out.toString();
		} catch (IOException e) {
			System.out.println("Error reading view: " + e.getMessage());
			throw e;
		}
	}




	private static String getUnsafeVar(String key, HashMap<String, String> vars) {
		if (vars == null || !vars.containsKey(key)) {
			return "";
		}
		return vars.get(key);
	}
	
	private static String getSafeVar(String key, HashMap<String, String> vars) {
		String s = getUnsafeVar(key, vars);
		s = s.replaceAll("&", "&amp;");
		s = s.replaceAll("<", "&lt;");
		s = s.replaceAll(">", "&gt;");
		s = s.replaceAll("\"", "&quot;");
		s = s.replaceAll("'", "&apos;");
		
		return s;
	}

	protected Integer getParameterInt(HttpServletRequest req, String paramName, Integer defaultValue) {
		try {
			return Integer.parseInt(req.getParameter(paramName));
		} catch (NumberFormatException | NullPointerException e) {
			return defaultValue;
		}
	}

    
    public String getParameterString(HttpServletRequest req, String key) {
 		String v = req.getParameter(key);
		return v == null ? "" : v;
	}
}
