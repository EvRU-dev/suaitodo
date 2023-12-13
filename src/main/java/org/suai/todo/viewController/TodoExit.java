package org.suai.todo.viewController;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.IOException;

public class TodoExit extends TodoBase {
	@Override
	public void doGet(
			HttpServletRequest req, 
			HttpServletResponse res) 
	throws ServletException, IOException {
	
		HttpSession session = req.getSession(false);
		
		if (session != null) {
			session.removeAttribute("user");
			session.invalidate();
		}
		redirect(res, "login");
	}
}
