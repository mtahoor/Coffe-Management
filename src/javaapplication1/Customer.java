package com.mycompany.coffee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Customer extends JPanel {

    private DefaultTableModel customerTableModel;

    public Customer() {
        setLayout(new BorderLayout());

        customerTableModel = new DefaultTableModel();
        customerTableModel.addColumn("ID");
        customerTableModel.addColumn("First Name");
        customerTableModel.addColumn("Last Name");
        customerTableModel.addColumn("Street");
        customerTableModel.addColumn("City");
        customerTableModel.addColumn("State");
        customerTableModel.addColumn("ZIP");
        customerTableModel.addColumn("Phone");
        customerTableModel.addColumn("Email");
        customerTableModel.addColumn("Credit Limit");

        JTable customerTable = new JTable(customerTableModel);

        JScrollPane scrollPane = new JScrollPane(customerTable);

        add(scrollPane, BorderLayout.CENTER);

        fetchCustomerData();
    }

    private void fetchCustomerData() {
        customerTableModel.setRowCount(0); 

        try {
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/Coffee", "root", "root");
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM Customer");

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("customer_number"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("street"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("zip"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getDouble("credit_limit")
                };
                customerTableModel.addRow(row);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching customer data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void refreshData() {
        fetchCustomerData();
    }
}
