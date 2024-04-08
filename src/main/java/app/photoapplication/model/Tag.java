package app.photoapplication.model;

import java.io.Serializable;

/**
 * Represents a tag associated with a photo, consisting of a name and a value.
 *
 * @author Jimmy Han, Luthfi Jamal Mohamed
 */
public class Tag implements Serializable {
    private String name;
    private String value;

    /**
     * Constructs a tag with the specified name and value.
     *
     * @param name  The name of the tag.
     * @param value The value of the tag.
     */
    public Tag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Retrieves the name of the tag.
     *
     * @return The name of the tag.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the value of the tag.
     *
     * @return The value of the tag.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Compares this tag to another object for equality.
     *
     * @param o The object to compare.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Tag) {
            Tag tag = (Tag) o;
            return this.name.equals(tag.getName()) && this.value.equals(tag.getValue());
        } else {
            return false;
        }
    }

    /**
     * Returns a string representation of the tag.
     *
     * @return A string representation of the tag.
     */
    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}