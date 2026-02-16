package projekat.ISA.Services;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.databind.ObjectMapper;
import projekat.ISA.Domain.UploadEvent;
import projekat.ISA.Proto.UploadEventOuterProtoClass;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.google.protobuf.*;
import org.springframework.stereotype.Service;

@Service
public class UploadEventService {

    private final String queueName;
    private final ObjectMapper mapper = new ObjectMapper();

    public UploadEventService() {
        this.queueName = "UploadVideoQueue";
    }

    private ConnectionFactory factory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        return factory;
    }
    
    private void send(byte[] message) throws Exception {
        try (Connection connection = factory().newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(queueName, true, false, false, null);
            channel.basicPublish("", queueName, null, message);
        }
    }

    public void sendJson(String title, String author, String description, LocalDateTime uploadTimestamp) throws Exception {
    	UploadEvent event = new UploadEvent(title, author, description, uploadTimestamp);
        byte[] message = mapper.writeValueAsBytes(event);
        send(message);
        System.out.println("Sent JSON message (" + message.length + " bytes)");
    }

    public void sendProtobuf(String title, String author, String description, LocalDateTime uploadTimestamp) throws Exception {
    	UploadEvent event = new UploadEvent(title, author, description, uploadTimestamp);
    	
    	LocalDateTime uploadTime = event.getUploadTimestamp();
    	Timestamp ts = Timestamp.newBuilder()
    	        .setSeconds(uploadTime.atZone(ZoneId.systemDefault()).toEpochSecond())
    	        .setNanos(uploadTime.getNano())
    	        .build();
    	
        UploadEventOuterProtoClass.UploadEvent proto = UploadEventOuterProtoClass.UploadEvent.newBuilder()
                .setTitle(event.getTitle())
                .setAuthor(event.getAuthor())
                .setDescription(event.getDescription())
                .setUploadTimestamp(ts)
                .build();

        byte[] message = proto.toByteArray();
        send(message);
        System.out.println("Sent Protobuf message (" + message.length + " bytes)");
    }
}