package com.tictactoe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "InitServlet", urlPatterns = "/start")
public class InitServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(InitServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Processing GET request for /start");

        HttpSession currentSession = req.getSession(true);

        Field field = new Field();
        logger.info("New game field initialized");

        currentSession.setAttribute("field", field);
        currentSession.setAttribute("data", field.getFieldData());

        logger.info("Forwarding to index.jsp");
        getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}