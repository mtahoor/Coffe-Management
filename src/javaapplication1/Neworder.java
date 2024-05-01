import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Neworder extends JPanel {

    private JComboBox<String> customerComboBox, coffeeComboBox;
    private JTextField quantityField;

    public Neworder() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding between components

        JLabel customerLabel = new JLabel("Select Customer:");
        customerComboBox = new JComboBox<>(fetchCustomers());
        addComponent(customerLabel, gbc, 0, 0);
        addComponent(customerComboBox, gbc, 1, 0);

        JLabel coffeeLabel = new JLabel("Select Coffee:");
        coffeeComboBox = new JComboBox<>(fetchCoffees());
        addComponent(coffeeLabel, gbc, 0, 1);
        addComponent(coffeeComboBox, gbc, 1, 1);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField(10);
        addComponent(quantityLabel, gbc, 0, 2);
        addComponent(quantityField, gbc, 1, 2);

        JButton addButton = new JButton("Add Order");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOrder();
            }
        });
        gbc.gridwidth = 2; 
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(addButton, gbc);
    }

    private void addComponent(Component component, GridBagConstraints gbc, int gridx, int gridy) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        add(component, gbc);
    }

    private String[] fetchCustomers() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/Coffee", "root", "root");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Customer");
            return formatResultSet(rs, "customer_number", "first_name", "last_name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    private String[] fetchCoffees() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/Coffee", "root", "root");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Coffee");
            return formatResultSet(rs, "coffee_id", "coffee_name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    private String[] formatResultSet(ResultSet rs, String... columns) throws SQLException {
        java.util.List<String> list = new java.util.ArrayList<>();
        while (rs.next()) {
            StringBuilder sb = new StringBuilder();
            for (String column : columns) {
                sb.append(rs.getString(column)).append(" - ");
            }
            list.add(sb.substring(0, sb.length() - 3));
        }
        return list.toArray(new String[0]);
    }

    private void addOrder() {
        if (!validateInput()) {
            JOptionPane.showMessageDialog(this, "Please enter valid data for all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selectedCustomer = (String) customerComboBox.getSelectedItem();
        int customerId = Integer.parseInt(selectedCustomer.split(" - ")[0]);

        String selectedCoffee = (String) coffeeComboBox.getSelectedItem();
        int coffeeId = Integer.parseInt(selectedCoffee.split(" - ")[0]);

        int quantity = Integer.parseInt(quantityField.getText());

        int availableStock = fetchStock(coffeeId);

        if (quantity > availableStock) {
            JOptionPane.showMessageDialog(this, "Quantity ordered exceeds available stock.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/Coffee", "root", "root");

            reduceStock(conn, coffeeId, quantity);

            String query = "INSERT INTO Orders (customer_id, coffee_id, number_ordered) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, coffeeId);
            pstmt.setInt(3, quantity);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Order added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding order: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int fetchStock(int coffeeId) {
        int stock = 0;
        try {
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/Coffee", "root", "root");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT number_in_stock FROM Coffee WHERE coffee_id = " + coffeeId);
            if (rs.next()) {
                stock = rs.getInt("number_in_stock");
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stock;
    }

    private void reduceStock(Connection conn, int coffeeId, int quantity) throws SQLException {
        String query = "UPDATE Coffee SET number_in_stock = number_in_stock - ? WHERE coffee_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, quantity);
        pstmt.setInt(2, coffeeId);
        pstmt.executeUpdate();
        pstmt.close();
    }

    private boolean validateInput() {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
