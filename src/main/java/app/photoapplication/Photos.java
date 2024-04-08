package app.photoapplication;

import app.photoapplication.model.Session;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/**
 * The main class responsible for launching the Photo Application.
 *
 * @author Jimmy Han, Luthfi Jamal Mohamed
 */
public class Photos extends Application {

    public static Session session = new Session();

    /**
     * The entry point of the application.
     *
     * @param args The command-line arguments passed to the application.
     * @throws Exception If an exception occurs during application startup.
     */
    public static void main(String[] args) throws Exception {
        try {
            session = Session.deserialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        launch(args);
    }

    /**
     * Starts the JavaFX application.
     *
     * @param stage The primary stage for the application.
     * @throws Exception If an exception occurs during application startup.
     */
    @Override
    public void start(Stage stage) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/app/photoapplication/Login.fxml"));
            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.setTitle("Photo Application");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent arg0) {
                try {
                    Session.serialize(session);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
