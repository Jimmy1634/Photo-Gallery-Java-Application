package app.photoapplication.model;

import java.io.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a session in the photo application, including the current user, album, and session status.
 *
 * @author Jimmy Han, Luthfi Jamal Mohamed
 */
public class Session implements Serializable {
    private static final long serialVersionUID = 1L;

    public Admin admin;
    public User currentUser;
    public Album currentAlbum;
    public boolean activeSession;

    public StockUser stockUser;

    /**
     * Constructs a session with an admin object and no active user.
     */
    public Session() {
        admin = new Admin();
        stockUser = new StockUser();
        this.currentUser = null;
        this.activeSession = false;
    }

    /**
     * Retrieves the current user.
     *
     * @return The current user.
     */
    public User getCurrUser() {
        return currentUser;
    }

    /**
     * Sets the current user.
     *
     * @param user The user to set as the current user.
     */
    public void setCurrUser(User user) {
        this.currentUser = user;
    }

    /**
     * Retrieves the current album.
     *
     * @return The current album.
     */
    public Album getCurrAlbum() {
        return this.currentAlbum;
    }

    /**
     * Sets the current album.
     *
     * @param album The album to set as the current album.
     */
    public void setCurrAlbum(Album album) {
        this.currentAlbum = album;
    }

    /**
     * Checks if a user exists in the system.
     *
     * @param username The username to check.
     * @return True if the user exists, false otherwise.
     */
    public boolean checkUser(String username) {
        int res = this.admin.existingUser(username);
        if (res == -1) {
            return false;
        } else {
            this.setCurrUser(admin.getUsers().get(res));
            this.activeSession = true;
            return true;
        }
    }

    /**
     * Serializes the session object to a file.
     *
     * @param session The session object to serialize.
     * @throws IOException If an I/O error occurs.
     */
    public static void serialize(Session session) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"));
        oos.writeObject(session);
        oos.close();
    }

    /**
     * Deserializes the session object from a file.
     *
     * @return The deserialized session object.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of the serialized object cannot be found.
     */
    public static Session deserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"));
        Session session = (Session) ois.readObject();
        ois.close();
        return session;
    }
}
