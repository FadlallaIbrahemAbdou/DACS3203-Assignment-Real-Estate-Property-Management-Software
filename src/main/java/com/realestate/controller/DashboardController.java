package realestate.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import realestate.service.AuthService;
import realestate.util.SessionManager;

public class DashboardController {

    @FXML private Label welcomeLabel;

    @FXML private Button staffButton;

    private final AuthService authService = new AuthService();
    @FXML
    private void initialize() {

        if (SessionManager.getCurrentUser() != null) {

            welcomeLabel.setText("Welcome, " + SessionManager.getCurrentUser().getFullName()

                    + " (" + SessionManager.getCurrentUser().getRole() + ")");

        }

        if (!SessionManager.isAdmin()) {

            staffButton.setVisible(false);
            staffButton.setManaged(false);
        }

    }


    @FXML
    private void openStaff() { navigateTo("/fxml/staff.fxml", "Staff Management"); }

    @FXML
    private void openProperties() { navigateTo("/fxml/property.fxml", "Property Management"); }


    @FXML
    private void openRenterLease() { navigateTo("/fxml/renter_lease.fxml", "Renter & Lease Management"); }

    @FXML

    private void openPayments() { navigateTo("/fxml/payment.fxml", "Payment Management"); }

    @FXML

    private void openMaintenance() { navigateTo("/fxml/maintenance.fxml", "Maintenance Management"); }

    @FXML

    private void handleLogout() {

        authService.logout();

        navigateTo("/fxml/login.fxml", "REPMS - Login");

    }

    private void navigateTo(String fxmlPath, String title) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("REPMS - " + title);
            stage.centerOnScreen();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

}