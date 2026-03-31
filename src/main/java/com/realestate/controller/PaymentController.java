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
import realestate.model.Payment;
import realestate.service.PaymentService;
import realestate.service.RenterLeaseService;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

public class PaymentController {
    @FXML private ComboBox<Lease> leaseCombo;
    @FXML private DatePicker paymentDatePicker;
    @FXML private DatePicker paymentMonthPicker;
    @FXML private TextField amountField;
    @FXML private ComboBox<String> methodCombo;
    @FXML private ComboBox<String> statusCombo;
    @FXML private Label messageLabel;
    @FXML private TableView<Payment> paymentTable;
    @FXML private TableColumn<Payment, Integer> colPaymentId;
    @FXML private TableColumn<Payment, Integer> colLeaseId;
    @FXML private TableColumn<Payment, String> colRenter;
    @FXML private TableColumn<Payment, Date> colPaymentDate;
    @FXML private TableColumn<Payment, Date> colPaymentMonth;
    @FXML private TableColumn<Payment, BigDecimal> colAmount;
    @FXML private TableColumn<Payment, String> colMethod;
    @FXML private TableColumn<Payment, String> colStatus;
    private final PaymentService paymentService = new PaymentService();
    private final RenterLeaseService renterLeaseService = new RenterLeaseService();
    private final ObservableList<Payment> paymentList = FXCollections.observableArrayList();


    @FXML
    private void initialize() {
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colLeaseId.setCellValueFactory(new PropertyValueFactory<>("leaseId"));
        colRenter.setCellValueFactory(new PropertyValueFactory<>("renterName"));
        colPaymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colPaymentMonth.setCellValueFactory(new PropertyValueFactory<>("paymentMonth"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        paymentTable.setItems(paymentList);
        methodCombo.setItems(FXCollections.observableArrayList("CASH", "BANK_TRANSFER", "CARD"));
        statusCombo.setItems(FXCollections.observableArrayList("PAID", "PARTIAL", "OVERDUE"));
        statusCombo.setValue("PAID");
        refreshLeaseCombo();
        refreshTable();


        leaseCombo.setOnAction(e -> {
            Lease selected = leaseCombo.getValue();

            if (selected != null) {
                paymentList.setAll(paymentService.listPaymentByLease(selected.getLeaseId()));
            } else {
                refreshTable();
            }

        });

    }


    @FXML
    private void handleRecordPayment() {
        Lease lease = leaseCombo.getValue();

        if (lease == null) { messageLabel.setText("Select a lease."); return; }
        LocalDate pDate = paymentDatePicker.getValue();
        LocalDate pMonth = paymentMonthPicker.getValue();

        if (pDate == null || pMonth == null) { messageLabel.setText("Payment date and month are required."); return; }
        String method = methodCombo.getValue();

        if (method == null) { messageLabel.setText("Select a payment method."); return; }
        try {
            Payment payment = new Payment(
                    lease.getLeaseId(),
                    Date.valueOf(pDate),
                    Date.valueOf(pMonth),
                    new BigDecimal(amountField.getText().trim()),
                    method,
                    statusCombo.getValue()

            );
            String error = paymentService.recordPayment(payment);
            messageLabel.setText(error != null ? error : "Payment recorded.");
            if (error == null) {
                clearFields();
                refreshTable();

            }

        } catch (NumberFormatException e) {
            messageLabel.setText("Amount must be a valid number.");
        }

    }


    @FXML
    private void handleShowAll() {
        leaseCombo.setValue(null);
        refreshTable();
    }

    @FXML
    private void handleBack() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) paymentTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("REPMS - Dashboard");
        } catch (Exception e) {
            System.out.println("Error: "+ e.getMessage());
        }

    }


    private void refreshTable() {
        paymentList.setAll(paymentService.listAllPayments());
    }


    private void refreshLeaseCombo() {
        leaseCombo.setItems(FXCollections.observableArrayList(renterLeaseService.listActiveLeases()));
    }


    private void clearFields() {
        paymentDatePicker.setValue(null);
        paymentMonthPicker.setValue(null);
        amountField.clear();
        methodCombo.setValue(null);
        statusCombo.setValue("PAID");

    }

}