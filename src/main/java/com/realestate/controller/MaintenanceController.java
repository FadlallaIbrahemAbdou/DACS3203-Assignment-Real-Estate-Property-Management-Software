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
import realestate.model.Contractor;
import realestate.model.MaintenanceRequest;
import realestate.model.Unit;
import realestate.service.MaintainceService;
import realestate.service.PropertyService;
import java.sql.Date;
import java.time.LocalDate;


public class MaintenanceController {
    // --- Contractor---
    @FXML private TextField contCompanyField;
    @FXML private TextField contContactField;
    @FXML private TextField contPhoneField;
    @FXML private TextField contServiceField;
    @FXML private Label contMessageLabel;
    @FXML private TableView<Contractor> contractorTable;
    @FXML private TableColumn<Contractor, Integer> colContId;
    @FXML private TableColumn<Contractor, String> colContCompany;
    @FXML private TableColumn<Contractor, String> colContContact;
    @FXML private TableColumn<Contractor, String> colContPhone;
    @FXML private TableColumn<Contractor, String> colContService;

    //nMaintenance Request

    @FXML private ComboBox<Unit> reqUnitCombo;
    @FXML private ComboBox<Contractor> reqContractorCombo;
    @FXML private TextField reqTitleField;
    @FXML private TextArea reqDescField;
    @FXML private ComboBox<String> reqPriorityCombo;
    @FXML private ComboBox<String> reqStatusCombo;
    @FXML private DatePicker reqDatePicker;
    @FXML private DatePicker reqScheduledPicker;
    @FXML private Label reqMessageLabel;
    @FXML private TableView<MaintenanceRequest> requestTable;
    @FXML private TableColumn<MaintenanceRequest, Integer> colReqId;
    @FXML private TableColumn<MaintenanceRequest, String> colReqUnit;
    @FXML private TableColumn<MaintenanceRequest, String> colReqTitle;
    @FXML private TableColumn<MaintenanceRequest, String> colReqPriority;
    @FXML private TableColumn<MaintenanceRequest, String> colReqStatus;
    @FXML private TableColumn<MaintenanceRequest, Date> colReqDate;
    @FXML private TableColumn<MaintenanceRequest, String> colReqContractor;

    private final MaintainceService maintainceService = new MaintainceService();
    private final PropertyService propertyService = new PropertyService();
    private final ObservableList<Contractor> contList = FXCollections.observableArrayList();
    private final ObservableList<MaintenanceRequest> reqList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        //Contractor table
        colContId.setCellValueFactory(new PropertyValueFactory<>("contractorId"));
        colContCompany.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        colContContact.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
        colContPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colContService.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
        contractorTable.setItems(contList);
        // Request table
        colReqId.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        colReqUnit.setCellValueFactory(new PropertyValueFactory<>("unitNumber"));
        colReqTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colReqPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        colReqStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colReqDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        colReqContractor.setCellValueFactory(new PropertyValueFactory<>("contractorName"));
        requestTable.setItems(reqList);
        reqPriorityCombo.setItems(FXCollections.observableArrayList("LOW", "MEDIUM", "HIGH"));
        reqPriorityCombo.setValue("MEDIUM");
        reqStatusCombo.setItems(FXCollections.observableArrayList("OPEN", "IN_PROGRESS", "COMPLETED"));
        reqStatusCombo.setValue("OPEN");

        refreshContractorTable();
        refreshRequestTable();
        refreshCombos();


        contractorTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                contCompanyField.setText(newVal.getCompanyName());
                contContactField.setText(newVal.getContactPerson());
                contPhoneField.setText(newVal.getPhone());
                contServiceField.setText(newVal.getServiceType());
            }

        });

        requestTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                reqTitleField.setText(newVal.getTitle());
                reqDescField.setText(newVal.getDescription());
                reqPriorityCombo.setValue(newVal.getPriority());
                reqStatusCombo.setValue(newVal.getStatus());
            }

        });

    }


    //Contractor actions


    @FXML
    private void handleAddContractor() {
        Contractor c = new Contractor(
                contCompanyField.getText().trim(),
                contContactField.getText().trim(),
                contPhoneField.getText().trim(),
                contServiceField.getText().trim()
        );
        String error = maintainceService.addContractor(c);
        contMessageLabel.setText(error != null ? error : "Contractor added.");
        if (error == null) { clearContFields(); refreshContractorTable(); refreshCombos(); }

    }


    @FXML
    private void handleUpdateContractor() {
        Contractor selected = contractorTable.getSelectionModel().getSelectedItem();

        if (selected == null) { contMessageLabel.setText("Select a contractor."); return; }
        selected.setCompanyName(contCompanyField.getText().trim());
        selected.setContactPerson(contContactField.getText().trim());
        selected.setPhone(contPhoneField.getText().trim());
        selected.setServiceType(contServiceField.getText().trim());
        String error = maintainceService.updateContractor(selected);
        contMessageLabel.setText(error != null ? error : "Contractor updated.");
        if (error == null) { clearContFields(); refreshContractorTable(); refreshCombos(); }
    }


    //Request actions

    @FXML
    private void handleCreateRequest() {
        Unit unit = reqUnitCombo.getValue();

        if (unit == null) { reqMessageLabel.setText("Select a unit."); return; }

        LocalDate reqDate = reqDatePicker.getValue();

        if (reqDate == null) { reqMessageLabel.setText("Request date is required."); return; }
        Contractor cont = reqContractorCombo.getValue();
        LocalDate sched = reqScheduledPicker.getValue();

        MaintenanceRequest req = new MaintenanceRequest(
                unit.getUnitId(),
                cont != null ? cont.getContractorId() : null,
                reqTitleField.getText().trim(),
                reqDescField.getText().trim(),
                reqPriorityCombo.getValue(),
                "OPEN",
                Date.valueOf(reqDate),
                sched != null ? Date.valueOf(sched) : null

        );
        String error = maintainceService.createRequest(req);
        reqMessageLabel.setText(error != null ? error : "Request created.");
        if (error == null) { clearReqFields(); refreshRequestTable(); }
    }


    @FXML
    private void handleUpdateStatus() {
        MaintenanceRequest selected = requestTable.getSelectionModel().getSelectedItem();
        if (selected == null) { reqMessageLabel.setText("Select a request."); return; }
        String error = maintainceService.updateRequestStatus(selected.getRequestId(), reqStatusCombo.getValue());
        reqMessageLabel.setText(error != null ? error : "Status updated.");
        if (error == null) refreshRequestTable();
    }


    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) contractorTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("REPMS - Dashboard");
        } catch (Exception e) {
            System.out.println("Error: "+ e.getMessage());
        }

    }

    private void refreshContractorTable() { contList.setAll(maintainceService.listContractors()); }
    private void refreshRequestTable() { reqList.setAll(maintainceService.listRequests()); }
    private void refreshCombos() {
        reqUnitCombo.setItems(FXCollections.observableArrayList(propertyService.listAllUnits()));
        reqContractorCombo.setItems(FXCollections.observableArrayList(maintainceService.listContractors()));
    }


    private void clearContFields() {
        contCompanyField.clear(); contContactField.clear();
        contPhoneField.clear(); contServiceField.clear();
        contractorTable.getSelectionModel().clearSelection();

    }

    private void clearReqFields() {
        reqTitleField.clear(); reqDescField.clear();
        reqPriorityCombo.setValue("MEDIUM"); reqStatusCombo.setValue("OPEN");
        reqDatePicker.setValue(null); reqScheduledPicker.setValue(null);
        reqUnitCombo.setValue(null); reqContractorCombo.setValue(null);
        requestTable.getSelectionModel().clearSelection();
    }

}