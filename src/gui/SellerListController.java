package gui;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.entities.Seller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class SellerListController implements Initializable {
    
    @FXML
    Button btNew;
    
    @FXML
    TableView<Seller> tableViewSeller;
    
    @FXML
    TableColumn<Seller, Integer> tableColumnId;
    
    @FXML
    TableColumn<Seller, String> tableColumnName;
    
    @FXML
    TableColumn<Seller, String> tableColumnEmail;
    
    @FXML
    TableColumn<Seller, Date> tableColumnBirthDate;
    
    @FXML
    TableColumn<Seller, Double> tableColumnBaseSalary;
    
    @FXML
    TableColumn<Seller, Department> tableColumnDepartmentName;
    
    @FXML
    public void onBtNewAction() {
        System.out.println("onBtNewAction");
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
}
