import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.PreparedStatement;


public class Getorders extends JPanel {

    private JComboBox<String> customerComboBox;
    private JTable orderTable;
    private DefaultTableModel orderTableModel;

    public Getorders() {
        setLayout(new BorderLayout());

        JPanel selectionPanel = new JPanel(new FlowLayout());

        ArrayList<String> customers = fetchCustomers();
        customerComboBox = new JComboBox<>(customers.toArray(new String[0]));
        customerComboBox.setFocusable(true);



        JButton filterButton = new JButton("Filter Orders");
        filterButton.addActionListener(e -> filterOrders());

        selectionPanel.add(new JLabel("Select Customer:"));
        selectionPanel.add(customerComboBox);
        selectionPanel.add(filterButton);

        add(selectionPanel, BorderLayout.NORTH);

        JPanel orderPanel = new JPanel(new BorderLayout());

        orderTableModel = new DefaultTableModel();
        orderTableModel.addColumn("Order ID");
        orderTableModel.addColumn("Coffee Name");
        orderTableModel.addColumn("Coffee Description");
        orderTableModel.addColumn("Price");

        orderTable = new JTable(orderTableModel);
        JScrollPane orderScrollPane = new JScrollPane(orderTable);

        orderPanel.add(orderScrollPane, BorderLayout.CENTER);
        add(orderPanel, BorderLayout.CENTER);
    }

    private ArrayList<String> fetchCustomers() {
        ArrayList<String> customers = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/Coffee", "root", "root");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Customer");
            while (rs.next()) {
                customers.add(rs.getString("customer_number") + " - " + rs.getString("first_name") + " " + rs.getString("last_name"));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

   private void filterOrders() {
    orderTableModel.setRowCount(0); 

    String selectedCustomer = (String) customerComboBox.getSelectedItem();
    int customerId = Integer.parseInt(selectedCustomer.split(" - ")[0]);

    try {
        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/Coffee", "root", "root");
        String query = "SELECT Orders.order_id, Coffee.coffee_name, Coffee.coffee_description, Coffee.price " +
                       "FROM Orders " +
                       "INNER JOIN Coffee ON Orders.coffee_id = Coffee.coffee_id " +
                       "WHERE customer_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, customerId);

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Object[] row = {
                rs.getString("order_id"),
                rs.getString("coffee_name"),
                rs.getString("coffee_description"),
                rs.getDouble("price")
            };
            orderTableModel.addRow(row);
        }
        rs.close();
        pstmt.close();
        conn.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}



}
