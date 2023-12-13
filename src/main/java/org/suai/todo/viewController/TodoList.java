package org.suai.todo.viewController;
import org.suai.todo.model.Record;
import org.suai.todo.model.Model;
import org.suai.todo.model.User;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.IOException;

import java.util.HashMap;
import java.util.ArrayList;

import java.time.LocalDate;

public class TodoList extends TodoBase {
	@Override
	public void doGet(
			HttpServletRequest req, 
			HttpServletResponse res) 
	throws ServletException, IOException {

		// Добавьте следующие строки, чтобы получить пользователя из сессии
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");

		Record record  = (Record)req.getAttribute("record");
		Record parentRecord  = (Record)req.getAttribute("parentRecord");
		Integer parent = parentRecord == null ? 0 : parentRecord.id;
		Integer page   = getParameterInt(req, "page", 0);

		Integer count = Model.getCount(user, parent);

		Integer recCount = Model.getCount(user, parent);
		Integer from = (page - 1) * user.itemsPerPage;

		if (from > recCount || page == 0) {
			redirect(res, "list?parent=" + parent + "&page=1");
			return;
		}
		ArrayList<Record> records = Model.getTodos(user, parent, from);
		HashMap<String, String>vars = new HashMap<String, String>();
		
		vars.put("pages", drawPageLinks(user, recCount, page, parent));
		vars.put("todos", drawRows(page, records));
		vars.put("username", user.name);
		vars.put("parent", parent.toString());
		vars.put("page", page.toString());
		
		if (parentRecord == null) {
			vars.put("parent_caption", "TODO");
		} else {
			vars.put("parent_caption", parentRecord.caption);
			vars.put("back", "<a href='list?parent=" 
				+ parentRecord.parent
				+ "&page=0' title='Перейти к родительскому списку'>наверх</a>&nbsp; ");
		}
		render(res, "list", vars);
	}
	
	private String drawPageLinks(User user, Integer size, Integer page, Integer parent) {
		Integer pageCount = 1 + (size - 1) / user.itemsPerPage;
		ArrayList<String> a = new ArrayList<String>();
		for (int i = 0; i < pageCount; i++) {
			if (i + 1 == page) {
				a.add(page.toString());
			} else {
				a.add("<a href='list?parent=" + parent
					+ "&page=" + (i + 1) + "'>" + (i + 1) + "</a>");
			}
		}
		return String.join(" ", a);
	}
	
	private String drawRows(Integer page, ArrayList<Record> records) 
	throws IOException {
		ArrayList<String> rows = new ArrayList<String>();

		for (Record rec : records) {
			HashMap<String, String> v = new HashMap<String, String>();
			
			v.put("id", rec.id.toString());
			v.put("parent", rec.parent.toString());
			v.put("page", page.toString());
			
			LocalDate deadline = rec.deadline.toLocalDate();
			v.put("date", deadline.toString());
			String cls = "caption";
			
			if (rec.priority > 0) {
				cls += " priority";
			}
			if (rec.done > 0) {
				cls += " done";
			}
			v.put("class", cls);
			v.put("caption", rec.caption);
			v.put("comment", rec.comment);
			
			rows.add(fillView("listitem", v));
		}
		return String.join("\r\n", rows);
	}
}
