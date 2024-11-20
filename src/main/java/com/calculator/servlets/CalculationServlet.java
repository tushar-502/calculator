package com.calculator.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.calculator.dao.CalculationsDAO;
import com.calculator.models.Calculations;

@WebServlet("/calc")
public class CalculationServlet extends HttpServlet {

    private CalculationsDAO calculationsDAO;

    @Override
    public void init() throws ServletException {
        calculationsDAO = new CalculationsDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        double result;

        try {
            result = evaluateExpression(input); // Evaluate the expression
            Calculations calculation = new Calculations(input, result);

            // Save calculation to the database
            calculationsDAO.saveCalculation(calculation);

            // Set the result in the request attribute to be displayed in JSP
            request.setAttribute("result", result);
            request.setAttribute("history", calculationsDAO.getAllCalculations()); // Add history to request
            request.getRequestDispatcher("/calculator.jsp").forward(request, response); // Forward to JSP

        } catch (SQLException | ScriptException ex) {
            // Provide detailed error handling
            request.setAttribute("error", "Error: Invalid expression or database issue!");
            request.getRequestDispatcher("/calculator.jsp").forward(request, response); // Forward to JSP
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Calculations> history = calculationsDAO.getAllCalculations();
            request.setAttribute("history", history);
            request.getRequestDispatcher("/calculator.jsp").forward(request, response);
        } catch (IOException | SQLException | ServletException ex) {
            // Handle exception and provide feedback to user
            request.setAttribute("error", "Error fetching calculation history");
            request.getRequestDispatcher("/calculator.jsp").forward(request, response);
        }
    }

    private double evaluateExpression(String expression) throws ScriptException {
        // Basic evaluation logic, consider using a custom parser for security
        if (!isValidExpression(expression)) {
            throw new ScriptException("Invalid expression");
        }
        return (double) new ScriptEngineManager().getEngineByName("JavaScript").eval(expression);
    }

    private boolean isValidExpression(String expression) {
        // Sanitize the expression to avoid potential issues
        // Example: only allow numbers, operators, and parentheses
        return expression.matches("[0-9+\\-*/().^ ]+");
    }
}
