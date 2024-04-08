package app.photoapplication.model;

import app.photoapplication.Photos;

import java.io.*;
import java.util.*;

/**
 * Represents a user in the photo application system.
 *
 * @author Jimmy Han, Luthfi Jamal Mohamed
 */
public class User implements Serializable{
    public static final long serialVersionUID = 1L;

    private ArrayList<Album> albums;
    private String username;
    public ArrayList<String> albumNames = new ArrayList<String>();

    /**
     * Constructs a user with the specified username and an empty list of albums.
     *
     * @param username The username of the user.
     */
    public User(String username) {
        this.username = username;
        this.albums = new ArrayList<Album>();
        albums.add( Photos.session.stockUser.getSerializeStockAlbum());
    }

    /**
     * Retrieves the username of the user.
     *
     * @return The username of the user.
     */
    public String getName() {
        return this.username;
    }

    /**
     * Retrieves the list of albums belonging to the user.
     *
     * @return The list of albums.
     */
    public ArrayList<Album> getAlbums() {
        return this.albums;
    }

    /**
     * Creates a new album with the given name and adds it to the user's list of albums.
     *
     * @param albumName The name of the new album.
     */
    public void createAlbum(String albumName) {
        Album newAlbum = new Album(albumName);
        this.albums.add(newAlbum);
    }

    /**
     * Adds an existing album to the user's list of albums.
     *
     * @param a The album to be added.
     */
    public void addAlbum(Album a) {
        this.albums.add(a);
    }

    /**
     * Finds and returns an album with the given name from the user's list of albums.
     *
     * @param albumName The name of the album to find.
     * @return The album with the specified name, or null if not found.
     */
    public Album findAlbum(String albumName) {
        for (Album a : this.albums) {
            if (a.getAlbumName().equals(albumName)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Deletes the album at the specified index from the user's list of albums.
     *
     * @param index The index of the album to delete.
     */
    public void deleteAlbum(int index) {
        this.albums.remove(index);
    }

    /**
     * Retrieves the album at the specified index from the user's list of albums.
     *
     * @param index The index of the album to retrieve.
     * @return The album at the specified index.
     */
    public Album getAlbumAt(int index) {
        return this.albums.get(index);
    }

    /**
     * Retrieves a list of album names belonging to the user.
     *
     * @return The list of album names.
     */
    public ArrayList<String> getAlbumNameList() {
        this.albumNames = new ArrayList<String>();
        for (int i = 0; i < albums.size(); i++) {
            this.albumNames.add(albums.get(i).getAlbumName());
        }
        return albumNames;
    }

    /**
     * Compares this user to another object for equality based on username.
     *
     * @param u The object to compare.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object u) {
        if (u instanceof User) {
            User user = (User) u;
            return this.getName().equals(user.getName());
        } else {
            return false;
        }
    }

    /**
     * Comparator for sorting albums alphabetically by name.
     */
    static Comparator<Album> alphabetical = new Comparator<Album>() {
        @Override
        public int compare(Album arg0, Album arg1) {
            return arg0.getAlbumName().compareTo(arg1.getAlbumName());
        }
    };

    /**
     * Comparator for sorting albums by earliest photo date.
     */
    static Comparator<Album> byEarliestDate = new Comparator<Album>() {
        @Override
        public int compare(Album arg0, Album arg1) {
            Date date1 = arg0.getEarliestPhoto();
            Date date2 = arg1.getEarliestPhoto();
            if (date1 == null && date2 == null)
                return 0;
            if (date1 == null && date2 != null)
                return -1;
            if (date2 == null && date1 != null)
                return 1;
            return arg0.getEarliestPhoto().compareTo(arg1.getEarliestPhoto());
        }
    };

    /**
     * Performs an OR tag search across the user's albums.
     *
     * @param searchTags The list of tags to search for.
     * @return The list of photos containing at least one of the search tags.
     */
    public ArrayList<Photo> orTagSearch(ArrayList<Tag> searchTags) {
        ArrayList<Photo> results = new ArrayList<Photo>();
        HashSet<Photo> noDuplicates = new HashSet<Photo>();
        for (Album a : this.albums) {
            for (Photo picture : a.getPhotos()) {
                for (Tag searchTag : searchTags) {
                    for (Tag photoTag : picture.getTags()) {
                        if (photoTag.getName().equalsIgnoreCase(searchTag.getName())
                                && photoTag.getValue().equalsIgnoreCase(searchTag.getValue())) {
                            noDuplicates.add(picture);
                        }
                    }
                }
            }
        }
        results.addAll(noDuplicates);
        return results;
    }

    /**
     * Performs an AND tag search across the user's albums.
     *
     * @param searchTags The list of tags to search for.
     * @return The list of photos containing all of the search tags.
     */
    public ArrayList<Photo> andTagSearch(ArrayList<Tag> searchTags) {
        ArrayList<Photo> results = new ArrayList<Photo>();
        HashSet<Photo> noDuplicates = new HashSet<Photo>();
        boolean found = false;
        for (Album a : this.albums) {
            for (Photo picture : a.getPhotos()) {
                for (Tag searchTag : searchTags) {
                    if (!picture.hasTag(searchTag)) {
                        found = false;
                        break;
                    } else {
                        found = true;
                    }
                }
                if (found) {
                    noDuplicates.add(picture);
                }
            }
        }
        results.addAll(noDuplicates);
        return results;
    }

    /**
     * Retrieves photos within a specified date range across the user's albums.
     *
     * @param date1 The start date of the date range.
     * @param date2 The end date of the date range.
     * @return The list of photos within the specified date range.
     */
    public ArrayList<Photo> getPhotosInRange(Date date1, Date date2) {
        ArrayList<Photo> results = new ArrayList<Photo>();
        for (Album album : this.albums) {
            for (Photo picture : album.getPhotos()) {
                Date testDate = picture.getDate();
                if (testDate.compareTo(date1) >= 0 && testDate.compareTo(date2) <= 0) {
                    if (results.contains(picture)) {
                        continue;
                    } else {
                        results.add(picture);
                    }
                }
            }
        }
        return results;
    }

    /**
     * Serializes the user object to a file.
     *
     * @param user The user object to be serialized.
     * @throws IOException            If an I/O error occurs while writing to the file.
     * @throws FileNotFoundException  If the file cannot be found.
     */
    public static void serialize(User user) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"));
        oos.writeObject(user);
        oos.close();
    }

    /**
     * Deserializes the user object from a file.
     *
     * @return The deserialized user object.
     * @throws IOException            If an I/O error occurs while reading from the file.
     * @throws ClassNotFoundException If the class of the serialized object cannot be found.
     */
    public static User deserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"));
        User userList = (User) ois.readObject();
        ois.close();
        return userList;
    }
}
