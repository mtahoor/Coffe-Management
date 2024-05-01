package com.mycompany.coffee;

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

public class Newcustomer extends JPanel {

    private JTextField firstNameField, lastNameField, streetField, cityField, stateField, zipField, phoneField, emailField;

    public Newcustomer() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel firstNameLabel = new JLabel("First Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(firstNameLabel, gbc);

        firstNameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(firstNameField, gbc);

        JLabel lastNameLabel = new JLabel("Last Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lastNameLabel, gbc);

        lastNameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(lastNameField, gbc);

        JLabel streetLabel = new JLabel("Street:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(streetLabel, gbc);

        streetField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(streetField, gbc);

        JLabel cityLabel = new JLabel("City:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(cityLabel, gbc);

        cityField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(cityField, gbc);

        JLabel stateLabel = new JLabel("State:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(stateLabel, gbc);

        stateField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(stateField, gbc);

        JLabel zipLabel = new JLabel("Zip:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(zipLabel, gbc);

        zipField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        add(zipField, gbc);

        JLabel phoneLabel = new JLabel("Phone:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(phoneLabel, gbc);

        phoneField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 6;
        add(phoneField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 7;
        add(emailLabel, gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 7;
        add(emailField, gbc);

        // Create button to add the new customer
        JButton addButton = new JButton("Add Customer");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 8;
        add(addButton, gbc);
    }

    private void addCustomer() {
        if (!validateInput()) {
            JOptionPane.showMessageDialog(this, "Please enter valid data for all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String street = streetField.getText();
        String city = cityField.getText();
        String state = stateField.getText();
        String zip = zipField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        try {
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/Coffee", "root", "root");
            Statement stmt = conn.createStatement();

            String query = "INSERT INTO Customer (first_name, last_name, street, city, state, zip, phone, email, credit_limit) VALUES ('" +
                    firstName + "', '" + lastName + "', '" + street + "', '" + city + "', '" + state + "', '" + zip + "', '" + phone + "', '" + email + "', 200.0)";
            stmt.executeUpdate(query);

            stmt.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Customer added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
           
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInput() {
   
        if (firstNameField.getText().isEmpty() ||
                lastNameField.getText().isEmpty() || streetField.getText().isEmpty() ||
                cityField.getText().isEmpty() || stateField.getText().isEmpty() ||
                zipField.getText().isEmpty() || phoneField.getText().isEmpty() ||
                emailField.getText().isEmpty()) {
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(emailField.getText());
        return matcher.matches();
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        streetField.setText("");
        cityField.setText("");
        stateField.setText("");
        zipField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }
}
