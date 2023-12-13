package org.suai.todo.viewController;
import org.suai.todo.model.Model;
import org.suai.todo.model.User;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.IOException;

import java.util.HashMap;

public class TodoSettings extends TodoBase {
	@Override
	public void doGet(
			HttpServletRequest req, 
			HttpServletResponse res) 
	throws ServletException, IOException {
	
		User user = (User)req.getAttribute("user");
		HashMap<String, String> vars = new HashMap<String, String>();
		
		vars.put("username", user.name);
		vars.put("itemsperpage", user.itemsPerPage.toString());
		
		render(res, "settings", vars);
	}

	@Override
	public void doPost(
			HttpServletRequest req, 
			HttpServletResponse res) 
	throws ServletException, IOException {
	
		User user = (User)req.getAttribute("user");
		
		Integer ipp = getParameterInt(req, "itemsperpage", 0);
		String inputPassword = getParameterString(req, "password");
		String newPassword = getParameterString(req, "new");
		
		if (ipp > 0) {
			user.itemsPerPage = ipp;
		}
		
		if (inputPassword != null && inputPassword.length() > 0) {
			HashMap<String, String> vars = new HashMap<String, String>();
			vars.put("username", user.name);
			vars.put("itemsperpage", user.itemsPerPage.toString());
			
			if (newPassword == null || newPassword.length() == 0) {
				vars.put("warning", "Недопустимый пароль");
				render(res, "settings", vars);
				return;
			} else if(!user.isValidPassword(inputPassword)) {
				vars.put("warning", "Неверный пароль");
				render(res, "settings", vars);
				return;
			} else {
				user.updatePassword(newPassword);
				HttpSession session = req.getSession(true);
				session.setAttribute("user", user);
			}
		}
		Model.save(user);
		
		redirect(res, "list?parent=0&page=1");
	}
}
