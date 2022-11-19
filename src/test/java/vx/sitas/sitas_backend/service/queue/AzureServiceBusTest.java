package vx.sitas.sitas_backend.service.queue;


import com.azure.messaging.servicebus.*;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import vx.sitas.sitas_backend.dto.queue.QueueDownloadRequest;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

@ExtendWith(MockitoExtension.class)
public class AzureServiceBusTest {

    static AtomicReference<QueueDownloadRequest> receivedRequest = new AtomicReference<>();

    @InjectMocks
    AzureServiceBus azureServiceBus;

    @Test
    void testSendRequest(){
        Gson gson = new Gson();

        QueueDownloadRequest queueDownloadRequest = new QueueDownloadRequest(
                "test_name",
                "test_user"
        );
        queueDownloadRequest.setSoundcloud(true);

        CountDownLatch countDownLatch = new CountDownLatch(1);

        ServiceBusProcessorClient downloadRequestReceiver = new ServiceBusClientBuilder()
                .connectionString(System.getenv("AZURE_SERVICE_BUS_CONNECTION_STRING"))
                .processor()
                .queueName(System.getenv("DOWNLOAD_REQUEST_QUEUE"))
                .processMessage(AzureServiceBusTest::processMessage)
                .processError(context -> processError(context, countDownLatch))
                .buildProcessorClient();

        downloadRequestReceiver.start();

        azureServiceBus.sendRequest(queueDownloadRequest);

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        downloadRequestReceiver.close();

        Assertions.assertTrue(receivedRequest.get().isSoundcloud());
        Assertions.assertFalse(receivedRequest.get().isSpotify());
        Assertions.assertEquals("test_name", receivedRequest.get().getSongName());
    }
    private static void processMessage(ServiceBusReceivedMessageContext context) {
        ServiceBusReceivedMessage message = context.getMessage();
        Gson gson = new Gson();
        try{
            receivedRequest.set(gson.fromJson(new String(message.getBody().toBytes(), "UTF-8"), QueueDownloadRequest.class));
        } catch (UnsupportedEncodingException ex){
            System.err.println(ex.getMessage());
        }
    }

    private static void processError(ServiceBusErrorContext context, CountDownLatch countDownLatch){
        System.out.printf("Error when receiving messages from namespace: '%s'. Entity: '%s'%n",
                context.getFullyQualifiedNamespace(), context.getEntityPath());
    }

}
