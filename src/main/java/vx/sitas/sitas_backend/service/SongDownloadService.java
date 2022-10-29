package vx.sitas.sitas_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vx.sitas.sitas_backend.dto.internal.SongDownloadListDTO;
import vx.sitas.sitas_backend.dto.mongo.SongDownloadPOJO;
import vx.sitas.sitas_backend.service.database.SongDownloadRepository;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SongDownloadService {


    @Autowired
    private SongDownloadRepository songDownloadRepository;

    public List<SongDownloadListDTO> getSongDownloads(String userId) throws Exception{
        if(userId == null)  throw new AccessDeniedException("Invalid user");

        List<SongDownloadPOJO> dbList = songDownloadRepository.getSongDownloads(userId);

        List<SongDownloadListDTO> returnList = new ArrayList<>();
        for(SongDownloadPOJO songDownload : dbList){
            returnList.add(new SongDownloadListDTO(songDownload.toSongDownload()));
        }

        return returnList;
    }
}
