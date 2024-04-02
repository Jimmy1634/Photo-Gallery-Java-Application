package com.example.photos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginController {

    @FXML
    TextField nameTextField;
    @FXML
    Text ErrorMessage;

    private Stage stage;
    private Scene scene;
    private Parent root;


    public void login(ActionEvent event) throws IOException {
        String username = nameTextField.getText();
        if (username == null || username == ""){
            ErrorMessage.setOpacity(1.0);
        }
        if (username.equals("admin")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin.fxml"));
            root = loader.load();

            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else if (username.equals("stock")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Stock.fxml")); // Update with the appropriate FXML file
            root = loader.load();

            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();        }
        else {
            for (User i : AdminController.nonAdminUsers){
                if (username.equals(i.username)){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Welcome.fxml")); //PROPERLY UPDATE THIS
                    root = loader.load();

                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    return;
                }
            }
            ErrorMessage.setOpacity(1.0);
        }
    }
}
