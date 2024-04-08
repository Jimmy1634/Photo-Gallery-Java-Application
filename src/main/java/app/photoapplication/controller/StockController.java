package app.photoapplication.controller;

import app.photoapplication.Photos;
import app.photoapplication.model.Album;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The controller class for managing stock actions.
 *
 * @author Jimmy Han, Luthfi Jamal Mohamed
 */
public class StockController extends LogoutController {

    /** The stock album associated with this controller. */
    public Album stockAlbum = new Album("Stock Album");

    /**
     * Opens the stock album view.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If an input or output exception occurs.
     */
    public void openAlbum(ActionEvent event) throws IOException {
        FXMLLoader album = new FXMLLoader();
        album.setLocation(getClass().getResource("/app/photoapplication/StockAlbum.fxml"));
        Parent parent = album.load();
        StockAlbumController albumController = album.getController();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Photos.session.setCurrAlbum(Photos.session.stockUser.getSerializeStockAlbum()); //was previously Photos.session.stockUser.StockAlbum;
        albumController.start();
        stage.setScene(scene);
        stage.show();
    }
}
