package gui;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {
    
    private DepartmentService departmentService;
    
    @FXML
    private TableView<Department> tableViewDepartment;
    
    @FXML
    private TableColumn<Department, Integer> tableColumnId;
    
    @FXML
    private TableColumn<Department, String> tableColumnName;
    
    @FXML
    private Button btNew;
    
    private ObservableList<Department> obsList;
    
    public void onBtNewAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Department department = new Department();
        createDialogForm(department, "/gui/DepartmentForm.fxml", parentStage);
    }
    
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }
    
    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
    }
    
    public void updateTableView() {
        if (departmentService == null) {
            throw new IllegalStateException("Service was null");
        } else {
            List<Department> list = departmentService.findAll();
            obsList = FXCollections.observableArrayList(list);
            tableViewDepartment.setItems(obsList);
        }
    }
    
    private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();
            
            DepartmentFormController controller = loader.getController();
            controller.setDepartment(obj);
            controller.updateFormData();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter department data");
            
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            Alerts.showAlert("IOException", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
