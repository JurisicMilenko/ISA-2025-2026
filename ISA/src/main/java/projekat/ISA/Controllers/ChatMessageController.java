package projekat.ISA.Controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import projekat.ISA.Config.ChatMessage;

@Controller
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat/{videoId}")
    public void sendMessage(@DestinationVariable Long videoId, ChatMessage message) {
        message.setVideoId(videoId);
        messagingTemplate.convertAndSend("/topic/chat/" + videoId, message);
    }
}