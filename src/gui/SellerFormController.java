package gui;

import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SellerFormController implements Initializable {
    
    //region TextFields
    
    @FXML
    private TextField txtId;
    
    @FXML
    private TextField txtName;
    
    @FXML
    private TextField txtEmail;
    
    @FXML
    private TextField txtBirthdate;
    
    @FXML
    private TextField txtBaseSalary;
    
    @FXML
    private TextField txtDepartmentName;
    
    //endregion
    
    //region Buttons
    @FXML
    private Button btSave;
    
    @FXML
    private Button btCancel;
    //endregion
    
    //region ErrorLabels
    @FXML
    private Label labelErrorName;
    
    @FXML
    private Label labelErrorEmail;
    
    @FXML
    private Label labelErrorBirthdate;
    
    @FXML
    private Label labelErrorBaseSalary;
    
    @FXML
    private Label labelErrorDepartmentName;
    //endregion
    
    private Seller entity;
    private SellerService sellerService;
    private DepartmentService departmentService;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
    
    public void setSeller(Seller entity) {
        this.entity = entity;
    }
    
    public void setSellerService(SellerService service) {
        this.sellerService = service;
    }
    
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }
    
    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        } else {
            txtId.setText(String.valueOf(entity.getId()));
            txtName.setText(entity.getName());
            txtEmail.setText(entity.getEmail());
            
            if (entity.getBirthDate() != null) {
                txtBirthdate.setText(sdf.format(entity.getBirthDate()));
            }
            
            txtBaseSalary.setText(String.valueOf(entity.getBaseSalary()));
            
            if (entity.getDepartment() != null) {
                txtDepartmentName.setText(entity.getDepartment().getName());
            }
        }
    }
    
    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (sellerService == null) {
            throw new IllegalStateException("Service was null");
        }
        
        try {
            entity = getFormData();
            sellerService.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        } catch (RuntimeException e) {
            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    //TODO:Refactoring
    private Seller getFormData() {
        clearErrorLabels();
        Seller seller = new Seller();
        
        try {
            ValidationException exception = new ValidationException("Validation error");
            
            seller.setId(Utils.tryParseToInt(txtId.getText()));
            
            if (txtName.getText() == null || txtName.getText().trim().equals("")) {
                exception.addErrors("Name", "Field cannot be empty");
            } else if (!txtName.getText().matches("^([A-Z][a-z]+\\s?)+$")) {
                exception.addErrors("Name", "Enter a proper name");
            } else {
                //I'm trimming the string here because of that optional space in the regex
                seller.setName(txtName.getText().trim());
            }
            
            if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
                exception.addErrors("Email", "Field cannot be empty");
            } else if (!txtEmail.getText().matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")) {
                //Regex for validating emails
                exception.addErrors("Email", "Not a valid email address");
            }
            seller.setEmail(txtEmail.getText());
            
            if (txtBirthdate.getText() == null || txtBirthdate.getText().trim().equals("")) {
                exception.addErrors("BirthDate", "Field cannot be empty");
            } else if (!txtBirthdate.getText().matches("^([0-2][0-9]|3[0-1])/(0[0-9]|1[0-2])/" +
                    "(19[0-9]{2}|20[0-1][0-9])$")) {
                
                exception.addErrors("BirthDate", "Format must be: dd/mm/yyyy");
            } else {
                seller.setBirthDate(sdf.parse(txtBirthdate.getText()));
            }
            
            if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
                exception.addErrors("BaseSalary", "Field cannot be empty");
            } else {
                
                seller.setBaseSalary(Double.parseDouble(txtBaseSalary.getText()));
            }
            
            Department department = new Department();
            
            if (txtDepartmentName.getText() == null || txtDepartmentName.getText().trim().equals("")) {
                exception.addErrors("DepName", "Field cannot be empty");
            } else {
                department = departmentService.findByName(txtDepartmentName.getText());
                
                if (department == null) {
                    setErrorMessages(exception.getErrors());
                    throw new IllegalArgumentException("There's no department with this name");
                }
            }
            seller.setDepartment(department);
            
            if (exception.getErrors().size() > 0) {
                throw exception;
            }
            
        } catch (ParseException e) {
            Alerts.showAlert("Parse exception", null, e.getMessage(), Alert.AlertType.ERROR);
        }
        
        return seller;
    }
    
    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();
        
        if (fields.contains("Name")) {
            labelErrorName.setText(errors.get("Name"));
        }
        if (fields.contains("Email")) {
            labelErrorEmail.setText(errors.get("Email"));
        }
        if (fields.contains("BirthDate")) {
            labelErrorBirthdate.setText(errors.get("BirthDate"));
        }
        if (fields.contains("BaseSalary")) {
            labelErrorBaseSalary.setText(errors.get("BaseSalary"));
        }
        if (fields.contains("DepName")) {
            labelErrorDepartmentName.setText(errors.get("DepName"));
        }
    }
    
    private void clearErrorLabels() {
        labelErrorName.setText("");
        labelErrorEmail.setText("");
        labelErrorBirthdate.setText("");
        labelErrorBaseSalary.setText("");
        labelErrorDepartmentName.setText("");
    }
    
    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }
    
    public void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
        Constraints.setTextFieldMaxLength(txtEmail, 50);
        Constraints.setTextFieldDouble(txtBaseSalary);
    }
    
    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }
    
    private void notifyDataChangeListeners() {
        dataChangeListeners.forEach(DataChangeListener::onDataChanged);
    }
}
