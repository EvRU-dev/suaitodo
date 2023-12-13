package org.suai.todo.utils;
import org.suai.todo.model.User;
import org.suai.todo.viewController.*;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.IOException;
import java.io.PrintWriter;

import java.time.LocalDateTime;
import java.time.Duration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import java.nio.charset.StandardCharsets;

public class AccessLog implements Filter {
	private Path logPath = null;
	private ServletContext servletContext = null;

	class WrappedResponse extends HttpServletResponseWrapper {
		private PrintWriterCount pwc = null;
		
		public WrappedResponse(HttpServletResponse res) throws IOException {
			super(res);
			pwc = new PrintWriterCount(res.getWriter());
		}
		
		@Override
		public PrintWriter getWriter() {
			return pwc;
		}
		
		public Long getLen() {
			return pwc.len;
		}
	}
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		String p = config.getInitParameter("filename");
		
		if (p != null) {
			logPath = Paths.get(p);
		}
		servletContext = config.getServletContext();
	}

    @Override
    public void doFilter(
    		ServletRequest  request, 
    		ServletResponse response, 
    		FilterChain     chain)
    throws ServletException, IOException {
    		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		LocalDateTime start = LocalDateTime.now();
		
		try {
 			req.setCharacterEncoding("UTF-8");
 			res.setCharacterEncoding("UTF-8");
 		} catch (Exception err) { 
 			throw new ServletException(err);
 		}
 		WrappedResponse wrappedRes = new WrappedResponse(res);

		chain.doFilter(req, wrappedRes);

		if (logPath == null) {
			return;
		}

		String s = req.getRemoteAddr() + " ";

		HttpSession session = req.getSession(false);
		User user = session == null ? null : (User)session.getAttribute("user");

		if (user == null) {
			s += "- ";
		} else if (user.name.contains(" ")) {
			s += "\"" + user.name + "\" ";
		} else {
			s += user.name + " ";
		}

		s += start.toString() + " ";

		s += req.getMethod() + " ";

		String path = req.getRequestURI();
		String qs = req.getQueryString();

		if (qs != null) {
			path += "?" + qs;
		} 
		s += path + " ";

		s += res.getStatus() + " ";

		s += wrappedRes.getLen() + " ";

		Long dt = Duration.between(start, LocalDateTime.now()).toMillis();
		s += dt.toString() + " ";

		Files.writeString(logPath, s + "\r\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	}
}
