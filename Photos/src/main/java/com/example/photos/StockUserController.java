package com.example.photos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class StockUserController extends LogoutClass {

    @FXML
    private ListView<String> photoListView;

    @FXML
    public void initialize() {
        // Initialize stock user controller
        // Populate photoListView with stock photos
        photoListView.getItems().addAll("Stock Photo 1", "Stock Photo 2", "Stock Photo 3");
    }

    @FXML
    private void openPhoto() {
        // Handle opening the selected photo
        String selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        if (selectedPhoto != null) {
            // Implement photo opening functionality
            System.out.println("Opening photo: " + selectedPhoto);
        } else {
            // Display error message if no photo is selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a photo to open.");
            alert.showAndWait();
        }
    }

    @FXML
    private void switchToUserScene(ActionEvent event) {
        // Switch to the user scene
        // You can implement this method to switch scenes to view user photos
        // For example:
        // Main.switchScene("user.fxml");
        System.out.println("Switching to user scene...");
    }

}
