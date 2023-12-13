package org.suai.todo.utils;
import org.suai.todo.model.*;
import org.suai.todo.viewController.*;

import javax.servlet.http.*;
import javax.servlet.*;

import java.time.LocalDateTime;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ErrorLog implements Filter {
	private ServletContext servletContext = null;
	private boolean debug = false;
	private Path logPath = null;
	
	@Override
	public void init(FilterConfig config) {
		servletContext = config.getServletContext();
		
		String dFlag = servletContext.getInitParameter("debug");
		debug = dFlag != null && dFlag.equals("true");
		
		String p = config.getInitParameter("filename");
		
		if (p != null) {
			logPath = Paths.get(p);
		}
	}

    @Override
    public void doFilter(
    		ServletRequest  request, 
    		ServletResponse response, 
    		FilterChain     chain)
    throws ServletException, IOException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String loginURI = req.getContextPath() + "/login";
       	        
        try {
        	chain.doFilter(request, response);
        } catch (Exception e) {
			Throwable cause = e.getCause();

			if (cause == null) {
				cause = e;
			}
			try {
				res.setStatus(500);
				res.setContentType("text/html; charset=utf-8");
				res.setHeader("Cache-Control", "no-cache");
				
				PrintWriter out = res.getWriter();
				out.print("<title>TODO: Error</title>\r\n");
				out.print("<h1>500 Internal Server Error</h1>\r\n");

				if (debug) {
					out.print("<pre>\r\n");
					cause.printStackTrace(out);
				}
			} catch (Exception err) {
				System.out.println(err);
			}
			if (logPath == null) {
				return;
			}
			StringWriter sw = new StringWriter();
			cause.printStackTrace(new PrintWriter(sw));
			String s = LocalDateTime.now().toString() + " " + sw.toString() + "\r\n";

			Files.writeString(logPath, s, StandardOpenOption.CREATE, 
				StandardOpenOption.APPEND);
        }
    }
}
