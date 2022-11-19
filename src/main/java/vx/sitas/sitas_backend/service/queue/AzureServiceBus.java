package vx.sitas.sitas_backend.service.queue;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vx.sitas.sitas_backend.dto.queue.QueueDownloadRequest;

@Service("azureQueue")
public class AzureServiceBus implements QueueService{

    private ServiceBusSenderClient downloadRequestSender;

    @Autowired
    public AzureServiceBus() throws Exception{
        downloadRequestSender = new ServiceBusClientBuilder()
                .connectionString(System.getenv("AZURE_SERVICE_BUS_CONNECTION_STRING"))
                .sender()
                .queueName(System.getenv("DOWNLOAD_REQUEST_QUEUE"))
                .buildClient();
    }

    @Override
    public void sendRequest(QueueDownloadRequest downloadRequest) {
        try{
            Gson gson = new Gson();

            String downloadRequestJson = gson.toJson(downloadRequest);

            downloadRequestSender.sendMessage(new ServiceBusMessage(downloadRequestJson));

        } catch (Exception ex){
            System.err.println("Error sending message: " + ex.getMessage());
        }
    }
}
