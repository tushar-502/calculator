package com.calculator.dao;

import com.calculator.models.Calculations;
import com.calculator.connection.DatabaseConnection;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculationsDAOTest {

    private CalculationsDAO calculationsDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private Statement mockStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() {
        calculationsDAO = new CalculationsDAO();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockStatement = mock(Statement.class);
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    void testSaveCalculation() throws Exception {
        Calculations calculation = new Calculations();
        calculation.setInput("2+2");
        calculation.setResult(4.0);

        try (MockedStatic<DatabaseConnection> mockedDbConnection = mockStatic(DatabaseConnection.class)) {
            mockedDbConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

            calculationsDAO.saveCalculation(calculation);

            verify(mockPreparedStatement).setString(1, "2+2");
            verify(mockPreparedStatement).setDouble(2, 4.0);
            verify(mockPreparedStatement).executeUpdate();
        }
    }

    @Test
    void testGetAllCalculations() throws Exception {
        try (MockedStatic<DatabaseConnection> mockedDbConnection = mockStatic(DatabaseConnection.class)) {
            mockedDbConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, true, false);
            when(mockResultSet.getInt("id")).thenReturn(1, 2);
            when(mockResultSet.getString("input")).thenReturn("2+2", "3*3");
            when(mockResultSet.getDouble("result")).thenReturn(4.0, 9.0);
            when(mockResultSet.getTimestamp("timestamp")).thenReturn(new Timestamp(System.currentTimeMillis()));

            List<Calculations> calculations = calculationsDAO.getAllCalculations();

            assertEquals(2, calculations.size());
            assertEquals("2+2", calculations.get(0).getInput());
            assertEquals(4.0, calculations.get(0).getResult());
            assertEquals("3*3", calculations.get(1).getInput());
            assertEquals(9.0, calculations.get(1).getResult());
        }
    }

    @Test
    void testClearAllCalculations() throws Exception {
        try (MockedStatic<DatabaseConnection> mockedDbConnection = mockStatic(DatabaseConnection.class)) {
            mockedDbConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.createStatement()).thenReturn(mockStatement);

            calculationsDAO.clearAllCalculations();

            verify(mockStatement).executeUpdate("DELETE FROM Calculations");
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        mockConnection.close();
        mockPreparedStatement.close();
        mockStatement.close();
        mockResultSet.close();
    }
}
