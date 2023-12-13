package org.suai.todo.viewController;
import org.suai.todo.model.User;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.IOException;

public class TodoDefault extends TodoBase {
	@Override
	public void doGet(
			HttpServletRequest req, 
			HttpServletResponse res) 
	throws ServletException, IOException {
	
		User user = null;
		HttpSession session = req.getSession(false);

		if (session != null) {
			user = (User)session.getAttribute("user");
		}
		String cPath = req.getContextPath();
		
		if (user == null) {
			redirect(res, cPath + "/login");
		} else {
			redirect(res, cPath + "/list?parent=0&page=1");
		}
	}
}
