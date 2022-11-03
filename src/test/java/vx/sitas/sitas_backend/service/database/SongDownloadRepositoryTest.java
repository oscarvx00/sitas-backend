package vx.sitas.sitas_backend.service.database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import vx.sitas.sitas_backend.dto.mongo.SongDownloadPOJO;
import vx.sitas.sitas_backend.service.SongDownloadServiceTest;

import java.util.List;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class SongDownloadRepositoryTest {

    @Autowired
    private SongDownloadRepository songDownloadRepository;

    @DisplayName("given userId to search "
    + "when test object is saved "
    + "then a list of SongDownloadPOJO is returned")
    @Test
    void testDataListObtained(@Autowired MongoTemplate mongoTemplate){
        //given
        SongDownloadPOJO objectToSave = new SongDownloadPOJO(
                "_id1",
                "test_user_id",
                "test_download_id1",
                "test_song_name",
                true,
                "test_status",
                "test_storage_node_name",
                "test_download_name1"
        );

        //when
        mongoTemplate.save(objectToSave);

        //then
        List<SongDownloadPOJO> songDownloadPOJOList = songDownloadRepository.getSongDownloads("test_user_id");
        Assertions.assertEquals("test_download_id1", songDownloadPOJOList.get(0).getDownloadId());

        //restore
        songDownloadRepository.deleteByDownloadId(objectToSave.getDownloadId());
    }

    @DisplayName("given userId to search "
    + "when user has no song downloads "
    + "a empty list must be returned")
    @Test
    void testDataListEmpty(){
        final String invalidUser = "invalid_user_id";

        List<SongDownloadPOJO> songDownloadPOJOList = songDownloadRepository.getSongDownloads(invalidUser);
        Assertions.assertEquals(0, songDownloadPOJOList.size());
    }

    @DisplayName("given downloadId to search "
    + "when the SongDownload exists " +
            "the object should be returned")
    @Test
    void getSongDownloadById() {
        //Given
        SongDownloadPOJO objectToSave = new SongDownloadPOJO(
                "_id1",
                "test_user_id",
                "test_download_id1",
                "test_song_name",
                true,
                "test_status",
                "test_storage_node_name",
                "test_download_name1"
        );

        //when
        songDownloadRepository.save(objectToSave);

        //then
        SongDownloadPOJO songDownloadPOJO = songDownloadRepository.getSongDownloadById(objectToSave.getDownloadId());
        Assertions.assertEquals(objectToSave.getDownloadId(), songDownloadPOJO.getDownloadId());
        Assertions.assertEquals(objectToSave.getId(), songDownloadPOJO.getId());

        //restore
        songDownloadRepository.deleteByDownloadId(objectToSave.getDownloadId());
    }

    @DisplayName("given downloadId to search "
            + "when the SongDownload NOT exists " +
            "null should be returned")
    @Test
    void getSongDownloadByIdNotFound(){
        //Given
        SongDownloadPOJO objectToSave = new SongDownloadPOJO(
                "_id1",
                "test_user_id",
                "test_download_id1",
                "test_song_name",
                true,
                "test_status",
                "test_storage_node_name",
                "test_download_name1"
        );

        //when
        songDownloadRepository.save(objectToSave);

        //then
        SongDownloadPOJO songDownloadPOJO = songDownloadRepository.getSongDownloadById("bad_download_id");
        Assertions.assertNull(songDownloadPOJO);

        //restore
        songDownloadRepository.deleteByDownloadId(objectToSave.getDownloadId());
    }

}
