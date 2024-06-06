package com.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

public class RestartServletTest {
    private RestartServlet restartServlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        restartServlet = new RestartServlet();

        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testDoPost() throws Exception {
        restartServlet.doPost(request, response);

        verify(request, times(1)).getSession();
        verify(session, times(1)).invalidate();
        verify(response, times(1)).sendRedirect("/start");
    }
}
