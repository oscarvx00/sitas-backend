package vx.sitas.sitas_backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import vx.sitas.sitas_backend.dto.rabbit.RabbitDownloadRequest;
import vx.sitas.sitas_backend.service.RequestService;
import vx.sitas.sitas_backend.service.rabbit.QueueService;


@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @Mock
    private QueueService queueService;

    @Captor
    ArgumentCaptor<RabbitDownloadRequest> sendDownloadRequestCaptor;
    @Captor
    ArgumentCaptor<String> sendDownloadRequestModuleCaptor;

    @InjectMocks
    RequestService requestService;

    @Test
    void setDestModulesSoundcloud1(){
        final String[] songs = {"https://soundcloud.com/dummy/dummy"};

        requestService.createDownloadRequests(songs, "test_user");

        Mockito.verify(queueService).sendRequest(sendDownloadRequestCaptor.capture(), sendDownloadRequestModuleCaptor.capture());

        RabbitDownloadRequest capturedDownloadRequest = sendDownloadRequestCaptor.getValue();
        Assertions.assertTrue(capturedDownloadRequest.isSoundcloud());
        Assertions.assertFalse(capturedDownloadRequest.isSpotify());
        Assertions.assertFalse(capturedDownloadRequest.isYoutube());
        Assertions.assertTrue(capturedDownloadRequest.isDirect());
        Assertions.assertEquals("soundcloud", sendDownloadRequestModuleCaptor.getValue());
    }

    @Test
    void setDestModulesSoundcloud2(){
        final String[] songs = {"soundcloud.com/dummy/dummy"};

        requestService.createDownloadRequests(songs, "test_user");

        Mockito.verify(queueService).sendRequest(sendDownloadRequestCaptor.capture(), sendDownloadRequestModuleCaptor.capture());

        RabbitDownloadRequest capturedDownloadRequest = sendDownloadRequestCaptor.getValue();
        Assertions.assertTrue(capturedDownloadRequest.isSoundcloud());
        Assertions.assertFalse(capturedDownloadRequest.isSpotify());
        Assertions.assertFalse(capturedDownloadRequest.isYoutube());
        Assertions.assertTrue(capturedDownloadRequest.isDirect());
        Assertions.assertEquals("soundcloud", sendDownloadRequestModuleCaptor.getValue());
    }

    @Test
    void setDestModuleAnyNotDirect(){
        final String[] songs = {"song_name"};

        requestService.createDownloadRequests(songs, "test_user");

        Mockito.verify(queueService).sendRequest(sendDownloadRequestCaptor.capture(), sendDownloadRequestModuleCaptor.capture());

        RabbitDownloadRequest capturedDownloadRequest = sendDownloadRequestCaptor.getValue();
        String capturedSelectedModule = sendDownloadRequestModuleCaptor.getValue();
        Assertions.assertFalse(capturedDownloadRequest.isDirect());
        Assertions.assertTrue(
                capturedDownloadRequest.isYoutube() ||
                        capturedDownloadRequest.isSoundcloud() ||
                        capturedDownloadRequest.isSpotify()
        );
        Assertions.assertTrue(
                capturedSelectedModule.equals("soundcloud") ||
                        capturedSelectedModule.equals("spotify") ||
                        capturedSelectedModule.equals("youtube")
        );
    }

    @Test
    void invalidUrl() {
        final String[] songs = {"https://dummy.com"};
        requestService.createDownloadRequests(songs, "test_user");

        Mockito.verify(queueService, Mockito.times(0)).sendRequest(sendDownloadRequestCaptor.capture(), sendDownloadRequestModuleCaptor.capture());
    }
}
