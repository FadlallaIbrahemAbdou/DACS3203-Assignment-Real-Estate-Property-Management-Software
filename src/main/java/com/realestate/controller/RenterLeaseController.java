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
import realestate.model.Lease;
import realestate.model.Renter;
import realestate.model.Unit;
import realestate.service.PropertyService;
import realestate.service.RenterLeaseService;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;


public class RenterLeaseController {
    //Renter fields

    @FXML private TextField renterNameField;
    @FXML private TextField renterPhoneField;
    @FXML private TextField renterEmailField;
    @FXML private Label renterMessageLabel;
    @FXML private TableView<Renter> renterTable;
    @FXML private TableColumn<Renter, Integer> colRenterId;
    @FXML private TableColumn<Renter, String> colRenterName;
    @FXML private TableColumn<Renter, String> colRenterPhone;
    @FXML private TableColumn<Renter, String> colRenterEmail;
    //Lease fields

    @FXML private ComboBox<Renter> leaseRenterCombo;
    @FXML private ComboBox<Unit> leaseUnitCombo;
    @FXML private DatePicker leaseStartDate;
    @FXML private DatePicker leaseEndDate;
    @FXML private TextField leaseDepositField;
    @FXML private TextField leaseRentField;
    @FXML private Label leaseMessageLabel;
    @FXML private TableView<Lease> leaseTable;
    @FXML private TableColumn<Lease, Integer> colLeaseId;
    @FXML private TableColumn<Lease, String> colLeaseRenter;
    @FXML private TableColumn<Lease, String> colLeaseUnit;
    @FXML private TableColumn<Lease, Date> colLeaseStart;
    @FXML private TableColumn<Lease, Date> colLeaseEnd;
    @FXML private TableColumn<Lease, BigDecimal> colLeaseRent;
    @FXML private TableColumn<Lease, String> colLeaseStatus;

    private final RenterLeaseService renterLeaseService = new RenterLeaseService();
    private final PropertyService propertyService = new PropertyService();
    private final ObservableList<Renter> renterList = FXCollections.observableArrayList();
    private final ObservableList<Lease> leaseList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Renter table

        colRenterId.setCellValueFactory(new PropertyValueFactory<>("renterId"));
        colRenterName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colRenterPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colRenterEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        renterTable.setItems(renterList);
        // Lease table

        colLeaseId.setCellValueFactory(new PropertyValueFactory<>("leaseId"));
        colLeaseRenter.setCellValueFactory(new PropertyValueFactory<>("renterName"));
        colLeaseUnit.setCellValueFactory(new PropertyValueFactory<>("unitNumber"));
        colLeaseStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colLeaseEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colLeaseRent.setCellValueFactory(new PropertyValueFactory<>("monthlyRent"));
        colLeaseStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        leaseTable.setItems(leaseList);

        refreshRenterTable();
        refreshLeaseTable();
        refreshCombos();
        renterTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {

            if (newVal != null) {
                renterNameField.setText(newVal.getFullName());
                renterPhoneField.setText(newVal.getPhone());
                renterEmailField.setText(newVal.getEmail());
            }

        });

    }


    //Renter actions


    @FXML
    private void handleAddRenter() {
        Renter r = new Renter(
                renterNameField.getText().trim(),
                renterPhoneField.getText().trim(),
                renterEmailField.getText().trim()
        );

        String error = renterLeaseService.addRenter(r);
        renterMessageLabel.setText(error != null ? error : "Renter added.");

        if (error == null) {
            clearRenterFields();
            refreshRenterTable();
            refreshCombos();
        }

    }


    @FXML

    private void handleUpdateRenter() {
        Renter selected = renterTable.getSelectionModel().getSelectedItem();

        if (selected == null) { renterMessageLabel.setText("Select a renter."); return; }
        selected.setFullName(renterNameField.getText().trim());
        selected.setPhone(renterPhoneField.getText().trim());
        selected.setEmail(renterEmailField.getText().trim());
        String error = renterLeaseService.updateRenter(selected);
        renterMessageLabel.setText(error != null ? error : "Renter updated.");
        if (error == null) {
            clearRenterFields();
            refreshRenterTable();
            refreshCombos();
        }

    }


    //ease actions


    @FXML

    private void handleCreateLease() {
        Renter renter = leaseRenterCombo.getValue();
        Unit unit = leaseUnitCombo.getValue();
        if (renter == null || unit == null) {
            leaseMessageLabel.setText("Select a renter and a unit.");
            return;
        }

        LocalDate start = leaseStartDate.getValue();
        LocalDate end = leaseEndDate.getValue();
        if (start == null || end == null) {
            leaseMessageLabel.setText("Start and end dates are required.");
            return;
        }

        try {
            BigDecimal deposit = new BigDecimal(leaseDepositField.getText().trim().isEmpty() ? "0" : leaseDepositField.getText().trim());
            BigDecimal rent = new BigDecimal(leaseRentField.getText().trim());
            Lease lease = new Lease(
                    renter.getRenterId(),
                    unit.getUnitId(),
                    Date.valueOf(start),
                    Date.valueOf(end),
                    deposit,
                    rent,
                    "ACTIVE"

            );
            String error = renterLeaseService.createLease(lease);
            leaseMessageLabel.setText(error != null ? error : "Lease created.");

            if (error == null) {

                clearLeaseFields();
                refreshLeaseTable();
                refreshCombos();
            }

        } catch (NumberFormatException e) {
            leaseMessageLabel.setText("Deposit and rent must be valid numbers.");
        }

    }


    @FXML

    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) renterTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("REPMS - Dashboard");
        } catch (Exception e) {
            System.out.println("Error: "+ e.getMessage());

        }

    }


    private void refreshRenterTable() {
        renterList.setAll(renterLeaseService.listRenters());
    }


    private void refreshLeaseTable() {
        leaseList.setAll(renterLeaseService.listLeases());
    }


    private void refreshCombos() {
        leaseRenterCombo.setItems(FXCollections.observableArrayList(renterLeaseService.listRenters()));
        leaseUnitCombo.setItems(FXCollections.observableArrayList(propertyService.listAvailableUnits()));
    }


    private void clearRenterFields() {
        renterNameField.clear(); renterPhoneField.clear(); renterEmailField.clear();
        renterTable.getSelectionModel().clearSelection();
    }


    private void clearLeaseFields() {
        leaseRenterCombo.setValue(null); leaseUnitCombo.setValue(null);
        leaseStartDate.setValue(null); leaseEndDate.setValue(null);
        leaseDepositField.clear(); leaseRentField.clear();
    }

}