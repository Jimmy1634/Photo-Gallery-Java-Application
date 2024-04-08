package app.photoapplication.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents an administrator with the ability to manage user accounts.
 *
 *  @author Jimmy Han, Luthfi Jamal Mohamed
 */
public class Admin implements Serializable {
    private ArrayList<User> users;
    private String username;

    /**
     * Constructs an Admin object with a given list of users.
     *
     * @param users The list of users to initialize the admin with.
     */
    public Admin(ArrayList<User> users) {
        this.username = "admin";
        this.users = users;
    }

    /**
     * Constructs an Admin object with an empty list of users.
     */
    public Admin() {
        this.username = "admin";
        this.users = new ArrayList<>();
    }

    /**
     * Retrieves the list of users.
     *
     * @return The list of users.
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * Checks if a user already exists.
     *
     * @param userName The username to check.
     * @return The index of the user if exists, otherwise -1.
     */
    public int existingUser(String userName) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getName().equals(userName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Adds a new user.
     *
     * @param userName The username of the new user.
     */
    public void addUser(String userName) {
        User newUser = new User(userName);
        users.add(newUser);
    }

    /**
     * Deletes a user.
     *
     * @param userName The username of the user to delete.
     */
    public void deleteUser(String userName) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getName().equals(userName)) {
                users.remove(i);
                return;
            }
        }
    }

    /**
     * Retrieves a list of usernames.
     *
     * @return The list of usernames.
     */
    public ArrayList<String> getUserList() {
        ArrayList<String> usernames = new ArrayList<>();
        for (User u : this.users) {
            usernames.add(u.getName());
        }
        return usernames;
    }
}