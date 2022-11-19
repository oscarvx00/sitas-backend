package vx.sitas.sitas_backend.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import vx.sitas.sitas_backend.dto.internal.SongDownload;
import vx.sitas.sitas_backend.dto.mongo.SongDownloadPOJO;
import vx.sitas.sitas_backend.dto.queue.QueueDownloadRequest;
import vx.sitas.sitas_backend.service.database.SongDownloadRepository;
import vx.sitas.sitas_backend.service.queue.QueueService;


import java.security.SecureRandom;

@Service
public class RequestService {

    @Autowired
    @Qualifier("azureQueue")
    QueueService rabbitService;

    @Autowired
    SongDownloadRepository songDownloadRepository;

    SecureRandom random = new SecureRandom();


    public void createDownloadRequests(String[] songsNames, String userId){
        for(String songName : songsNames){
            QueueDownloadRequest downloadRequest = new QueueDownloadRequest(
                    songName.trim(),
                    userId
            );
            downloadRequest = setDestModules(downloadRequest);
            if(!checkAnyModuleActive(downloadRequest)) continue;

            SongDownload songDownload = new SongDownload(
                    downloadRequest.getDownloadId(),
                    downloadRequest.getSongName(),
                    "DOWNLOADING",
                    null,
                    downloadRequest.getUserId(),
                    false,
                    ""
            );
            songDownloadRepository.insert(new SongDownloadPOJO(songDownload));

            rabbitService.sendRequest(downloadRequest);
        }
    }

    private QueueDownloadRequest setDestModules(QueueDownloadRequest downloadRequest){
        String songName = downloadRequest.getSongName();

        if(songName.startsWith("https://soundcloud.com") || songName.startsWith("soundcloud.com")){
            downloadRequest.setSoundcloud(true);
            downloadRequest.setDirect(true);
        }
        else if(songName.startsWith("https://www.youtube.com") || songName.startsWith("www.youtube.com")){
            downloadRequest.setYoutube(true);
            downloadRequest.setDirect(true);
        }
        else if(!songName.contains(".com")){
            //ENABLE ALL
            downloadRequest.setYoutube(true);
            downloadRequest.setSoundcloud(true);
            downloadRequest.setDirect(false);
        }

        return downloadRequest;
    }

    private boolean checkAnyModuleActive(QueueDownloadRequest queueDownloadRequest){
        return queueDownloadRequest.isYoutube()
                || queueDownloadRequest.isSpotify()
                || queueDownloadRequest.isSoundcloud();
    }

}
