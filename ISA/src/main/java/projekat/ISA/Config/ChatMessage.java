package projekat.ISA.Config;

public class ChatMessage {
	private Long videoId;
	private String sender;
	private String content;

	public ChatMessage() {
	}

	public ChatMessage(Long videoId, String sender, String content) {
		this.videoId = videoId;
		this.sender = sender;
		this.content = content;
	}

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
