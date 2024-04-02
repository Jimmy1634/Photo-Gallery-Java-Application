package com.example.photos;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Tag {
    // Constants for tag types
    private static final String LOCATION_TAG_TYPE = "location";
    private static final String PERSON_TAG_TYPE = "person";

    // Fields
    private String name; // Name of the tag
    private String type; // Type of the tag (e.g., location, person)
    private Set<String> values; // Set of values associated with the tag

    // Constructor
    public Tag(String name, String type) {
        this.name = name;
        this.type = type;
        this.values = new HashSet<>(); // Initialize values as a new HashSet
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<String> getValues() {
        return values;
    }

    public void setValues(Set<String> values) {
        this.values = values;
    }

    // Method to check if the tag is of type "location"
    public boolean isLocationTag() {
        return LOCATION_TAG_TYPE.equals(type);
    }

    // Method to check if the tag is of type "person"
    public boolean isPersonTag() {
        return PERSON_TAG_TYPE.equals(type);
    }

    // Equals method to check if two tags are equal
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name) && Objects.equals(type, tag.type) && Objects.equals(values, tag.values);
    }

    // Hash code method for hash-based collections
    @Override
    public int hashCode() {
        return Objects.hash(name, type, values);
    }

    // Method to add a value to the tag
    public void addValue(String value) {
        if (values.add(value)) {
            // Add a check to see if the tag name + value is not a duplicate
            // Check if there is already a tag with the same name and value
            // You can throw an exception or log an error message if a duplicate is found
        }
    }

    // Method to remove a value from the tag
    public void removeValue(String value) {
        values.remove(value);
    }
}
