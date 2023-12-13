package org.suai.todo.viewController;
import org.suai.todo.model.Model;
import org.suai.todo.model.User;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.IOException;

import java.util.HashMap;

public class TodoLogin extends TodoBase {
	@Override
	public void doGet(
			HttpServletRequest req, 
			HttpServletResponse res) 
	throws ServletException, IOException {
	
		render(res, "login", null);
	}

	@Override
	public void doPost(
			HttpServletRequest req, 
			HttpServletResponse res)
	throws ServletException, IOException {
	
		String name = getParameterString(req, "name");
		String password = getParameterString(req, "password");
		
		User user = Model.getUserByName(name);
		HashMap<String, String>vars = new HashMap<String, String>();
		
		vars.put("name", name);	
		
		if (user == null) {
			vars.put("warning", "Неверное имя пользователя");
			render(res, "login", vars);
			return;
		} else if (!user.isValidPassword(password)) {
			vars.put("warning", "Пароль неверен");
			render(res, "login", vars);
			return;
		}
		HttpSession session = req.getSession(true);
		session.setAttribute("user", user);
		System.out.println("User set in session: " + user.getName());


		redirect(res, "list?parent=0&page=1");
	}
}
