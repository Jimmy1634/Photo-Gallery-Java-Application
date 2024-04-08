package app.photoapplication.controller;

import app.photoapplication.Photos;
import app.photoapplication.model.Album;
import app.photoapplication.model.Photo;
import app.photoapplication.model.Tag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;

/**
 * The controller class for handling photo-related functionality.
 *
 * @author Jimmy Han, Luthfi Jamal Mohamed
 */
public class PhotoController extends LogoutController {

    @FXML
    private TextField tag1Name, tag1Value, tag2Name, tag2Value,
            optionalTagName, optionalTagValue,
            startDate, endDate, albumName;

    @FXML
    private Button createAlbum, logOut, searchTag, searchDate, back;

    @FXML
    private ListView<String> searches;

    private ObservableList<String> results = FXCollections.observableArrayList(new ArrayList<String>());
    private ArrayList<Photo> photoSearchResultsArray = new ArrayList<Photo>();

    /**
     * Initializes the controller.
     */
    public void start() {
        refresh();
    }

    /**
     * Refreshes the search results.
     */
    public void refresh() {
        results.clear();
        ArrayList<String> photoDetails = new ArrayList<String>();
        for (Photo p : photoSearchResultsArray) {
            photoDetails.add(p.getPhotoDetails());
        }
        results = FXCollections.observableArrayList(photoDetails);
        searches.setItems(results);
        searches.refresh();
    }

    public void logOut(ActionEvent e) throws IOException {
        loggingOut(e);
    }

    /**
     * Searches photos by tag.
     *
     * @param e The action event triggered by the user.
     */
    public void searchByTag(ActionEvent e) {
        String tagName1 = tag1Name.getText().trim();
        String tagValue1 = tag1Value.getText().trim();
        String tagName2 = tag2Name.getText().trim();
        String tagValue2 = tag2Value.getText().trim();
        String tagName = optionalTagName.getText().trim();
        String tagValue = optionalTagValue.getText().trim();

        tag1Name.clear();
        tag1Value.clear();
        tag2Name.clear();
        tag2Value.clear();
        optionalTagName.clear();
        optionalTagValue.clear();

        if (tagName1.isEmpty() || tagName1 == null || tagValue1.isEmpty() || tagValue1 == null) {
            showAlert("ERROR", "Missing mandatory tag, need at least one tag to search.", AlertType.ERROR);
            return;
        }
        ArrayList<Tag> searchTags = new ArrayList<Tag>();
        Tag tag1 = new Tag(tagName1, tagValue1);
        searchTags.add(tag1);
        if (tagName2.isEmpty() || tagName2 == null || tagValue2.isEmpty() || tagValue2 == null) {
            if (!tagName.isEmpty() && tagName != null && !tagValue.isEmpty() && tagValue != null) {
                Tag tag2 = new Tag(tagName, tagValue);
                searchTags.add(tag2);
            }
            photoSearchResultsArray = Photos.session.getCurrUser().orTagSearch(searchTags);
        } else {
            Tag tag2 = new Tag(tagName2, tagValue2);
            searchTags.add(tag2);
            photoSearchResultsArray = Photos.session.getCurrUser().andTagSearch(searchTags);
        }
        refresh();
    }

    /**
     * Searches photos by date range.
     *
     * @param e The action event triggered by the user.
     * @throws ParseException If date parsing fails.
     */
    public void searchByDate(ActionEvent e) throws ParseException {
        String dateBeginning = startDate.getText().trim();
        String dateEnd = endDate.getText().trim();

        if (dateBeginning.isEmpty() || dateBeginning == null || dateEnd.isEmpty() || dateEnd == null) {
            showAlert("ERROR", "Please enter mm/dd/yyyy in valid format.", AlertType.ERROR);
            return;
        }
        startDate.clear();
        endDate.clear();
        Date date1;
        Date date2;
        try {
            date1 = new SimpleDateFormat("MM/dd/yyyy").parse(dateBeginning);
            date2 = new SimpleDateFormat("MM/dd/yyyy").parse(dateEnd);
        } catch (Exception exception) {
            showAlert("ERROR", "Please enter mm/dd/yyyy in valid format.", AlertType.ERROR);
            return;
        }
        if (date2.before(date1)) {
            showAlert("ERROR", "Please enter dates in correct chronological order.", AlertType.ERROR);
            return;
        }
        photoSearchResultsArray = Photos.session.getCurrUser().getPhotosInRange(date1, date2);
        refresh();
    }

    /**
     * Saves search results as an album.
     *
     * @param e The action event triggered by the user.
     */
    public void saveResultsAsAlbum(ActionEvent e) {
        if (photoSearchResultsArray.size() == 0) {
            showAlert("ERROR", "No results match your search.", AlertType.ERROR);
            return;
        }

        String newAlbumName = albumName.getText().trim();
        albumName.clear();
        if (newAlbumName.isEmpty() || newAlbumName == null) {
            showAlert("ERROR", "Please enter an album name.", AlertType.ERROR);
            return;
        }
        if (Photos.session.getCurrUser().findAlbum(newAlbumName) != null) {
            showAlert("ERROR", "Album exists, please enter a different name.", AlertType.ERROR);
            return;
        }
        Album newAlbum = new Album(newAlbumName, photoSearchResultsArray);
        Photos.session.getCurrUser().addAlbum(newAlbum);
        results.clear();
    }

    /**
     * Navigates back to the user interface.
     *
     * @param e The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void goBack(ActionEvent e) throws IOException {
        FXMLLoader fxml = new FXMLLoader();
        fxml.setLocation(getClass().getResource("/app/photoapplication/User.fxml"));
        Parent sceneManager = (Parent) fxml.load();
        UserController user = fxml.getController();
        Scene scene = new Scene(sceneManager);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        user.start();
        stage.setScene(scene);
        stage.show();
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
