package vx.sitas.sitas_backend.service.database;

import org.springframework.stereotype.Service;
import vx.sitas.sitas_backend.dto.internal.SongDownload;

import java.util.List;

@Service
public interface DAO {
    List<SongDownload> getSongDownloads(String userId);
}
