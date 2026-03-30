package realestate.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import realestate.model.User;
import realestate.service.StaffService;


public class StaffController {

    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    @FXML private TableView<User> staffTable;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colFullName;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colStatus;

    private final StaffService staffService = new StaffService();
    private final ObservableList<User> staffList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        staffTable.setItems(staffList);
        refreshTable();


        staffTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {

            if (newVal != null) {
                fullNameField.setText(newVal.getFullName());
                usernameField.setText(newVal.getUsername());
                emailField.setText(newVal.getEmail());
                passwordField.clear();
            }

        });

    }


    @FXML
    private void handleAdd() {
        String error = staffService.createStaff(
                fullNameField.getText().trim(),
                usernameField.getText().trim(),
                emailField.getText().trim(),
                passwordField.getText()
        );

        if (error != null) {
            messageLabel.setText(error);
        } else {

            messageLabel.setText("Staff created successfully.");
            clearFields();
            refreshTable();
        }

    }


    @FXML
    private void handleUpdate() {
        User selected = staffTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            messageLabel.setText("Select a staff member to update.");
            return;
        }

        selected.setFullName(fullNameField.getText().trim());
        selected.setEmail(emailField.getText().trim());
        String error = staffService.updateStaff(selected, passwordField.getText());
        if (error != null) {
            messageLabel.setText(error);
        } else {
            messageLabel.setText("Staff updated successfully.");
            clearFields();
            refreshTable();
        }

    }


    @FXML
    private void handleDeactivate() {
        User selected = staffTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Select a staff member to deactivate.");
            return;
        }
        String error = staffService.deactivateStaff(selected.getUserId());
        if (error != null) {
            messageLabel.setText(error);
        } else {
            messageLabel.setText("Staff deactivated successfully.");
            clearFields();
            refreshTable();

        }

    }

    @FXML

    private void handleBack() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) staffTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("REPMS - Dashboard");

        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        }

    }

    private void refreshTable() {
        try {
            staffList.setAll(staffService.listAllStaff());
        } catch (SecurityException e) {

            messageLabel.setText(e.getMessage());
        }
    }


    private void clearFields() {
        fullNameField.clear();
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        staffTable.getSelectionModel().clearSelection();

    }

}