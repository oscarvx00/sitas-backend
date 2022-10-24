package vx.sitas.sitas_backend.service.rabbit;


import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vx.sitas.sitas_backend.dto.rabbit.RabbitDownloadRequest;

@Service
public class RabbitService implements QueueService{

    private Connection connection;
    private Channel channel;

    @Autowired
    public RabbitService() throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(System.getenv("RABBITMQ_ENDPOINT"));
        connectionFactory.setVirtualHost(System.getenv("RABBITMQ_VHOST"));
        connectionFactory.setUsername(System.getenv("RABBITMQ_USER"));
        connectionFactory.setPassword(System.getenv("RABBITMQ_PASS"));

        this.connection = connectionFactory.newConnection();
        this.channel = connection.createChannel();

        //Declare exchange
        channel.exchangeDeclare(System.getenv("DOWNLOAD_REQUEST_EXCHANGE"), "direct");
    }

    @Override
    public void sendRequest(RabbitDownloadRequest downloadRequest, String selectedModule) {
        try{
            Gson gson = new Gson();

            String downloadRequestJson = gson.toJson(downloadRequest);

            channel.basicPublish(System.getenv("DOWNLOAD_REQUEST_EXCHANGE"), selectedModule, null, downloadRequestJson.getBytes("UTF-8"));
        } catch (Exception ex){
            System.err.println("Error sending message: " + ex.getMessage());
        }
    }
}
