package gui;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MainViewController implements Initializable {
    
    @FXML
    private MenuItem menuItemSeller;
    @FXML
    private MenuItem menuItemDepartment;
    @FXML
    private MenuItem menuItemAbout;
    
    @FXML
    public void onMenuItemSellerAction() {
        loadView("/gui/SellerList.fxml", x -> {
        });
    }
    
    @FXML
    public void onMenuItemDepartmentAction() {
        loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
            controller.setDepartmentService(new DepartmentService());
            controller.updateTableView();
        });
    }
    
    @FXML
    public void onMenuItemAboutAction() {
        loadView("/gui/About.fxml", x -> {
        });
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }
    
    private synchronized <T> void loadView(String absoluteName, Consumer<T> consumer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox vBox = loader.load();
            
            Scene mainScene = Main.getMainScene();
            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
            
            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            
            mainVBox.getChildren().addAll(vBox.getChildren());
            
            T controller = loader.getController();
            consumer.accept(controller);
        } catch (IOException e) {
            Alerts.showAlert("IOException", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
