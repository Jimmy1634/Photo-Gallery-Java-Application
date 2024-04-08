package app.photoapplication.model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents an album containing photos.
 *
 * @author Jimmy Han, Luthfi Jamal Mohamed
 */
public class Album implements Serializable {
	private static final long serialVersionUID = 1L;

	private String albumName;
	private ArrayList<Photo> photos;
	private int count = 0;
	private Photo photo;
	private Date earliestPhoto;
	private Date latestPhoto;
	private int displayPhotoIndex;

	/**
	 * Constructs an empty album with the given name.
	 *
	 * @param albumName The name of the album.
	 */
	public Album(String albumName) {
		this.albumName = albumName;
		this.photos = new ArrayList<>();
		this.count = 0;
		this.earliestPhoto = null;
		this.latestPhoto = null;
		this.displayPhotoIndex = -1;
	}

	/**
	 * Constructs an album with the given name and photos.
	 *
	 * @param albumName The name of the album.
	 * @param photos    The list of photos to initialize the album with.
	 */
	public Album(String albumName, ArrayList<Photo> photos) {
		this.albumName = albumName;
		this.photos = photos;
		this.count = photos.size();
		this.earliestPhoto = findEarliestPhoto();
		this.latestPhoto = findLatestPhoto();
		this.displayPhotoIndex = (this.count > 0) ? 0 : -1;
	}

	/**
	 * Retrieves the index of the currently displayed photo.
	 *
	 * @return The index of the currently displayed photo.
	 */
	public int getDisplayPhotoIndex() {
		return displayPhotoIndex;
	}

	/**
	 * Sets the index of the currently displayed photo.
	 *
	 * @param newIndex The index to set for the currently displayed photo.
	 */
	public void setDisplayPhotoIndex(int newIndex) {
		this.displayPhotoIndex = newIndex;
	}

	/**
	 * Retrieves the name of the album.
	 *
	 * @return The name of the album.
	 */
	public String getAlbumName() {
		return albumName;
	}

	/**
	 * Retrieves the number of photos in the album.
	 *
	 * @return The number of photos in the album.
	 */
	public int getCount() {
		return this.count;
	}

	/**
	 * Sets the name of the album.
	 *
	 * @param albumName The name to set for the album.
	 */
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	/**
	 * Retrieves the date of the earliest photo in the album.
	 *
	 * @return The date of the earliest photo.
	 */
	public Date getEarliestPhoto() {
		return this.earliestPhoto;
	}

	/**
	 * Retrieves the date of the latest photo in the album.
	 *
	 * @return The date of the latest photo.
	 */
	public Date getLatestPhoto() {
		return this.latestPhoto;
	}

	/**
	 * Retrieves the list of photos in the album.
	 *
	 * @return The list of photos.
	 */
	public ArrayList<Photo> getPhotos() {
		return photos;
	}

	/**
	 * Retrieves the currently selected photo.
	 *
	 * @return The currently selected photo.
	 */
	public Photo getPhoto() {
		return photo;
	}

	/**
	 * Sets the currently selected photo based on index.
	 *
	 * @param index The index of the photo to set as currently selected.
	 */
	public void setPhoto(int index) {
		this.photo = photos.get(index);
	}

	/**
	 * Sets the currently selected photo.
	 *
	 * @param selectedPhoto The photo to set as currently selected.
	 */
	public void setSelectedPhoto(Photo selectedPhoto) {
		this.photo = selectedPhoto;
	}

	/**
	 * Adds a photo to the album.
	 *
	 * @param photo The photo to add.
	 * @return True if the photo was added successfully, false otherwise.
	 */
	public boolean addPhoto(Photo photo) {
		if (photos.contains(photo)) return false;
		photos.add(photo);
		this.count++;
		if (this.earliestPhoto == null && this.latestPhoto == null) {
			this.earliestPhoto = photo.getDate();
			this.latestPhoto = photo.getDate();
		}
		if (photo.getDate().compareTo(earliestPhoto) < 0) {
			this.earliestPhoto = photo.getDate();
		}
		if (photo.getDate().compareTo(latestPhoto) > 0) {
			this.latestPhoto = photo.getDate();
		}
		return true;
	}

	/**
	 * Deletes a photo from the album based on index.
	 *
	 * @param index The index of the photo to delete.
	 */
	public void deletePhoto(int index) {
		photos.remove(index);
		this.count--;
		this.earliestPhoto = findEarliestPhoto();
		this.latestPhoto = findLatestPhoto();
	}

	/**
	 * Overrides the toString method to return the album name.
	 *
	 * @return The album name.
	 */
	@Override
	public String toString() {
		return getAlbumName();
	}

	/**
	 * Finds the date of the earliest photo in the album.
	 *
	 * @return The date of the earliest photo.
	 */
	public Date findEarliestPhoto() {
		if (this.photos.size() == 0) return null;
		Date earliest = this.photos.get(0).getDate();
		for (Photo p : this.photos) {
			if (p.getDate().compareTo(earliest) < 0) {
				earliest = p.getDate();
			}
		}
		return earliest;
	}

	/**
	 * Finds the date of the latest photo in the album.
	 *
	 * @return The date of the latest photo.
	 */
	public Date findLatestPhoto() {
		if (this.photos.size() == 0) return null;
		Date latest = this.photos.get(0).getDate();
		for (Photo p : this.photos) {
			if (p.getDate().compareTo(latest) > 0) {
				latest = p.getDate();
			}
		}
		return latest;
	}

	/**
	 * Retrieves album details including name, photo count, and date range.
	 *
	 * @return Album details as a string.
	 */
	public String getAlbumDetails() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		StringBuilder details = new StringBuilder();
		details.append("Name: ").append(this.albumName).append("\n");
		details.append("# of Photos: ").append(this.count).append("\n");
		if (earliestPhoto != null && latestPhoto != null)
			details.append("Dates: ").append(formatter.format(this.earliestPhoto)).append(" - ").append(formatter.format(this.latestPhoto)).append("\n");
		return details.toString();
	}

	/**
	 * Serializes the album object to a file.
	 *
	 * @param album The album object to serialize.
	 * @throws IOException If an I/O error occurs while writing to the file.
	 */
	public static void serialize(Album album) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"));
		oos.writeObject(album);
		oos.close();
	}

	/**
	 * Deserializes the album object from a file.
	 *
	 * @return The deserialized album object.
	 * @throws IOException            If an I/O error occurs while reading from the file.
	 * @throws ClassNotFoundException If the class of the serialized object cannot be found.
	 */
	public static Album deserialize() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"));
		Album album = (Album) ois.readObject();
		ois.close();
		return album;
	}
}