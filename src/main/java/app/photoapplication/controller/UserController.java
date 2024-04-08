package app.photoapplication.controller;

import app.photoapplication.Photos;
import app.photoapplication.model.Album;
import app.photoapplication.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javafx.collections.*;
import javafx.fxml.FXMLLoader;

/**
 * The controller class for managing user actions.
 *
 * @author Jimmy Han, Luthfi Jamal Mohamed
 */
public class UserController extends LogoutController {

    @FXML
    private Button createAlbum, deleteAlbum, renameAlbum, openAlbum, logOut, search;

    @FXML
    private TextField EnteredAlbumName;

    @FXML
    private ListView<String> albumsList;

    @FXML
    private Label user;

    private ObservableList<String> list;
    private static ArrayList<String> albums = new ArrayList<>();

    /**
     * Initializes the controller.
     */
    public void start() {
        refresh();
        if (list.size() > 0) {
            albumsList.getSelectionModel().select(0);
        }
        user.setText(Photos.session.getCurrUser().getName() + "'s Albums");
    }

    /**
     * Performs search action.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void search(ActionEvent event) throws IOException {
        FXMLLoader photo = new FXMLLoader();
        photo.setLocation(getClass().getResource("/app/photoapplication/Photo.fxml"));
        Parent root = photo.load();
        PhotoController searchController = photo.getController();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        searchController.start();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates a new album.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void createAlbum(ActionEvent event) throws IOException {
        String name = EnteredAlbumName.getText().trim();
        if (name.isEmpty() || name == null) {
            showAlert("ERROR", "Please enter an album name.", AlertType.ERROR);
            return;
        } else if (Photos.session.getCurrUser().findAlbum(name) != null) {
            EnteredAlbumName.clear();
            showAlert("ERROR", "Album already exists.", AlertType.ERROR);
            return;
        } else {
            Photos.session.getCurrUser().createAlbum(name);
            EnteredAlbumName.clear();
            User.serialize(Photos.session.getCurrUser());
            refresh();
        }
    }

    /**
     * Deletes an album.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void deleteAlbum(ActionEvent event) throws IOException {
        int index = albumsList.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showAlert("ERROR", "No albums to delete.", AlertType.ERROR);
            return;
        }
        Alert a = new Alert(AlertType.CONFIRMATION);
        a.setTitle("CONFIRMATION");
        a.setHeaderText(null);
        a.setContentText("Confirm you want to delete " + Photos.session.getCurrUser().getAlbumAt(index).getAlbumName());
        Optional<ButtonType> r = a.showAndWait();
        if (r.get() == ButtonType.OK) {
            Photos.session.getCurrUser().deleteAlbum(index);
            User.serialize(Photos.session.getCurrUser());
            refresh();
            if (Photos.session.getCurrUser().getAlbums().size() == 0) {
            } else {
                int i = Photos.session.getCurrUser().getAlbums().size();
                if (Photos.session.getCurrUser().getAlbums().size() == 1) {
                    albumsList.getSelectionModel().select(0);
                } else if (index == i) {
                    albumsList.getSelectionModel().select(i - 1);
                } else {
                    albumsList.getSelectionModel().select(index);
                }
            }
        } else {
            return;
        }
        return;
    }

    /**
     * Renames an album.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void renameAlbum(ActionEvent event) throws IOException {
        int index = albumsList.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showAlert("ERROR", "No albums to rename.", AlertType.ERROR);

            return;
        }
        String name = EnteredAlbumName.getText().trim();
        if (name.isEmpty() || name == null) {
            showAlert("ERROR", "Please enter new name to rename album.", AlertType.ERROR);
            return;
        }
        EnteredAlbumName.clear();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setHeaderText("Confirm that you would like to change "
                + Photos.session.getCurrUser().getAlbums().get(index).getAlbumName() + " to "
                + name);

        Optional<ButtonType> buttonClicked = alert.showAndWait();
        if (buttonClicked.get() == ButtonType.OK) {
            alert.close();
            Photos.session.getCurrUser().getAlbums().get(index).setAlbumName(name);

            refresh();
            User.serialize(Photos.session.getCurrUser());
            albumsList.getSelectionModel().select(index);
        } else {
            alert.close();
            return;
        }

    }

    /**
     * Opens the selected album.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void openAlbum(ActionEvent event) throws IOException {
        int index = albumsList.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showAlert("ERROR", "No albums to open.", AlertType.ERROR);
            return;
        }

        if (Photos.session.getCurrUser().getAlbums().get(index).getAlbumName().equals("Stock Album")){
            FXMLLoader album = new FXMLLoader();
            album.setLocation(getClass().getResource("/app/photoapplication/UserStockAlbum.fxml"));
            Parent parent = album.load();
            UserStockAlbumController albumController = album.getController();
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Photos.session.setCurrAlbum(Photos.session.getCurrUser().getAlbums().get(index));

            albumController.start();
            stage.setScene(scene);
            stage.show();
        }
        else {
            FXMLLoader album = new FXMLLoader();
            album.setLocation(getClass().getResource("/app/photoapplication/Album.fxml"));
            Parent parent = album.load();
            AlbumController albumController = album.getController();
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Photos.session.setCurrAlbum(Photos.session.getCurrUser().getAlbums().get(index));

            albumController.start();
            stage.setScene(scene);
            stage.show();
        }
    }

    public void logOut(ActionEvent event) throws IOException {
        loggingOut(event);
    }

    /**
     * Refreshes the list of albums.
     */
    public void refresh() {
        albums.clear();
        for (Album a : Photos.session.getCurrUser().getAlbums()) {
            albums.add(a.getAlbumDetails());
        }
        list = FXCollections.observableArrayList(albums);
        albumsList.setItems(list);
        albumsList.refresh();
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
