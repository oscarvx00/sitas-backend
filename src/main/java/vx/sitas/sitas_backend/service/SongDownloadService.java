package vx.sitas.sitas_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Service;
import vx.sitas.sitas_backend.dto.internal.SongDownload;
import vx.sitas.sitas_backend.dto.internal.SongDownloadListDTO;
import vx.sitas.sitas_backend.service.database.DAO;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SongDownloadService {


    @Autowired
    private DAO mongoManager;

    public List<SongDownloadListDTO> getSongDownloads(String userId) throws Exception{
        if(userId == null)  throw new AccessDeniedException("Invalid user");

        List<SongDownload> dbList = mongoManager.getSongDownloads(userId);

        List<SongDownloadListDTO> returnList = new ArrayList<>();
        for(SongDownload songDownload : dbList){
            returnList.add(new SongDownloadListDTO(songDownload));
        }

        return returnList;
    }
}
