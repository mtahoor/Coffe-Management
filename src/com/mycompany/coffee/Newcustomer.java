import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Newcustomer extends JFrame {

    private JTextField customerNumberField, firstNameField, lastNameField, streetField, cityField, stateField, zipField, phoneField, emailField;

    public Newcustomer() {
        // Set title of the JFrame
        setTitle("Add New Customer");

        // Set size of the JFrame
        setSize(400, 400);

        // Set default close operation
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set layout manager
        setLayout(new GridLayout(0, 2, 10, 10));

        // Create labels and text fields for each customer field
        JLabel customerNumberLabel = new JLabel("Customer Number:");
        customerNumberField = new JTextField();
        add(customerNumberLabel);
        add(customerNumberField);

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField();
        add(firstNameLabel);
        add(firstNameField);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField();
        add(lastNameLabel);
        add(lastNameField);

        JLabel streetLabel = new JLabel("Street:");
        streetField = new JTextField();
        add(streetLabel);
        add(streetField);

        JLabel cityLabel = new JLabel("City:");
        cityField = new JTextField();
        add(cityLabel);
        add(cityField);

        JLabel stateLabel = new JLabel("State:");
        stateField = new JTextField();
        add(stateLabel);
        add(stateField);

        JLabel zipLabel = new JLabel("Zip:");
        zipField = new JTextField();
        add(zipLabel);
        add(zipField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField();
        add(phoneLabel);
        add(phoneField);

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        add(emailLabel);
        add(emailField);

        // Create button to add the new customer
        JButton addButton = new JButton("Add Customer");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });
        add(addButton);

        // Center the JFrame on the screen
        setLocationRelativeTo(null);

        // Make the JFrame visible
        setVisible(true);
    }

    private void addCustomer() {
        // Validate input fields
        if (!validateInput()) {
            JOptionPane.showMessageDialog(this, "Please enter valid data for all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get input values
        int customerNumber = Integer.parseInt(customerNumberField.getText());
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String street = streetField.getText();
        String city = cityField.getText();
        String state = stateField.getText();
        String zip = zipField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        try {
            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/Coffee", "root", "root");
            Statement stmt = conn.createStatement();

            // Insert new customer record into the database
            String query = "INSERT INTO Customer VALUES (" + customerNumber + ", '" + firstName + "', '" + lastName + "', '" +
                    street + "', '" + city + "', '" + state + "', '" + zip + "', '" + phone + "', '" + email + "', 200.0)";
            stmt.executeUpdate(query);

            // Close the statement and connection
            stmt.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Customer added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the add customer screen
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInput() {
        // Validate all fields
        if (customerNumberField.getText().isEmpty() || firstNameField.getText().isEmpty() ||
                lastNameField.getText().isEmpty() || streetField.getText().isEmpty() ||
                cityField.getText().isEmpty() || stateField.getText().isEmpty() ||
                zipField.getText().isEmpty() || phoneField.getText().isEmpty() ||
                emailField.getText().isEmpty()) {
            return false;
        }

        // Validate email format
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(emailField.getText());
        if (!matcher.matches()) {
            return false;
        }

        // You can add more validation if needed

        return true;
    }

    public static void main(String[] args) {
        // Create an instance of AddCustomerScreen
        new Newcustomer();
    }
}
