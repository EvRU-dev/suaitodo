package org.suai.todo.viewController;

import org.suai.todo.model.Record;
import org.suai.todo.model.Model;
import org.suai.todo.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TodoDelete extends TodoBase {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Integer recordId = getParameterInt(req, "id", 0);
		Integer parent = getParameterInt(req, "parent", 0);
		Integer page = getParameterInt(req, "page", 1);

		if (recordId > 0) {
			User loggedInUser = (User) req.getSession().getAttribute("loggedInUser");
			Record record = Model.getTodo(loggedInUser, recordId);
			if (record != null) {
				Model.delete(record);
			}
		}

		redirect(res, "list?parent=" + parent + "&page=" + page);
	}
}
