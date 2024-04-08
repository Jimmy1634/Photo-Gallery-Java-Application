package app.photoapplication.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents a photo with associated details such as date, caption, and tags.
 *
 * @author Jimmy Han, Luthfi Jamal Mohamed
 */
public class Photo implements Serializable {
    public static final long serialVersionUID = 1L;

    private Date date;
    private String caption;
    private String filePath;
    private String name;
    private File picture;
    private ArrayList<Tag> tags;

    /**
     * Constructs a photo object from a file.
     *
     * @param picture The file representing the photo.
     * @throws IOException If an I/O error occurs.
     */
    public Photo(File picture) throws IOException {
        if (picture != null) {
            this.filePath = picture.getAbsolutePath();
            this.name = picture.getName();
            this.picture = picture;
            this.tags = new ArrayList<>();
            this.caption = "";

            FileTime fT = Files.getLastModifiedTime(Paths.get(filePath));
            this.date = new Date(fT.toMillis());
        }
    }

    /**
     * Retrieves the name of the photo.
     *
     * @return The name of the photo.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the date the photo was taken.
     *
     * @return The date the photo was taken.
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Retrieves the tags associated with the photo.
     *
     * @return The list of tags associated with the photo.
     */
    public ArrayList<Tag> getTags() {
        return this.tags;
    }

    /**
     * Retrieves the file representing the photo.
     *
     * @return The file representing the photo.
     */
    public File getFile() {
        return this.picture;
    }

    /**
     * Edits the caption of the photo.
     *
     * @param newCaption The new caption to set for the photo.
     */
    public void editCaption(String newCaption) {
        this.caption = newCaption;
    }

    /**
     * Adds a tag to the photo.
     *
     * @param tagName  The name of the tag.
     * @param tagValue The value of the tag.
     */
    public void addTag(String tagName, String tagValue) {
        tags.add(new Tag(tagName, tagValue));
    }

    /**
     * Deletes a tag from the photo.
     *
     * @param name     The name of the tag to delete.
     * @param tagValue The value of the tag to delete.
     */
    public void deleteTag(String name, String tagValue) {
        for (int i = 0; i < tags.size(); i++) {
            Tag x = tags.get(i);
            if (x.getName().equalsIgnoreCase(name)) {
                if (x.getValue().equalsIgnoreCase(tagValue)) {
                    tags.remove(i);
                }
            }
        }
    }

    /**
     * Checks if the photo has a specific tag.
     *
     * @param tag The tag to check.
     * @return True if the photo has the tag, false otherwise.
     */
    public boolean hasTag(Tag tag) {
        for (Tag x : this.tags) {
            if (x.getName().equalsIgnoreCase(tag.getName())
                    && x.getValue().equalsIgnoreCase(tag.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves details of the photo including name, caption, date, and tags.
     *
     * @return Details of the photo as a formatted string.
     */
    public String getPhotoDetails() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        StringBuilder detailsBuilder = new StringBuilder();

        detailsBuilder.append("Name: ").append(this.name).append("\n");
        detailsBuilder.append("Caption: ").append(this.caption).append("\n");
        detailsBuilder.append("Date: ").append(formatter.format(this.date)).append("\n");
        detailsBuilder.append("Tags:\n");

        for (Tag t : this.tags) {
            detailsBuilder.append("Name: ").append(t.getName()).append(" | Value: ").append(t.getValue()).append("\n");
        }

        return detailsBuilder.toString();
    }

    /**
     * Overrides the equals method to check equality based on photo file and name.
     *
     * @param o The object to compare with this photo.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Photo) {
            Photo photo = (Photo) o;
            return (this.picture.equals(photo.picture) && this.name.equals(photo.getName()));
        } else {
            return false;
        }
    }

    /**
     * Serializes the photo object to a file.
     *
     * @param photo The photo object to serialize.
     * @throws IOException If an I/O error occurs.
     */
    public static void serialize(Photo photo) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"));
        oos.writeObject(photo);
        oos.close();
    }

    /**
     * Deserializes the photo object from a file.
     *
     * @return The deserialized photo object.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of the serialized object cannot be found.
     */
    public static Photo deserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"));
        Photo photo = (Photo) ois.readObject();
        ois.close();
        return photo;
    }
}