package org.suai.todo.viewController;
import org.suai.todo.model.Record;
import org.suai.todo.model.Model;
import org.suai.todo.model.User;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TodoEdit extends TodoBase {
	@Override
	public void doGet(
			HttpServletRequest req,
			HttpServletResponse res)
			throws ServletException, IOException {

		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		Record record = (Record) req.getAttribute("record");
		Integer parent = getParameterInt(req, "parent", 0);
		Integer page = getParameterInt(req, "page", 0);

		// Добавьте этот блок кода для проверки, устанавливается ли объект "user" в сессии
		if (user == null) {
			redirect(res, "login"); // Перенаправьте на страницу входа, если пользователь не авторизован
			return;
		}

		HashMap<String, String> vars = new HashMap<String, String>();

		vars.put("username", user.name);
		vars.put("id", record == null ? "0" : (record.id.toString()));
		vars.put("parent", parent.toString());
		vars.put("page", page.toString());
		vars.put("date", drawDate(record));

		if (record == null) {
			// создается новая запись
			render(res, "edit", vars);
			return;
		}

		if (record.priority > 0) {
			vars.put("priority", "checked");
		}
		if (record.done > 0) {
			vars.put("done", "checked");
		}
		vars.put("caption", record.caption);
		vars.put("comment", record.comment);

		render(res, "edit", vars);
	}


	private String drawDate(Record r) {
		// рисует списки для выбора дня, месяца и года
		
		ArrayList<String> rows = new ArrayList<String>();
		LocalDateTime d = r == null ? LocalDate.now().plusDays(1).atStartOfDay() : r.deadline;
		LocalDateTime now = LocalDateTime.now();

		// список лет
		rows.add("<select name=year>");
		
		if (d.getYear() < now.getYear()) {
			// год вне списка
			rows.add(drawOption(d.getYear(), d.getYear()));
		}
		for (int i = now.getYear(); i < now.getYear() + 10; i++) {
			rows.add(drawOption(i, d.getYear()));
		}
		rows.add("</select>-");
		
		// список месяцев
		rows.add("<select name=month>");
		
		for (int i = 1; i <= 12; i++) {
			rows.add(drawOption(i, d.getMonthValue()));
		}
		rows.add("</select>-");
		
		// список дней
		rows.add("<select name=day>");
		
		for (int i = 1; i <= 31; i++) {
			rows.add(drawOption(i, d.getDayOfMonth()));
		}
		rows.add("</select>");
		
		return String.join("\r\n", rows);
	}
	
	private String drawOption(int value, int select) {
		String arg = value == select ? " selected" : "";
		return "<option" + arg + ">" + value + "</option>"; 
	}

	@Override
	public void doPost(
			HttpServletRequest req, 
			HttpServletResponse res)
	throws ServletException, IOException {

		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");

		Record record = (Record)req.getAttribute("record");
		Integer parent = getParameterInt(req, "parent", 0);
		Integer page = getParameterInt(req, "page", 0);

		if (record == null) {
			record = create(user, parent);
		}
		update(req, record);
		
		redirect(res, "list?parent=" + record.parent + "&page=" + page);
	}

	private Record create(User user, Integer parent) {
		if (user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}

		Record record = new Record(user.id);
		record.id = 0;
		record.owner = user.id;
		record.parent = parent;
		record.created = LocalDateTime.now();

		return record;
	}


	
	private void update(HttpServletRequest req, Record r) throws ServletException {
		Integer year  = getParameterInt(req, "year", 0);
		Integer month = getParameterInt(req, "month", 0);
		Integer day   = getParameterInt(req, "day", 0);
		
		LocalDateTime ldt = LocalDateTime.now();
		
		Integer minYear = ldt.getYear();
		Integer maxYear = ldt.getYear() + 10;
		
		if (r.deadline != null) {
			minYear = Math.min(minYear, r.deadline.getYear());
			maxYear = Math.max(maxYear, r.deadline.getYear());
		}
		if (year >= minYear && year <= maxYear && 
		    month >= 1 && month <= 12 &&
		    day >= 1 && day <= 31) {
		    
			// коррекция дня месяца
			LocalDate ld = LocalDate.of(year, month, 1);
			day = Math.min(day, ld.lengthOfMonth());
			// проверенная дата
			ldt = LocalDateTime.of(year, month, day, 0, 0);
		}
		r.deadline = ldt;
		r.priority = getParameterInt(req, "priority", 0);
		r.done     = getParameterInt(req, "done", 0);
		r.caption  = getParameterString(req, "caption");
		r.comment  = getParameterString(req, "comment");
		
		Model.save(r);
	}
}
