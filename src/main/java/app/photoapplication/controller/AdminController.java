package app.photoapplication.controller;

import app.photoapplication.Photos;
import app.photoapplication.model.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The controller class for managing administrator actions.
 *
 * @author Jimmy Han, Luthfi Jamal Mohamed
 */
public class AdminController extends LogoutController {

    @FXML
    ListView<String> userList;
    @FXML
    Button addUser, deleteUser, logOff;
    @FXML
    TextField user;

    ObservableList<String> list;

    /**
     * Initializes the controller.
     */
    public void start() {
        refresh();
        if (!list.isEmpty()) {
            userList.getSelectionModel().select(0);
        }
    }

    /**
     * Creates a new user.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void createUser(ActionEvent event) throws IOException {
        String userToBeCreated = user.getText().trim();
        if (userToBeCreated == null || userToBeCreated.isEmpty()) {
            showAlert("ERROR", "User field is empty.", AlertType.ERROR);
            return;
        } else if (userToBeCreated.equals("admin")) {
            showAlert("ERROR", "Username 'admin' is taken.", AlertType.ERROR);
            return;
        } else if (userToBeCreated.equals("stock")){
            showAlert("ERROR", "Username 'stock' is taken.", AlertType.ERROR);
            return;
        } else if (Photos.session.admin.existingUser(userToBeCreated) != -1) {
            showAlert("ERROR", "Username already exists.", AlertType.ERROR);
            return;
        } else {
            Photos.session.admin.addUser(userToBeCreated);
            Session.serialize(Photos.session);
            refresh();
            userList.getSelectionModel().select(userToBeCreated);
            user.clear();
        }
    }

    /**
     * Deletes a user.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void deleteUser(ActionEvent event) throws IOException {
        int delete = userList.getSelectionModel().getSelectedIndex();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setContentText("Confirm you want to delete " + userList.getSelectionModel().getSelectedItem() + ".");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            String user = userList.getSelectionModel().getSelectedItem();
            Photos.session.admin.deleteUser(user);
            refresh();
            Session.serialize(Photos.session);
            int i = Photos.session.admin.getUsers().size();
            if (Photos.session.admin.getUsers().size() == 1) {
                userList.getSelectionModel().select(0);
            } else if (delete == i) {
                userList.getSelectionModel().select(i - 1);
            } else {
                userList.getSelectionModel().select(delete);
            }
        } else {
            return;
        }
    }

    /**
     * Refreshes the user list.
     */
    public void refresh() {
        userList.refresh();
        list = FXCollections.observableArrayList(Photos.session.admin.getUserList());
        userList.setItems(list);
        userList.refresh();
    }

    /**
     * Logs out the admin user.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void logOut(ActionEvent event) throws IOException {
        loggingOut(event);
    }

    /**
     * Displays an alert dialog.
     *
     * @param title     The title of the alert dialog.
     * @param content   The content of the alert dialog.
     * @param alertType The type of the alert dialog.
     */
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
