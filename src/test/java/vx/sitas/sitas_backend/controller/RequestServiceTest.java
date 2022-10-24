package vx.sitas.sitas_backend.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import vx.sitas.sitas_backend.dto.rabbit.RabbitDownloadRequest;
import vx.sitas.sitas_backend.service.RequestService;
import vx.sitas.sitas_backend.service.rabbit.QueueService;
import vx.sitas.sitas_backend.service.rabbit.RabbitService;


@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @Mock
    private QueueService queueService;

    @Captor
    ArgumentCaptor<RabbitDownloadRequest> sendDownloadRequestCaptor;
    @Captor
    ArgumentCaptor<String> sendDowloadRequestModuleCaptor;

    @InjectMocks
    RequestService requestService;

    @Test
    void setDestModulesSoundcloud1(){
        final String[] songs = {"https://soundcloud.com/duummy/dummy"};

        requestService.createDownloadRequests(songs, "test_user");

        Mockito.verify(queueService).sendRequest(sendDownloadRequestCaptor.capture(), sendDowloadRequestModuleCaptor.capture());

        Assertions.assertTrue(sendDownloadRequestCaptor.getValue().isSoundcloud());
        Assertions.assertEquals("soundcloud", sendDowloadRequestModuleCaptor.getValue());

    }
}
