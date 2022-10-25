package vx.sitas.sitas_backend.service.rabbit;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import vx.sitas.sitas_backend.dto.rabbit.RabbitDownloadRequest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RabbitServiceTest {

    private Connection connection;
    private Channel channel;

    final String QUEUE_TEST = "sitas-test-request-queue";
    final String MODULE_TEST = "test_module";

    @InjectMocks
    RabbitService rabbitService;

    @BeforeAll
    void setUp() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(System.getenv("RABBITMQ_ENDPOINT"));
        connectionFactory.setVirtualHost(System.getenv("RABBITMQ_VHOST"));
        connectionFactory.setUsername(System.getenv("RABBITMQ_USER"));
        connectionFactory.setPassword(System.getenv("RABBITMQ_PASS"));

        this.connection = connectionFactory.newConnection();
        this.channel = connection.createChannel();

        //Declare exchange, queue and bind
        channel.exchangeDeclare(System.getenv("DOWNLOAD_REQUEST_EXCHANGE"), "direct");
        channel.queueDeclare(QUEUE_TEST, false, true, true, null);
        channel.queueBind(QUEUE_TEST, System.getenv("DOWNLOAD_REQUEST_EXCHANGE"), MODULE_TEST);

    }

    @Test
    void testSendRequest() throws IOException{
        Gson gson = new Gson();

        RabbitDownloadRequest rabbitDownloadRequest = new RabbitDownloadRequest(
                "test_name",
                "test_user"
        );
        rabbitDownloadRequest.setSoundcloud(true);

        AtomicReference<RabbitDownloadRequest> receivedRequest = new AtomicReference<>();

        DeliverCallback deliverCallback = ((consumerTag, message) -> {

            receivedRequest.set(gson.fromJson(new String(message.getBody(), "UTF-8"), RabbitDownloadRequest.class));

        });

        channel.basicConsume(QUEUE_TEST, true, deliverCallback, consumerTag -> {});

        rabbitService.sendRequest(rabbitDownloadRequest, MODULE_TEST);

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertTrue(receivedRequest.get().isSoundcloud());
        Assertions.assertFalse(receivedRequest.get().isSpotify());
        Assertions.assertEquals("test_name", receivedRequest.get().getSongName());

    }

}
