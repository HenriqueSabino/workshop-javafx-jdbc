package gui;

import db.DbException;
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
import jdk.jshell.execution.Util;
import model.entities.Department;
import model.services.DepartmentService;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {
    
    private Department entity;
    
    private DepartmentService service;
    
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
    
    public void setDepartmentService(DepartmentService service) {
        this.service = service;
    }
    
    public void setDepartment(Department entity) {
        this.entity = entity;
    }
    
    @FXML
    public void onBtSaveAction(ActionEvent event) {
        
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        
        try {
            entity = getFormData();
            service.savaOrUpdate(entity);
        } catch (DbException e) {
            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
        
        Utils.currentStage(event).close();
    }
    
    private Department getFormData() {
        Department obj = new Department();
        obj.setId(Utils.tryParseToInt(txtId.getText()));
        obj.setName(txtName.getText());
        
        return obj;
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
