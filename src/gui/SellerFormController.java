package gui;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;

import java.net.URL;
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
    
    public void setSeller(Seller entity) {
        this.entity = entity;
    }
    
    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        } else {
            txtId.setText(String.valueOf(entity.getId()));
            txtName.setText(entity.getName());
            txtEmail.setText(entity.getEmail());
            
            if (entity.getBirthDate() != null) {
                txtBirthdate.setText(String.valueOf(entity.getBirthDate()));
            }
            
            txtBaseSalary.setText(String.valueOf(entity.getBaseSalary()));
            
            if (entity.getDepartment() != null) {
                txtDepartmentName.setText(entity.getDepartment().getName());
            }
        }
    }
    
    @FXML
    public void onBtSaveAction() {
        System.out.println("onBtSaveAction");
    }
    
    @FXML
    public void onBtCancelAction() {
        System.out.println("onBtCancelAction");
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
