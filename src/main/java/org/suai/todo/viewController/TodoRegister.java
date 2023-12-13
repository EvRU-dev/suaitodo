package org.suai.todo.viewController;
import org.suai.todo.model.Model;
import org.suai.todo.model.User;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.IOException;

import java.util.HashMap;

public class TodoRegister extends TodoBase {
	@Override
	public void doGet(
			HttpServletRequest req, 
			HttpServletResponse res) 
	throws ServletException, IOException {
	
		render(res, "register", null);
	}

	@Override
	public void doPost(
			HttpServletRequest req, 
			HttpServletResponse res) 
	throws ServletException, IOException {
	
		String name = getParameterString(req, "name");
		String password = getParameterString(req, "password");
		name = name.trim();
		
		if (name.length() == 0 || password.length() == 0) {
			redirect(res, "register");
			return;
		}
		User user = Model.getUserByName(name);
		
		if (user != null) {
			HashMap<String, String> vars = new HashMap<String, String>();
			vars.put("warning", "Пользователь с таким именем уже зарегистрирован");
			render(res, "register", vars);
			return;
		}
		user = new User(name, password);
		Model.save(user);
		
		redirect(res, "login");
	}
}
