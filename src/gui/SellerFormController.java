package gui;

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
import java.util.ResourceBundle;

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
            
            Utils.currentStage(event).close();
        } catch (RuntimeException e) {
            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private Seller getFormData() {
        Seller seller = new Seller();
        
        try {
            seller.setId(Utils.tryParseToInt(txtId.getText()));
            seller.setName(txtName.getText());
            seller.setEmail(txtEmail.getText());
            seller.setBirthDate(sdf.parse(txtBirthdate.getText()));
            seller.setBaseSalary(Double.parseDouble(txtBaseSalary.getText()));
            
            Department department = departmentService.findByName(txtDepartmentName.getText());
            if (department == null)
                throw new IllegalArgumentException("There's no department with this name");
            seller.setDepartment(department);
            
        } catch (ParseException e) {
            throw new ValidationException("Could not read the birthdate field");
        }
        
        return seller;
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
}
