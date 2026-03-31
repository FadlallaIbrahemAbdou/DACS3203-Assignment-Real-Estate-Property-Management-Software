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
import realestate.model.Property;
import realestate.model.Unit;
import realestate.service.PropertyService;
import java.math.BigDecimal;

public class PropertyController {


    //Property fields
    @FXML private TextField propNameField;
    @FXML private TextField propAddressField;
    @FXML private TextField propCityField;
    @FXML private TextField propDescField;
    @FXML private TextField propSearchField;
    @FXML private Label propMessageLabel;
    @FXML private TableView<Property> propertyTable;
    @FXML private TableColumn<Property, Integer> colPropId;
    @FXML private TableColumn<Property, String> colPropName;
    @FXML private TableColumn<Property, String> colPropAddress;
    @FXML private TableColumn<Property, String> colPropCity;
    //Unit fields
    @FXML private TextField unitNumberField;
    @FXML private TextField unitRentField;
    @FXML private ComboBox<String> unitStatusCombo;
    @FXML private Label unitMessageLabel;
    @FXML private TableView<Unit> unitTable;
    @FXML private TableColumn<Unit, Integer> colUnitId;
    @FXML private TableColumn<Unit, String> colUnitNumber;
    @FXML private TableColumn<Unit, BigDecimal> colUnitRent;
    @FXML private TableColumn<Unit, String> colUnitStatus;



    private final PropertyService propertyService = new PropertyService();
    private final ObservableList<Property> propList = FXCollections.observableArrayList();
    private final ObservableList<Unit> unitList = FXCollections.observableArrayList();
    @FXML
    private void initialize() {
        colPropId.setCellValueFactory(new PropertyValueFactory<>("propertyId"));
        colPropName.setCellValueFactory(new PropertyValueFactory<>("propertyName"));
        colPropAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPropCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        propertyTable.setItems(propList);
        colUnitId.setCellValueFactory(new PropertyValueFactory<>("unitId"));
        colUnitNumber.setCellValueFactory(new PropertyValueFactory<>("unitNumber"));
        colUnitRent.setCellValueFactory(new PropertyValueFactory<>("rentAmount"));
        colUnitStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        unitTable.setItems(unitList);
        unitStatusCombo.setItems(FXCollections.observableArrayList("AVAILABLE", "OCCUPIED", "MAINTENANCE"));
        unitStatusCombo.setValue("AVAILABLE");
        refreshPropertyTable();

        propertyTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                propNameField.setText(newVal.getPropertyName());
                propAddressField.setText(newVal.getAddress());
                propCityField.setText(newVal.getCity());
                propDescField.setText(newVal.getDescription());
                refreshUnitTable(newVal.getPropertyId());
            }

        });


        unitTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {

                unitNumberField.setText(newVal.getUnitNumber());
                unitRentField.setText(newVal.getRentAmount().toPlainString());
                unitStatusCombo.setValue(newVal.getStatus());
            }

        });

    }

    @FXML
    private void handleAddProperty() {

        Property p = new Property(

                propNameField.getText().trim(),
                propAddressField.getText().trim(),
                propCityField.getText().trim(),
                propDescField.getText().trim()
        );

        String error = propertyService.addProperty(p);
        propMessageLabel.setText(error != null ? error : "Property added.");
        if (error == null) { clearPropFields(); refreshPropertyTable(); }

    }


    @FXML
    private void handleUpdateProperty() {
        Property selected = propertyTable.getSelectionModel().getSelectedItem();
        if (selected == null) { propMessageLabel.setText("Select a property."); return; }
        selected.setPropertyName(propNameField.getText().trim());
        selected.setAddress(propAddressField.getText().trim());
        selected.setCity(propCityField.getText().trim());
        selected.setDescription(propDescField.getText().trim());
        String error = propertyService.updateProperty(selected);
        propMessageLabel.setText(error != null ? error : "Property updated.");
        if (error == null) { clearPropFields(); refreshPropertyTable(); }
    }


    @FXML
    private void handleSearchProperty() {
        String keyword = propSearchField.getText().trim();
        propList.setAll(propertyService.searchProperties(keyword));
    }


    @FXML
    private void handleAddUnit() {
        Property selected = propertyTable.getSelectionModel().getSelectedItem();
        if (selected == null) { unitMessageLabel.setText("Select a property first."); return; }
        try {
            Unit u = new Unit(selected.getPropertyId(),
                    unitNumberField.getText().trim(),
                    new BigDecimal(unitRentField.getText().trim()),
                    unitStatusCombo.getValue());
            String error = propertyService.addUnit(u);
            unitMessageLabel.setText(error != null ? error : "Unit added.");
            if (error == null) { clearUnitFields(); refreshUnitTable(selected.getPropertyId()); }

        } catch (NumberFormatException e) {
            unitMessageLabel.setText("Rent amount must be a valid number.");
        }

    }


    @FXML
    private void handleUpdateUnit() {
        Unit selected = unitTable.getSelectionModel().getSelectedItem();
        if (selected == null) { unitMessageLabel.setText("Select a unit."); return; }
        try {
            selected.setUnitNumber(unitNumberField.getText().trim());
            selected.setRentAmount(new BigDecimal(unitRentField.getText().trim()));
            selected.setStatus(unitStatusCombo.getValue());
            String error = propertyService.updateUnit(selected);
            unitMessageLabel.setText(error != null ? error : "Unit updated.");
            if (error == null) { clearUnitFields(); refreshUnitTable(selected.getPropertyId()); }
        } catch (NumberFormatException e) {
            unitMessageLabel.setText("Rent amount must be a valid number.");
        }

    }


    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) propertyTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("REPMS - Dashboard");
        } catch (Exception e) {
            System.out.println("Error: "+ e.getMessage());

        }

    }


    private void refreshPropertyTable() {

        propList.setAll(propertyService.listProperties());

    }

    private void refreshUnitTable(int propertyId) {
        unitList.setAll(propertyService.listUnitsByProperty(propertyId));
    }
    private void clearPropFields() {

        propNameField.clear(); propAddressField.clear();
        propCityField.clear(); propDescField.clear();
        propertyTable.getSelectionModel().clearSelection();
    }


    private void clearUnitFields() {

        unitNumberField.clear(); unitRentField.clear();
        unitStatusCombo.setValue("AVAILABLE");
        unitTable.getSelectionModel().clearSelection();
    }

}