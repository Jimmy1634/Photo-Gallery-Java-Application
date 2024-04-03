package application;

import java.io.*;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public String username;

    public User(String username) {
        this.username = username;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
    public static final String storeFile = "Users.dat";
    public static void writeApp(User u) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(storeFile));

        oos.writeObject(u);
    }
    public static User readApp(User u) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeFile));
        User readUser = (User)ois.readObject();
        return readUser;
    }

}
