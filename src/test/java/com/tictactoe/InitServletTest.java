package com.tictactoe;

import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;
import static org.mockito.Mockito.*;

public class InitServletTest {
    private InitServlet initServlet;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;
    @Mock private RequestDispatcher dispatcher;
    @Mock private ServletConfig servletConfig;
    @Mock private ServletContext servletContext;

    @BeforeEach
    public void setUp() throws ServletException {
        MockitoAnnotations.openMocks(this);
        initServlet = new InitServlet();

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);
        when(request.getSession(true)).thenReturn(session);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        initServlet.init(servletConfig);
    }

    @Test
    public void testDoGet() throws Exception {
        initServlet.doGet(request, response);

        verify(request, times(1)).getSession(true);
        verify(dispatcher, times(1)).forward(request, response);

        verify(session, times(1)).setAttribute(eq("field"), any(Field.class));
        verify(session, times(1)).setAttribute(eq("data"), any(List.class));
    }
}
