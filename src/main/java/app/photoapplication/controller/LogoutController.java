package app.photoapplication.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import java.util.Optional;

/**
 * The controller class for handling logout functionality.
 *
 * @author Jimmy Han, Luthfi Jamal Mohamed
 */
public class LogoutController {

	/**
	 * Handles the logout action.
	 *
	 * @param event The action event triggered by the user.
	 * @throws IOException If an input or output exception occurs.
	 */
	public void loggingOut(ActionEvent event) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Log Out?");

		Optional<ButtonType> confirm = alert.showAndWait();
		if (confirm.get() == ButtonType.OK) {
			FXMLLoader logoutLoader = new FXMLLoader();
			logoutLoader.setLocation(getClass().getResource("/app/photoapplication/Login.fxml"));
			Parent parent = logoutLoader.load();
			Scene adminScene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(adminScene);
			stage.show();
		} else {
			return;
		}
	}
}