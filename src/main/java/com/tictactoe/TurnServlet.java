package com.tictactoe;

import com.exception.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "TurnServlet", value = "/turn")
public class TurnServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(TurnServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession currentSession = req.getSession();
        Field field = extractField(currentSession);
        int index = getSelectedIndex(req);
        logger.info("Selected index: {}", index);

        Sign currentSign = field.getField().get(index);
        if (Sign.EMPTY != currentSign) {
            logger.info("Selected cell is not empty, forwarding to index.jsp");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(req, resp);
            return;
        }

        field.getField().put(index, Sign.CROSS);
        logger.info("Placed CROSS at index {}", index);

        if (checkWin(resp, currentSession, field)) {
            return;
        }

        int emptyFieldIndex = field.getEmptyFieldIndex();
        if (emptyFieldIndex >= 0) {
            field.getField().put(emptyFieldIndex, Sign.NOUGHT);
            logger.info("Placed NOUGHT at index {}", emptyFieldIndex);

            if (checkWin(resp, currentSession, field)) {
                return;
            }
        } else {
            logger.info("No empty fields left, game is a draw");
            currentSession.setAttribute("draw", true);
            List<Sign> data = field.getFieldData();
            currentSession.setAttribute("data", data);
            resp.sendRedirect("/index.jsp");
            return;
        }

        List<Sign> data = field.getFieldData();
        currentSession.setAttribute("data", data);
        currentSession.setAttribute("field", field);

        logger.info("Redirecting to index.jsp");
        resp.sendRedirect("/index.jsp");
    }

    private boolean checkWin(HttpServletResponse response, HttpSession currentSession, Field field) throws IOException {
        Sign winner = field.checkWin();
        if (Sign.CROSS == winner || Sign.NOUGHT == winner) {
            currentSession.setAttribute("winner", winner);
            List<Sign> data = field.getFieldData();
            currentSession.setAttribute("data", data);
            logger.info("{} has won the game", winner);
            response.sendRedirect("/index.jsp");
            return true;
        }
        return false;
    }

    private int getSelectedIndex(HttpServletRequest request) {
        String click = request.getParameter("click");
        boolean isNumeric = click.chars().allMatch(Character::isDigit);
        int index = isNumeric ? Integer.parseInt(click) : 0;
        logger.info("Click parameter: {}, parsed index: {}", click, index);
        return index;
    }

    private Field extractField(HttpSession currentSession) {
        Object fieldAttribute = currentSession.getAttribute("field");
        if (Field.class != fieldAttribute.getClass()) {
            currentSession.invalidate();
            logger.error("Field attribute is invalid, invalidating session");
            throw new InvalidArgumentException("Field attribute is invalid");
        }
        return (Field) fieldAttribute;
    }
}