package app.photoapplication.controller;

import app.photoapplication.Photos;
import app.photoapplication.model.Album;
import app.photoapplication.model.Photo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * The controller class for the user to manage stock album actions.
 *
 * @author Jimmy Han, Luthfi Jamal Mohamed
 */
public class UserStockAlbumController extends LogoutController {

    @FXML
    private Button addPhoto, removePhoto, addCaption, addTag, deleteTag,
            copy_paste, movePhoto, nextPhoto, previousPhoto,
            logOut, changeCaption, search, back;

    @FXML
    private TextField tag, caption, value, album;

    @FXML
    private ListView<Photo> photos;

    @FXML
    private ImageView display;

    @FXML
    private TextArea details;

    @FXML
    private Label viewDetails;

    private ObservableList<Photo> observablePhotoList;
    private FileChooser fileChooser = new FileChooser();
    private Desktop desktop = Desktop.getDesktop();

    /**
     * Initializes the controller.
     */
    public void start() {
        refresh();
        if (observablePhotoList.size() > 0) {
            photos.getSelectionModel().select(0);
        }
    }

    /**
     * Refreshes the list of photos.
     */
    public void refresh() {
        observablePhotoList = FXCollections.observableArrayList(Photos.session.getCurrAlbum().getPhotos());
        photos.setItems(observablePhotoList);
        photos.setCellFactory(param -> new ListCell<Photo>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(Photo photo, boolean empty) {
                super.updateItem(photo, empty);
                if (empty || photo == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    File pictureFile = photo.getFile();
                    Image i = new Image(pictureFile.toURI().toString());
                    imageView.setImage(i);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    String deets = photo.getPhotoDetails();
                    setText(deets);
                    setGraphic(imageView);
                }
            }
        });
        if (observablePhotoList.size() > 0) {
            photos.getSelectionModel().select(0);
            if (Photos.session.getCurrAlbum().getDisplayPhotoIndex() == -1) {
                Photos.session.getCurrAlbum().setDisplayPhotoIndex(0);
            }
        }
    }

    /**
     * Navigates back to the user interface.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void goBack(ActionEvent event) throws IOException {
        FXMLLoader fxml = new FXMLLoader();
        fxml.setLocation(getClass().getResource("/app/photoapplication/User.fxml"));

        Parent parent = fxml.load();
        UserController user = fxml.getController();

        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        user.start();
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Copies the selected photo to another album.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void copy_paste(ActionEvent event) throws IOException {
        String album = this.album.getText().trim();
        this.album.clear();
        if (Photos.session.getCurrUser().getAlbumNameList().contains(album)) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setContentText("Are you sure you want to copy this photo to " + album + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                int index = photos.getSelectionModel().getSelectedIndex();
                Photos.session.getCurrAlbum().setPhoto(index);
                Photo photo = Photos.session.getCurrAlbum().getPhoto();
                Photos.session.getCurrUser().findAlbum(album).addPhoto(photo);
                Album.serialize(Photos.session.getCurrAlbum());
                refresh();
            }
        } else {
            showAlert("ERROR", "Album does not exist.", AlertType.ERROR);
            return;
        }
    }

    /**
     * Displays the selected photo.
     *
     * @param event The action event triggered by the user.
     */
    public void displayPhoto(ActionEvent event) {
        int index = photos.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            showAlert("ERROR", "No photos to display.", AlertType.ERROR);
            return;
        }
        Photos.session.getCurrAlbum().setDisplayPhotoIndex(index);
        Photos.session.getCurrAlbum().setPhoto(index);
        Photo photo = Photos.session.getCurrAlbum().getPhoto();
        File pictureFile = photo.getFile();
        if (photo != null) {
            Image image = new Image(pictureFile.toURI().toString());
            display.setImage(image);
            details.setText(photo.getPhotoDetails());
        }
    }

    /**
     * Displays the next photo in the slideshow.
     *
     * @param event The action event triggered by the user.
     */
    public void nextPhoto(ActionEvent event) {
        if (Photos.session.getCurrAlbum().getDisplayPhotoIndex() == -1) {
            showAlert("ERROR", "No photos to display.", AlertType.ERROR);
            return;
        }
        if (Photos.session.getCurrAlbum().getCount() == 1) {
            return;
        }
        int index = Photos.session.getCurrAlbum().getDisplayPhotoIndex();
        if (index + 1 >= Photos.session.getCurrAlbum().getCount()) {
            showAlert("ERROR", "End of slideshow.", AlertType.ERROR);
            return;
        } else {
            index++;
            Photos.session.getCurrAlbum().setDisplayPhotoIndex(index);
            Photos.session.getCurrAlbum().setPhoto(index);
            Photo photo = Photos.session.getCurrAlbum().getPhoto();
            File pictureFile = photo.getFile();
            if (photo != null) {
                Image img = new Image(pictureFile.toURI().toString());
                display.setImage(img);
                details.setText(photo.getPhotoDetails());
            }
            photos.getSelectionModel().select(index);
        }
    }

    /**
     * Displays the previous photo in the slideshow.
     *
     * @param event The action event triggered by the user.
     */
    public void previousPhoto(ActionEvent event) {
        if (Photos.session.getCurrAlbum().getDisplayPhotoIndex() == -1) {
            showAlert("ERROR", "No photos to display.", AlertType.ERROR);
            return;
        }
        if (Photos.session.getCurrAlbum().getCount() == 1) {
            return;
        }
        int index = Photos.session.getCurrAlbum().getDisplayPhotoIndex();
        if (index - 1 < 0) {
            showAlert("ERROR", "End of slideshow.", AlertType.ERROR);
            return;
        } else {
            index--;
            Photos.session.getCurrAlbum().setDisplayPhotoIndex(index);
            Photos.session.getCurrAlbum().setPhoto(index);
            Photo photo = Photos.session.getCurrAlbum().getPhoto();
            File pictureFile = photo.getFile();
            if (photo != null) {
                Image img = new Image(pictureFile.toURI().toString());
                display.setImage(img);
                details.setText(photo.getPhotoDetails());
            }
            photos.getSelectionModel().select(index);
        }
    }


    /**
     * Opens the photo search view.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void search(ActionEvent event) throws IOException {
        FXMLLoader search = new FXMLLoader();
        search.setLocation(getClass().getResource("/app/photoapplication/Photo.fxml"));
        Parent parent = search.load();
        PhotoController pc = search.getController();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        pc.start();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Logs out the current user.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void logOut(ActionEvent event) throws IOException {
        loggingOut(event);
    }

    /**
     * Custom ListCell for previewing photos.
     */
    public class Preview extends ListCell<Photo> {
        HBox box = new HBox();
        Pane pane = new Pane();
        Image image;
        ImageView view;
        TextArea details;

        public Preview() {
            super();
            box.getChildren().addAll(view, details, pane);
            box.setHgrow(pane, Priority.ALWAYS);
        }

        @Override
        public void updateItem(Photo photo, boolean empty) {
            super.updateItem(photo, empty);
            if (empty || photo == null) {
                setText(null);
                setGraphic(null);
            } else {
                File pictureFile = photo.getFile();
                image = new Image(pictureFile.toURI().toString());
                view = new ImageView(image);
                view.setFitHeight(100);
                view.setFitWidth(100);
                view.setPreserveRatio(true);
                details = new TextArea(photo.getPhotoDetails());
                setText(photo.getPhotoDetails());
                setGraphic(view);
            }
        }
    }

    /**
     * Shows an alert dialog.
     *
     * @param title     The title of the alert.
     * @param content   The content of the alert.
     * @param alertType The type of the alert.
     */
    private void showAlert(String title, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
