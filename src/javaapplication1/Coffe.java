package com.mycompany.coffee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Coffe extends JPanel {

    private DefaultTableModel coffeeTableModel;

    public Coffe() {
        setLayout(new BorderLayout());

        coffeeTableModel = new DefaultTableModel();
        coffeeTableModel.addColumn("ID");
        coffeeTableModel.addColumn("Name");
        coffeeTableModel.addColumn("Description");
        coffeeTableModel.addColumn("Price");
        coffeeTableModel.addColumn("Stock");

        JTable coffeeTable = new JTable(coffeeTableModel);

        JScrollPane scrollPane = new JScrollPane(coffeeTable);

        add(scrollPane, BorderLayout.CENTER);

        fetchCoffeeData();
    }

    private void fetchCoffeeData() {
        coffeeTableModel.setRowCount(0); 

        try {
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/Coffee", "root", "root");
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM Coffee");

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("coffee_id"),
                        rs.getString("coffee_name"),
                        rs.getString("coffee_description"),
                        rs.getDouble("price"),
                        rs.getInt("number_in_stock")
                };
                coffeeTableModel.addRow(row);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching coffee data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
     public void refreshData() {
        fetchCoffeeData();
    }
}
