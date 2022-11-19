package vx.sitas.sitas_backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import vx.sitas.sitas_backend.dto.queue.QueueDownloadRequest;
import vx.sitas.sitas_backend.service.database.SongDownloadRepository;
import vx.sitas.sitas_backend.service.queue.QueueService;


@ExtendWith(MockitoExtension.class)
@DataMongoTest
public class RequestServiceTest {

    @Mock
    private QueueService queueService;

    @Captor
    ArgumentCaptor<QueueDownloadRequest> sendDownloadRequestCaptor;

    @MockBean
    private SongDownloadRepository songDownloadRepository;

    @InjectMocks
    RequestService requestService;

    @Test
    void setDestModulesSoundcloud1(){
        final String[] songs = {"https://soundcloud.com/dummy/dummy"};

        requestService.createDownloadRequests(songs, "test_user");

        Mockito.verify(queueService).sendRequest(sendDownloadRequestCaptor.capture());

        QueueDownloadRequest capturedDownloadRequest = sendDownloadRequestCaptor.getValue();
        Assertions.assertTrue(capturedDownloadRequest.isSoundcloud());
        Assertions.assertFalse(capturedDownloadRequest.isSpotify());
        Assertions.assertFalse(capturedDownloadRequest.isYoutube());
        Assertions.assertTrue(capturedDownloadRequest.isDirect());
    }

    @Test
    void setDestModulesSoundcloud2(){
        final String[] songs = {"soundcloud.com/dummy/dummy"};

        requestService.createDownloadRequests(songs, "test_user");

        Mockito.verify(queueService).sendRequest(sendDownloadRequestCaptor.capture());

        QueueDownloadRequest capturedDownloadRequest = sendDownloadRequestCaptor.getValue();
        Assertions.assertTrue(capturedDownloadRequest.isSoundcloud());
        Assertions.assertFalse(capturedDownloadRequest.isSpotify());
        Assertions.assertFalse(capturedDownloadRequest.isYoutube());
        Assertions.assertTrue(capturedDownloadRequest.isDirect());
    }

    @Test
    void setDestModuleAnyNotDirect(){
        final String[] songs = {"song_name"};

        requestService.createDownloadRequests(songs, "test_user");

        Mockito.verify(queueService).sendRequest(sendDownloadRequestCaptor.capture());

        QueueDownloadRequest capturedDownloadRequest = sendDownloadRequestCaptor.getValue();
        Assertions.assertFalse(capturedDownloadRequest.isDirect());
        Assertions.assertTrue(
                capturedDownloadRequest.isYoutube() ||
                        capturedDownloadRequest.isSoundcloud() ||
                        capturedDownloadRequest.isSpotify()
        );
    }

    @Test
    void invalidUrl() {
        final String[] songs = {"https://dummy.com"};
        requestService.createDownloadRequests(songs, "test_user");

        Mockito.verify(queueService, Mockito.times(0)).sendRequest(sendDownloadRequestCaptor.capture());
    }

    //TODO: Youtube tests
}
