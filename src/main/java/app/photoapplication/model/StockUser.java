package app.photoapplication.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Represents a user with a stock album containing photos.
 *
 * @author Jimmy Han, Luthfi Jamal Mohamed
 */
public class StockUser implements Serializable {
    /** The serial version UID for serialization. */
    public static final long serialVersionUID = 1L;

    /** The serialized stock album. */
    public Album serializeStockAlbum;

    /** The static instance of the stock album. */
    public static Album StockAlbum;

    /**
     * Constructs a StockUser with a default stock album and loads photos from a specified folder.
     */
    public StockUser() {
        serializeStockAlbum = new Album("Stock Album");
        StockAlbum = serializeStockAlbum;
        loadPhotosFromFolder("data");
    }

    /**
     * Constructs a StockUser with a specified stock album.
     *
     * @param StockAlbum The stock album to be used.
     */
    public StockUser(Album StockAlbum) {
        this.serializeStockAlbum = StockAlbum;
        this.StockAlbum = StockAlbum;
    }

    /**
     * Retrieves the serialized stock album.
     *
     * @return The serialized stock album.
     */
    public Album getSerializeStockAlbum() {
        return serializeStockAlbum;
    }

    /**
     * Loads photos from a specified folder into the stock album.
     *
     * @param folderPath The path to the folder containing the photos.
     */
    private void loadPhotosFromFolder(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        StockAlbum.addPhoto(new Photo(file));
                    } catch (IOException e) {
                        // Handle any IO exception that occurs during photo creation
                        System.err.println("Error loading photo: " + e.getMessage());
                    }
                }
            }
        }
    }
}
