package com.calculator.dao;

import com.calculator.models.Calculations;
import com.calculator.connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalculationsDAO {

    // Save a calculation to the database
    public void saveCalculation(Calculations calculation) throws SQLException {
        String query = "INSERT INTO Calculations (input, result) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, calculation.getInput());
            stmt.setDouble(2, calculation.getResult());
            stmt.executeUpdate();
        }
    }

    // Retrieve all calculations
    public List<Calculations> getAllCalculations() throws SQLException {
        List<Calculations> calculations = new ArrayList<>();
        String query = "SELECT * FROM Calculations ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Calculations calc = new Calculations();
                calc.setId(rs.getInt("id"));
                calc.setInput(rs.getString("input"));
                calc.setResult(rs.getDouble("result"));
                calc.setTimestamp(rs.getTimestamp("timestamp"));
                calculations.add(calc);
            }
        }
        return calculations;
    }

    // Clear all calculations
    public void clearAllCalculations() throws SQLException {
        String query = "DELETE FROM Calculations";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
        }
    }
}
