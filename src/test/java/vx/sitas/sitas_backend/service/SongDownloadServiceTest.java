package vx.sitas.sitas_backend.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.context.IContext;
import vx.sitas.sitas_backend.dto.internal.SongDownload;
import vx.sitas.sitas_backend.dto.internal.SongDownloadListDTO;
import vx.sitas.sitas_backend.dto.internal.StorageNodeType;
import vx.sitas.sitas_backend.dto.internal.StringResponse;
import vx.sitas.sitas_backend.dto.mongo.SongDownloadPOJO;
import vx.sitas.sitas_backend.service.database.SongDownloadRepository;
import vx.sitas.sitas_backend.service.database.StorageNodeRepository;
import vx.sitas.sitas_backend.service.node_storage.MinioStorageNode;
import vx.sitas.sitas_backend.service.node_storage.StorageNode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataMongoTest
public class SongDownloadServiceTest {

    @MockBean
    private SongDownloadRepository songDownloadRepository;

    @MockBean
    private StorageNodeRepository storageNodeRepository;


    @Captor
    ArgumentCaptor<String> userIdCaptor;

    @InjectMocks
    SongDownloadService songDownloadService;

    private MinioClient minioClient;


    final String testId = "test_user_id";
    final String SONG_DOWNLOAD_ID="test_download_id1";
    final String SONG_DOWNLOAD_ID_BAD_SONGNAME="test_download_id_bad_songname";

    @BeforeAll
    void initStorageNodes() throws Exception{
        List< StorageNode> storageNodes = new ArrayList<>();

        vx.sitas.sitas_backend.dto.internal.StorageNode testNode = new vx.sitas.sitas_backend.dto.internal.StorageNode(
                "oscar-minio",
                false,
                System.getenv("MINIO_NODE_BUCKET"),
                StorageNodeType.MINIO,
                System.getenv("MINIO_NODE_ENDPOINT"),
                System.getenv("MINIO_NODE_USER"),
                System.getenv("MINIO_NODE_PASS")
        );
        storageNodes.add(StorageNode.build(testNode));

        ReflectionTestUtils.setField(
                songDownloadService,
                "storageNodes",
                storageNodes
        );

        this.minioClient = MinioClient.builder()
                .endpoint(System.getenv("MINIO_NODE_ENDPOINT"))
                .credentials(System.getenv("MINIO_NODE_USER"), System.getenv("MINIO_NODE_PASS"))
                .build();

        //Create and upload test_songname.mp3 to minio
        final String dummy = "dummy_content";
        InputStream targetStream = new ByteArrayInputStream(dummy.getBytes());
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(System.getenv("MINIO_NODE_BUCKET"))
                        .object("test_song_name.mp3")
                        .stream(targetStream, -1, 10485760)
                        .build()
        );
    }

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
                                "test_storage_node_name",
                                "test_download_name1"
                        ),
                        new SongDownloadPOJO(
                                "_id2",
                                "test_user_id",
                                "test_download_id2",
                                "test_song_name",
                                true,
                                "test_status",
                                "test_storage_node_name",
                                "test_download_name2"
                        )
                )
        ));

        Mockito.when(songDownloadRepository.getSongDownloadById(SONG_DOWNLOAD_ID)).thenReturn(
                        new SongDownloadPOJO(
                                "_id1",
                                "test_user_id",
                                SONG_DOWNLOAD_ID,
                                "test_song_name",
                                true,
                                "test_status",
                                "oscar-minio",
                                "test_download_name1"
                        ));
        Mockito.when(songDownloadRepository.getSongDownloadById(SONG_DOWNLOAD_ID_BAD_SONGNAME)).thenReturn(
                new SongDownloadPOJO(
                        "_id2",
                        "test_user_id",
                        SONG_DOWNLOAD_ID_BAD_SONGNAME,
                        "test_song_name_bad",
                        true,
                        "test_status",
                        "oscar-minio",
                        "test_download_name2"
                ));

    }


    @Test
    void getSongDownloads() throws Exception{
        List<SongDownloadListDTO> response = songDownloadService.getSongDownloads(testId);

        Assertions.assertEquals(response.get(0).getDownloadId(), "test_download_id1");
        Assertions.assertEquals(response.get(1).getDownloadId(), "test_download_id2");
    }

    @DisplayName("When the method getSongDownloadUrl is called "
    + "if the downloadId exists in db AND the song name exists in storage node "
    + "string response must be returned")
    @Test
    void getSongDownloadUrl() throws Exception{

        //Call method
        StringResponse res = songDownloadService.getSongDownloadUrl(SONG_DOWNLOAD_ID);

        Assertions.assertTrue(res.getVal().contains("test_download_name1.mp3"));

    }

    @DisplayName("When the method getSongDownloadUrl is called "
    + "if the downloadId NOT exists "
    + "exception 'download id not found {id}' must be thrown")
    @Test
    void getSongDownloadIdNotFound(){
        try{
            StringResponse res = songDownloadService.getSongDownloadUrl("bad_downloadId");
        } catch (Exception ex){
            Assertions.assertEquals("download id not found bad_downloadId", ex.getMessage());
        }
    }

    @DisplayName("When the method getSongDownloadUrl is called "
    + "if the downloadId exists AND the song name does NOT exist in storage node "
    + "exception 'Song name not found in storage node {storageNodeName}' must be thrown")
    @Test
    void getSongDownloadNotFoundInStorage(){
        try{
            StringResponse res = songDownloadService.getSongDownloadUrl(SONG_DOWNLOAD_ID_BAD_SONGNAME);
        }catch (Exception ex){
            Assertions.assertEquals("Song name not found in storage node test_song_name_bad", ex.getMessage());
        }
    }

}
