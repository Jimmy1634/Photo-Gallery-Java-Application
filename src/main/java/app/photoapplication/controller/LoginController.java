package app.photoapplication.controller;

import app.photoapplication.Photos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

/**
 * The controller class for handling login functionality.
 *
 * @author Jimmy Han, Luthfi Jamal Mohamed
 */
public class LoginController {

	@FXML
	private Button loginButton;

	@FXML
	private TextField nameInput;

	/**
	 * Handles the login action.
	 *
	 * @param event The action event triggered by the user.
	 * @throws IOException If an input or output exception occurs.
	 */
	public void login(ActionEvent event) throws IOException {
		String username = nameInput.getText().trim();

		if (username.isEmpty() || username == null) {
			showAlert("ERROR", "Please enter a username.", AlertType.ERROR);
		} else if (username.equals("admin")) {
			FXMLLoader adminLoader = new FXMLLoader();
			adminLoader.setLocation(getClass().getResource("/app/photoapplication/Admin.fxml"));
			Parent parent = adminLoader.load();
			AdminController admin = adminLoader.getController();

			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			admin.start();
			stage.setScene(scene);
			stage.show();
		} else if (username.equals("stock")){
			FXMLLoader stockLoader = new FXMLLoader();
			stockLoader.setLocation(getClass().getResource("/app/photoapplication/Stock.fxml"));
			Parent parent = stockLoader.load();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} else if (username.equals("stock")){
		}
		else if (Photos.session.checkUser(username)) {
			FXMLLoader userLoader = new FXMLLoader();
			userLoader.setLocation(getClass().getResource("/app/photoapplication/User.fxml"));
			Parent parent = userLoader.load();
			UserController user = userLoader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			user.start();
			stage.setScene(scene);
			stage.show();
		} else {
			showAlert("ERROR", "Username is invalid.", AlertType.ERROR);
			return;
		}
	}

	/**
	 * Shows an alert dialog.
	 *
	 * @param title     The title of the alert.
	 * @param content   The content of the alert.
	 * @param alertType The type of the alert.
	 */
	private void showAlert(String title, String content, Alert.AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
