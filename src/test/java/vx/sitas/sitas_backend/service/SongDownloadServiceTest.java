package vx.sitas.sitas_backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import vx.sitas.sitas_backend.dto.internal.SongDownloadListDTO;
import vx.sitas.sitas_backend.dto.mongo.SongDownloadPOJO;
import vx.sitas.sitas_backend.service.database.SongDownloadRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@DataMongoTest
public class SongDownloadServiceTest {

    @MockBean
    private SongDownloadRepository songDownloadRepository;


    @Captor
    ArgumentCaptor<String> userIdCaptor;

    @InjectMocks
    SongDownloadService songDownloadService;


    final String testId = "test_user_id";

    @BeforeEach
    void init(){
        Mockito.when(songDownloadRepository.getSongDownloads(testId)).thenReturn(new ArrayList<>(
                Arrays.asList(
                        new SongDownloadPOJO(
                                "_id1",
                                "test_user_id",
                                "test_download_id1",
                                "test_song_name",
                                true,
                                "test_status",
                                "test_storage_node_name"
                        ),
                        new SongDownloadPOJO(
                                "_id2",
                                "test_user_id",
                                "test_download_id2",
                                "test_song_name",
                                true,
                                "test_status",
                                "test_storage_node_name"
                        )
                )
        ));
    }


    @Test
    void getSongDownloads() throws Exception{


        List<SongDownloadListDTO> response = songDownloadService.getSongDownloads(testId);

        Assertions.assertEquals(response.get(0).getDownloadId(), "test_download_id1");
        Assertions.assertEquals(response.get(1).getDownloadId(), "test_download_id2");
    }

}
