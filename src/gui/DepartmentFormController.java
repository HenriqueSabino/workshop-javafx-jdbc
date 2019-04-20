package gui;

import db.DbException;
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
import model.exceptions.ValidationException;
import model.services.DepartmentService;

import java.net.URL;
import java.util.*;

public class DepartmentFormController implements Initializable {
    
    private Department entity;
    
    private DepartmentService service;
    
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
    
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
    
    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
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
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (DbException e) {
            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        } catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        }
    }
    
    private void notifyDataChangeListeners() {
        dataChangeListeners.forEach(DataChangeListener::onDataChanged);
    }
    
    private Department getFormData() {
        
        ValidationException exception = new ValidationException("Validation error");
        
        Department obj = new Department();
        obj.setId(Utils.tryParseToInt(txtId.getText()));
        
        if (txtName.getText() == null || txtName.getText().trim().equals("")) {
            exception.addErrors("Name", "Field cannot be empty");
        }
        obj.setName(txtName.getText());
        
        if (exception.getErrors().size() > 0) {
            throw exception;
        }
        
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
    
    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();
        
        if (fields.contains("Name")) {
            labelErrorName.setText(errors.get("Name"));
        }
    }
}
