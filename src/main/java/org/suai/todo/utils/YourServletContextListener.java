package org.suai.todo.utils;

import org.suai.todo.model.Model;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.ServletException;

@WebListener
public class YourServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String dbFilePath = servletContextEvent.getServletContext().getInitParameter("dbfilename");

        try {
            Model.init(dbFilePath);
        } catch (ServletException e) {
            e.printStackTrace(); // Обработайте исключение в соответствии с вашими потребностями
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        // Код, который должен выполняться при уничтожении контекста (остановке приложения)
    }
}

