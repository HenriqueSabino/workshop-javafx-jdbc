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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.entities.Seller;
import model.services.SellerService;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class SellerListController implements Initializable {
    
    private SellerService service;
    
    private ObservableList<Seller> obsList;
    
    @FXML
    private Button btNew;
    
    @FXML
    private TableView<Seller> tableViewSeller;
    
    @FXML
    private TableColumn<Seller, Integer> tableColumnId;
    
    @FXML
    private TableColumn<Seller, String> tableColumnName;
    
    @FXML
    private TableColumn<Seller, String> tableColumnEmail;
    
    @FXML
    private TableColumn<Seller, Date> tableColumnBirthDate;
    
    @FXML
    private TableColumn<Seller, Double> tableColumnBaseSalary;
    
    @FXML
    private TableColumn<Seller, Department> tableColumnDepartmentName;
    
    @FXML
    public void onBtNewAction(ActionEvent event) {
        Stage stage = Utils.currentStage(event);
        Seller seller = new Seller();
        createDialogForm(seller, "/gui/SellerForm.fxml", stage);
    }
    
    public void setSellerService(SellerService service) {
        this.service = service;
    }
    
    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was null");
        } else {
            List<Seller> list = service.findAll();
            obsList = FXCollections.observableArrayList(list);
            tableViewSeller.setItems(obsList);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }
    
    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
        initializeDepartmentNameColumn();
        
        Stage mainStage = (Stage) Main.getMainScene().getWindow();
        tableViewSeller.prefHeightProperty().bind(mainStage.heightProperty());
        
    }
    
    private void initializeDepartmentNameColumn() {
        
        tableColumnDepartmentName.setCellValueFactory(new PropertyValueFactory<>("department"));
        
        tableColumnDepartmentName.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Department department, boolean b) {
                super.updateItem(department, b);
                
                if (department == null) {
                    setText(null);
                } else {
                    setText(department.getName());
                }
            }
        });
    }
    
    private void createDialogForm(Seller seller, String absoluteName, Stage parentStage) {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();
            
            SellerFormController controller = loader.getController();
            controller.setSeller(seller);
            controller.updateFormData();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter seller data");
            
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
