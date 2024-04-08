package app.photoapplication.controller;

import app.photoapplication.Photos;
import app.photoapplication.model.Album;
import app.photoapplication.model.Photo;
import app.photoapplication.model.Session;
import app.photoapplication.model.Tag;
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
 * The controller class for the stock user to manage stock album actions.
 *
 * @author Jimmy Han, Luthfi Jamal Mohamed
 */
public class StockAlbumController extends LogoutController{


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
                    javafx.scene.image.Image i = new javafx.scene.image.Image(pictureFile.toURI().toString());
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
     * Adds a photo to the album.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void addPhoto(ActionEvent event) throws IOException {
        fileChooser.setTitle("Select Picture");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        Photo newPhoto = new Photo(file);
        if (!Photos.session.getCurrAlbum().addPhoto(newPhoto)) {
            showAlert("ERROR", "Photo already exists.", Alert.AlertType.ERROR);
            return;
        } else {
            Album.serialize(Photos.session.getCurrAlbum());
            refresh();
            photos.getSelectionModel().select(newPhoto);
        }
    }


    /**
     * Removes the selected photo from the album.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void removePhoto(ActionEvent event) throws IOException {
        int index = photos.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            Photos.session.getCurrAlbum().deletePhoto(index);
            int i = Photos.session.getCurrAlbum().getPhotos().size();
            if (Photos.session.getCurrAlbum().getPhotos().size() == 1) {
                photos.getSelectionModel().select(0);
            } else if (index == i) {
                photos.getSelectionModel().select(i - 1);
            } else {
                photos.getSelectionModel().select(index);
            }
            if (index == Photos.session.getCurrAlbum().getDisplayPhotoIndex()) {
                display.setImage(null);
                details.clear();
            }
            Album.serialize(Photos.session.getCurrAlbum());
            refresh();
        }
    }

    /**
     * Adds a caption to the selected photo.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void addCaption(ActionEvent event) throws IOException {
        int index = photos.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showAlert("ERROR", "No photo selected.", Alert.AlertType.ERROR);
            return;
        }
        String newCaption = caption.getText().trim();
        caption.clear();
        if (newCaption.isEmpty() || newCaption == null) {
            showAlert("ERROR", "Please enter a caption.", Alert.AlertType.ERROR);
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setHeaderText("Confirm that you would like to change the caption to " + newCaption);
        Optional<ButtonType> buttonClicked = alert.showAndWait();
        if (buttonClicked.get() == ButtonType.OK) {
            alert.close();
            Photos.session.getCurrAlbum().setPhoto(index);
            Photos.session.getCurrAlbum().getPhoto().editCaption(newCaption);
            refresh();
            Photo.serialize(Photos.session.getCurrAlbum().getPhoto());
            photos.getSelectionModel().select(index);
            if (display != null)
                details.setText(Photos.session.getCurrAlbum().getPhoto().getPhotoDetails());
        } else {
            alert.close();
            return;
        }
    }

    /**
     * Adds a tag to the selected photo.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void addTag(ActionEvent event) throws IOException {
        int index = photos.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showAlert("ERROR", "No photo selected.", Alert.AlertType.ERROR);
            return;
        }
        String name = tag.getText().trim();
        String value = this.value.getText().trim();
        tag.clear();
        this.value.clear();
        if (name.isEmpty() || value.isEmpty()) {
            showAlert("ERROR", "Please fill in both the name and value fields.", Alert.AlertType.ERROR);
            return;
        }
        Tag tag = new Tag(name, value);
        Photos.session.getCurrAlbum().setPhoto(index);
        if (Photos.session.getCurrAlbum().getPhoto().hasTag(tag)) {
            showAlert("ERROR", "Tag already exists.", Alert.AlertType.ERROR);
            return;
        } else {
            Photos.session.getCurrAlbum().getPhoto().addTag(name, value);
            if (display != null)
                details.setText(Photos.session.getCurrAlbum().getPhoto().getPhotoDetails());
        }
        Session.serialize(Photos.session);
        refresh();
    }

    /**
     * Deletes a tag from the selected photo.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void deleteTag(ActionEvent event) throws IOException {
        int index = photos.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showAlert("ERROR", "No photo selected.", Alert.AlertType.ERROR);
            return;
        }
        String name = tag.getText().trim();
        String value = this.value.getText().trim();
        tag.clear();
        this.value.clear();
        if (name.isEmpty() || value.isEmpty()) {
            showAlert("ERROR", "Please fill in both the name and value fields.", Alert.AlertType.ERROR);
            return;
        }
        Tag tag = new Tag(name, value);
        Photos.session.getCurrAlbum().setPhoto(index);
        if (Photos.session.getCurrAlbum().getPhoto().hasTag(tag)) {
            Photos.session.getCurrAlbum().getPhoto().deleteTag(name, value);
            if (display != null)
                details.setText(Photos.session.getCurrAlbum().getPhoto().getPhotoDetails());
        } else {
            showAlert("ERROR", "Tag does not exist.", Alert.AlertType.ERROR);
            return;
        }
        Session.serialize(Photos.session);
        refresh();
    }



    /**
     * Displays the selected photo.
     *
     * @param event The action event triggered by the user.
     */
    public void displayPhoto(ActionEvent event) {
        int index = photos.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            showAlert("ERROR", "No photos to display.", Alert.AlertType.ERROR);
            return;
        }
        Photos.session.getCurrAlbum().setDisplayPhotoIndex(index);
        Photos.session.getCurrAlbum().setPhoto(index);
        Photo photo = Photos.session.getCurrAlbum().getPhoto();
        File pictureFile = photo.getFile();
        if (photo != null) {
            javafx.scene.image.Image image = new javafx.scene.image.Image(pictureFile.toURI().toString());
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
            showAlert("ERROR", "No photos to display.", Alert.AlertType.ERROR);
            return;
        }
        if (Photos.session.getCurrAlbum().getCount() == 1) {
            return;
        }
        int index = Photos.session.getCurrAlbum().getDisplayPhotoIndex();
        if (index + 1 >= Photos.session.getCurrAlbum().getCount()) {
            showAlert("ERROR", "End of slideshow.", Alert.AlertType.ERROR);
            return;
        } else {
            index++;
            Photos.session.getCurrAlbum().setDisplayPhotoIndex(index);
            Photos.session.getCurrAlbum().setPhoto(index);
            Photo photo = Photos.session.getCurrAlbum().getPhoto();
            File pictureFile = photo.getFile();
            if (photo != null) {
                javafx.scene.image.Image img = new javafx.scene.image.Image(pictureFile.toURI().toString());
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
            showAlert("ERROR", "No photos to display.", Alert.AlertType.ERROR);
            return;
        }
        if (Photos.session.getCurrAlbum().getCount() == 1) {
            return;
        }
        int index = Photos.session.getCurrAlbum().getDisplayPhotoIndex();
        if (index - 1 < 0) {
            showAlert("ERROR", "End of slideshow.", Alert.AlertType.ERROR);
            return;
        } else {
            index--;
            Photos.session.getCurrAlbum().setDisplayPhotoIndex(index);
            Photos.session.getCurrAlbum().setPhoto(index);
            Photo photo = Photos.session.getCurrAlbum().getPhoto();
            File pictureFile = photo.getFile();
            if (photo != null) {
                javafx.scene.image.Image img = new javafx.scene.image.Image(pictureFile.toURI().toString());
                display.setImage(img);
                details.setText(photo.getPhotoDetails());
            }
            photos.getSelectionModel().select(index);
        }
    }

    /**
     * Changes the caption of the selected photo.
     *
     * @param event The action event triggered by the user.
     */
    public void changeCaption(ActionEvent event) {
        int index = photos.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showAlert("ERROR", "No photo is selected", Alert.AlertType.ERROR);
            return;
        }
        String newCaption = caption.getText().trim();
        caption.clear();
        if (newCaption.isEmpty() || newCaption == null) {
            showAlert("ERROR", "Please enter a caption.", Alert.AlertType.ERROR);
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setHeaderText("Confirm that you would like to change the caption to " + newCaption);
        Optional<ButtonType> buttonClicked = alert.showAndWait();
        if (buttonClicked.get() == ButtonType.OK) {
            alert.close();
            Photos.session.getCurrAlbum().setPhoto(index);
            Photos.session.getCurrAlbum().getPhoto().editCaption(newCaption);
            if (display != null)
                details.setText(Photos.session.getCurrAlbum().getPhoto().getPhotoDetails());
            refresh();
        } else {
            alert.close();
            return;
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
        javafx.scene.image.Image image;
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
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
