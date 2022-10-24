package vx.sitas.sitas_backend.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vx.sitas.sitas_backend.dto.rabbit.RabbitDownloadRequest;
import vx.sitas.sitas_backend.service.rabbit.QueueService;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class RequestService {

    @Autowired
    QueueService rabbitService;


    public void createDownloadRequests(String[] songsNames, String userId){
        for(String songName : songsNames){
            RabbitDownloadRequest downloadRequest = new RabbitDownloadRequest(
                    songName,
                    userId
            );
            downloadRequest = setDestModules(downloadRequest);
            String selectedModule = decideModule(downloadRequest);
            if(selectedModule == null) continue;

            rabbitService.sendRequest(downloadRequest, selectedModule);
        }
    }

    private RabbitDownloadRequest setDestModules(RabbitDownloadRequest downloadRequest){
        String songName = downloadRequest.getSongName();

        if(songName.startsWith("https://soundcloud.com") || songName.startsWith("soundcloud.com")){
            downloadRequest.setSoundcloud(true);
            downloadRequest.setDirect(true);
        }
        else if(!songName.contains(".")){
            //ENABLE ALL
            downloadRequest.setSoundcloud(true);
            downloadRequest.setDirect(false);
        }

        return downloadRequest;
    }

    private String decideModule(RabbitDownloadRequest downloadRequest){
        List<String> availableModules = new ArrayList<>();
        if(downloadRequest.isSoundcloud()) availableModules.add("soundcloud");

        if(availableModules.size() == 0)    return null;

        return availableModules.get(new Random().nextInt(availableModules.size()));
    }
}
