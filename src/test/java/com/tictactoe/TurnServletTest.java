package com.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TurnServletTest {
    private TurnServlet turnServlet;
    private Field field;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private ServletContext servletContext;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setUp() throws ServletException {
        MockitoAnnotations.openMocks(this);
        turnServlet = new TurnServlet();
        field = new Field();

        when(request.getSession()).thenReturn(session);
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        turnServlet.init(servletConfig);

    }

    @Test
    public void testDoGet_TargetCellIsNotEmpty_NoWinner() throws Exception {
        when(request.getParameter("click")).thenReturn("2");
        when(session.getAttribute("field")).thenReturn(field);

        field.getField().put(2, Sign.NOUGHT);
        turnServlet.doGet(request, response);

        verify(request, times(1)).getSession();
        verify(dispatcher, times(1)).forward(request, response);
        verify(session, never()).setAttribute(eq("winner"), any(Sign.class));
    }

    @Test
    public void testDoGet_TargetCellIsEmpty_NoWinner() throws Exception {
        when(request.getParameter("click")).thenReturn("1");
        when(session.getAttribute("field")).thenReturn(field);

        turnServlet.doGet(request, response);

        assertEquals(Sign.CROSS, field.getField().get(1));

        verify(request, times(1)).getSession();
        verify(session, times(1)).setAttribute("data", field.getFieldData());
        verify(session, times(1)).setAttribute("field", field);
        verify(response, times(1)).sendRedirect("/index.jsp");
        verify(session, never()).setAttribute(eq("winner"), any(Sign.class));
    }

    @Test
    public void testDoGet_TargetCellIsEmpty_CrossWon() throws Exception {
        when(request.getParameter("click")).thenReturn("0");
        when(session.getAttribute("field")).thenReturn(field);
        field.getField().put(1, Sign.CROSS);
        field.getField().put(2, Sign.CROSS);

        turnServlet.doGet(request, response);

        verify(request, times(1)).getSession();
        verify(session, times(1)).setAttribute("winner", Sign.CROSS);
        verify(session, times(1)).setAttribute("data", field.getFieldData());
        verify(response, times(1)).sendRedirect("/index.jsp");
    }

    @Test
    public void testDoGet_TargetCellIsEmpty_NoughtWon() throws Exception {
        when(request.getParameter("click")).thenReturn("3");
        when(session.getAttribute("field")).thenReturn(field);
        field.getField().put(1, Sign.NOUGHT);
        field.getField().put(2, Sign.NOUGHT);

        turnServlet.doGet(request, response);

        verify(request, times(1)).getSession();
        verify(session, times(1)).setAttribute("winner", Sign.NOUGHT);
        verify(session, times(1)).setAttribute("data", field.getFieldData());
        verify(response, times(1)).sendRedirect("/index.jsp");
    }

    @Test
    public void testDoGet_TargetCellIsEmpty_Draw() throws Exception {
        when(request.getParameter("click")).thenReturn("1");
        when(session.getAttribute("field")).thenReturn(field);
        Map<Integer, Sign> fieldMap = field.getField();
        fieldMap.put(0, Sign.NOUGHT);
        fieldMap.put(2, Sign.NOUGHT);
        fieldMap.put(3, Sign.CROSS);
        fieldMap.put(4, Sign.CROSS);
        fieldMap.put(5, Sign.NOUGHT);
        fieldMap.put(6, Sign.CROSS);
        fieldMap.put(7, Sign.NOUGHT);
        fieldMap.put(8, Sign.CROSS);

        turnServlet.doGet(request, response);

        verify(session, times(1)).setAttribute("draw", true);
        verify(session, times(1)).setAttribute("data", field.getFieldData());
        verify(response, times(1)).sendRedirect("/index.jsp");
    }

    @Test
    public void testDoGet_FieldIsInvalid() throws Exception {
        Object wrongObject = new Object();
        when(session.getAttribute("field")).thenReturn(wrongObject);

        assertThrows(RuntimeException.class,
                () -> turnServlet.doGet(request, response));
        verify(request, times(1)).getSession();
    }
}
