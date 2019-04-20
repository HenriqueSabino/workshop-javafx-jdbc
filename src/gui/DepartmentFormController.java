package gui;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {
    
    private Department entity;
    
    @FXML
    private TextField txtId;
    
    @FXML
    private TextField txtName;
    
    @FXML
    private Label labelErrorName;
    
    @FXML
    private Button btSave;
    
    @FXML
    private Button btCancel;
    
    public void setDepartment(Department entity) {
        this.entity = entity;
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
    }
    
    public void updateFormData() {
        if (entity != null) {
            //the entity is not null, but its id might be
            //That's why the usage of the method String.valueOf()
            txtId.setText(String.valueOf(entity.getId()));
            txtName.setText(entity.getName());
        } else {
            throw new IllegalStateException("Entity was null");
        }
    }
}
