package com.example.photos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PhotosApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        AdminController.nonAdminUsers = SerializeUsers.DeserializeAllUsers();
        Parent root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
        Scene scene = new Scene(root, 1024, 768);
        stage.setTitle("Photo Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
