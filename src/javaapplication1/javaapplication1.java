import com.mycompany.coffee.Newcustomer;
import com.mycompany.coffee.Coffe;
import com.mycompany.coffee.Customer;
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

public class javaapplication1 extends JFrame implements ActionListener {

    private Coffe coffeePanel;
    private Customer customerPanel;
    private Getorders getOrdersPanel;
    private Newcustomer addCustomerPanel;
    private Neworder newOrderPanel;
    private JDialog addCustomerDialog;

    public javaapplication1() {
        setTitle("Coffee Shop Management System");

        setSize(1200, 1000);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton displayCustomersBtn = new JButton("Display Customers");
        JButton displayCoffeeBtn = new JButton("Display Coffee");
        JButton getOrdersBtn = new JButton("Get Orders");
        JButton addCustomerBtn = new JButton("Add Customer");
        JButton newOrderBtn = new JButton("New Order");
        JButton createDatabaseBtn = new JButton("Create Database");
        JButton dropDatabaseBtn = new JButton("Drop Database");
        

        displayCustomersBtn.addActionListener(this);
        createDatabaseBtn.addActionListener(this);
        getOrdersBtn.addActionListener(this);
        dropDatabaseBtn.addActionListener(this);
        displayCoffeeBtn.addActionListener(this);
        addCustomerBtn.addActionListener(this);
        newOrderBtn.addActionListener(this);

        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        leftPanel.add(displayCustomersBtn);
        leftPanel.add(displayCoffeeBtn);
        leftPanel.add(getOrdersBtn);
        leftPanel.add(addCustomerBtn);
        leftPanel.add(newOrderBtn);
        leftPanel.add(createDatabaseBtn);
        leftPanel.add(dropDatabaseBtn);
        add(leftPanel, BorderLayout.WEST); 

        customerPanel = new Customer();
        coffeePanel = new Coffe();
        addCustomerPanel = new Newcustomer();
        newOrderPanel = new Neworder();
        getOrdersPanel = new Getorders();
        

        setLocationRelativeTo(null);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Display Customers")) {
            customerPanel.refreshData();
            add(customerPanel, BorderLayout.CENTER);
            coffeePanel.setVisible(false);         
            addCustomerPanel.setVisible(false);
            customerPanel.setVisible(true);            
            newOrderPanel.setVisible(false); 
            getOrdersPanel.setVisible(false);
        } else if (e.getActionCommand().equals("Display Coffee")) {
            // Show Coffee panel
            coffeePanel.refreshData();
            add(coffeePanel, BorderLayout.CENTER);
            customerPanel.setVisible(false);
            addCustomerPanel.setVisible(false);
            coffeePanel.setVisible(true);            
            newOrderPanel.setVisible(false);
            getOrdersPanel.setVisible(false);
        } else if(e.getActionCommand().equals("Add Customer")){
            add(addCustomerPanel, BorderLayout.CENTER);
            customerPanel.setVisible(false);
            addCustomerPanel.setVisible(true);
            coffeePanel.setVisible(false);
            newOrderPanel.setVisible(false);
            getOrdersPanel.setVisible(false);
        } else if(e.getActionCommand().equals("New Order")){
            add(newOrderPanel, BorderLayout.CENTER);
            customerPanel.setVisible(false);
            addCustomerPanel.setVisible(false);
            coffeePanel.setVisible(false);            
            newOrderPanel.setVisible(true);
            getOrdersPanel.setVisible(false);
        }
        else if(e.getActionCommand().equals("Get Orders")){
            add(getOrdersPanel, BorderLayout.CENTER);
            customerPanel.setVisible(false);
            addCustomerPanel.setVisible(false);
            coffeePanel.setVisible(false);            
            newOrderPanel.setVisible(false);
            getOrdersPanel.setVisible(true);
        }
        else if (e.getActionCommand().equals("Create Database")) {
            createDatabase();
        } else if (e.getActionCommand().equals("Drop Database")) {
            dropDatabase();
        }
    }

    private void createDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/Coffee", "root", "root");

            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE Customer (customer_number INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, first_name VARCHAR(50), last_name VARCHAR(50), street VARCHAR(100), city VARCHAR(50), state VARCHAR(50), zip VARCHAR(10), phone VARCHAR(20), email VARCHAR(100), credit_limit DOUBLE)");
            stmt.executeUpdate("CREATE TABLE Coffee (coffee_id INT PRIMARY KEY, coffee_name VARCHAR(100), coffee_description VARCHAR(255), price DOUBLE, number_in_stock INT)");
            stmt.executeUpdate("CREATE TABLE Orders (order_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, customer_id INT, coffee_id INT, number_ordered DOUBLE, total DOUBLE)");

           
            stmt.executeUpdate("INSERT INTO Customer (first_name, last_name, street, city, state, zip, phone, email, credit_limit) VALUES ('John', 'Doe', '123 Main St', 'Anytown', 'CA', '12345', '555-1234', 'john@example.com', 100.0)");
            stmt.executeUpdate("INSERT INTO Customer (first_name, last_name, street, city, state, zip, phone, email, credit_limit) VALUES ('Jane', 'Smith', '456 Oak St', 'Sometown', 'NY', '54321', '555-5678', 'jane@example.com', 200.0)");
            stmt.executeUpdate("INSERT INTO Customer (first_name, last_name, street, city, state, zip, phone, email, credit_limit) VALUES ('Alice', 'Johnson', '789 Elm St', 'Anycity', 'FL', '67890', '555-9876', 'alice@example.com', 150.0)");
            stmt.executeUpdate("INSERT INTO Customer (first_name, last_name, street, city, state, zip, phone, email, credit_limit) VALUES ('Bob', 'Williams', '101 Pine St', 'Somecity', 'TX', '98765', '555-4321', 'bob@example.com', 300.0)");
            stmt.executeUpdate("INSERT INTO Customer (first_name, last_name, street, city, state, zip, phone, email, credit_limit) VALUES ('Eva', 'Brown', '202 Cedar St', 'Othercity', 'WA', '13579', '555-2468', 'eva@example.com', 250.0)");

        
            stmt.executeUpdate("INSERT INTO Coffee VALUES (1, 'Espresso', 'Strong coffee', 2.5, 10)");
            stmt.executeUpdate("INSERT INTO Coffee VALUES (2, 'Latte', 'Coffee with milk', 3.0, 8)");
            stmt.executeUpdate("INSERT INTO Coffee VALUES (3, 'Cappuccino', 'Espresso with frothy milk', 3.5, 6)");
            stmt.executeUpdate("INSERT INTO Coffee VALUES (4, 'Americano', 'Diluted espresso', 2.0, 12)");
            stmt.executeUpdate("INSERT INTO Coffee VALUES (5, 'Mocha', 'Coffee with chocolate and milk', 4.0, 5)");
            stmt.executeUpdate("INSERT INTO Coffee VALUES (6, 'Macchiato', 'Espresso with a small amount of milk', 3.0, 7)");
            stmt.executeUpdate("INSERT INTO Coffee VALUES (7, 'Affogato', 'Espresso poured over vanilla ice cream', 4.5, 3)");
            stmt.executeUpdate("INSERT INTO Coffee VALUES (8, 'Flat White', 'Coffee with velvety milk', 3.5, 9)");

            stmt.executeUpdate("INSERT INTO Orders (customer_id, coffee_id, number_ordered, total) VALUES (1, 1, 2, 5.0)");
            stmt.executeUpdate("INSERT INTO Orders (customer_id, coffee_id, number_ordered, total) VALUES (2, 2, 1, 3.0)");
            stmt.executeUpdate("INSERT INTO Orders (customer_id, coffee_id, number_ordered, total) VALUES (3, 3, 3, 10.5)");
            stmt.executeUpdate("INSERT INTO Orders (customer_id, coffee_id, number_ordered, total) VALUES (4, 4, 1, 2.0)");
            stmt.executeUpdate("INSERT INTO Orders (customer_id, coffee_id, number_ordered, total) VALUES (5, 5, 2, 8.0)");
            stmt.executeUpdate("INSERT INTO Orders (customer_id, coffee_id, number_ordered, total) VALUES (1, 6, 1, 3.0)");
            stmt.executeUpdate("INSERT INTO Orders (customer_id, coffee_id, number_ordered, total) VALUES (2, 7, 2, 9.0)");
            stmt.executeUpdate("INSERT INTO Orders (customer_id, coffee_id, number_ordered, total) VALUES (3, 8, 1, 3.5)");
            stmt.executeUpdate("INSERT INTO Orders (customer_id, coffee_id, number_ordered, total) VALUES (4, 1, 2, 5.0)");
            stmt.executeUpdate("INSERT INTO Orders (customer_id, coffee_id, number_ordered, total) VALUES (5, 2, 1, 3.0)");


            JOptionPane.showMessageDialog(this, "Tables created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
           
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void dropDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/Coffee", "root", "root");
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("DROP TABLE Customer");

            stmt.executeUpdate("DROP TABLE Coffee");

            stmt.executeUpdate("DROP TABLE Orders");

            stmt.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Tables dropped successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error dropping tables: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new javaapplication1();
    }
}
