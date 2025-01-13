package com.calculator.servlets;

import com.calculator.dao.CalculationsDAO;
import com.calculator.models.Calculations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.script.ScriptException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculationServletTest {

    private CalculationServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private CalculationsDAO calculationsDAO;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new CalculationServlet();
        servlet.init();
        servlet.calculationsDAO = calculationsDAO;
        responseWriter = new StringWriter();
    }

    @Test
    void testDoPostValidExpression() throws Exception {
        when(request.getParameter("input")).thenReturn("2+2");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request, response);
        doNothing().when(calculationsDAO).saveCalculation(any(Calculations.class));
        when(calculationsDAO.getAllCalculations()).thenReturn(new ArrayList<>());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("result"), eq(4.0));
        verify(request).setAttribute(eq("history"), anyList());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoPostInvalidExpression() throws Exception {
        when(request.getParameter("input")).thenReturn("invalid_expression");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request, response);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), contains("Invalid expression"));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoPostDatabaseError() throws Exception {
        when(request.getParameter("input")).thenReturn("2+2");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doThrow(new SQLException("Database error")).when(calculationsDAO).saveCalculation(any(Calculations.class));

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), contains("database issue"));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGetFetchHistory() throws Exception {
        List<Calculations> history = new ArrayList<>();
        history.add(new Calculations("2+2", 4.0));
        history.add(new Calculations("3*3", 9.0));

        when(calculationsDAO.getAllCalculations()).thenReturn(history);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request, response);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("history"), eq(history));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGetDatabaseError() throws Exception {
        when(calculationsDAO.getAllCalculations()).thenThrow(new SQLException("Database error"));
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request, response);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("error"), contains("Error fetching calculation history"));
        verify(requestDispatcher).forward(request, response);
    }
}
