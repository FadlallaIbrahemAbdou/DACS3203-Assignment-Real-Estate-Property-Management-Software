package realestate.service;

import realestate.dao.LeaseDAO;
import realestate.dao.PaymentDAO;
import realestate.model.Lease;
import realestate.model.Payment;
import realestate.security.AuditLogger;
import realestate.security.ValidationUtil;

import java.util.List;

public class PaymentService {

    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final LeaseDAO leaseDAO = new LeaseDAO();

    public String recordPayment(Payment payment){
        AccessControlService.requireLogin();

        if (!ValidationUtil.isPositiveAmount(payment.getAmount())){
            return "Payment amount must be positive.";
        }
        if (payment.getPaymentDate() == null){
            return "Payment due is required";
        }if (payment.getPaymentMonth() == null){
            return "Payment month is required";
        }if (ValidationUtil.isNullOrEmpty(payment.getPaymentMethod())){
            return "Payment method is required";
        }if (!ValidationUtil.isValidStatus(payment.getStatus(),"PAID", "PARTIAL", "OVERDUE")) {
            return "Invalid payment status.";
        }
        Lease lease = leaseDAO.findById(payment.getLeaseId());
        if (lease == null){
            return "Lease does not exist";
        }

        boolean ok = paymentDAO.insert(payment);
        if (ok){
            AuditLogger.log("RECORD_PAYMENT","Payment", payment.getPaymentId(), "Record payment of " + payment.getAmount()+ " for lease ID "+ payment.getLeaseId());
            return null;
        }
        return "Failed to record payment.";
    }

    public List<Payment> listAllPayments(){
        AccessControlService.requireLogin();
        return paymentDAO.findAll();
    }

    public List<Payment> listPaymentByLease(int leaseId){
        AccessControlService.requireLogin();
        return paymentDAO.findByLease(leaseId);
    }
}
