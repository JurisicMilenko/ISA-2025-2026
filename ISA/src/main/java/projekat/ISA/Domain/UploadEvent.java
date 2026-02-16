package projekat.ISA.Domain;

import java.time.LocalDateTime;

public class UploadEvent {
	private String title;
	private String author;
	private String description;
	private LocalDateTime uploadTimestamp;

	public UploadEvent() {
	}

	public UploadEvent(String title, String author, String description, LocalDateTime uploadTimestamp) {
		this.title = title;
		this.author = author;
		this.description = description;
		this.uploadTimestamp = uploadTimestamp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getUploadTimestamp() {
		return uploadTimestamp;
	}

	public void setUploadTimestamp(LocalDateTime uploadTimestamp) {
		this.uploadTimestamp = uploadTimestamp;
	}
}
