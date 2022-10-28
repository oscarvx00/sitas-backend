package vx.sitas.sitas_backend.service.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import vx.sitas.sitas_backend.dto.mongo.SongDownloadPOJO;

import java.util.List;

public interface SongDownloadRepository extends MongoRepository<SongDownloadPOJO, String> {
    @Query("{userId:'?0'}")
    List<SongDownloadPOJO> getSongDownloads(String userId);

    public long count();
}
