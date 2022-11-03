package vx.sitas.sitas_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vx.sitas.sitas_backend.dto.internal.SongDownloadListDTO;
import vx.sitas.sitas_backend.dto.internal.StringResponse;
import vx.sitas.sitas_backend.dto.mongo.SongDownloadPOJO;
import vx.sitas.sitas_backend.dto.mongo.StorageNodePOJO;
import vx.sitas.sitas_backend.service.database.SongDownloadRepository;
import vx.sitas.sitas_backend.service.database.StorageNodeRepository;
import vx.sitas.sitas_backend.service.node_storage.StorageNode;

import javax.annotation.PostConstruct;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SongDownloadService {


    @Autowired
    private SongDownloadRepository songDownloadRepository;

    @Autowired
    private StorageNodeRepository storageNodeRepository;

    List<StorageNode> storageNodes = new ArrayList<>();


    @PostConstruct
    private void initStorageNodes(){
        List<StorageNodePOJO> storageNodes = storageNodeRepository.findAll();
        for(StorageNodePOJO snPOJO : storageNodes){
            vx.sitas.sitas_backend.dto.internal.StorageNode storageNode = snPOJO.toStorageNode();
            this.storageNodes.add(StorageNode.build(storageNode));
        }
    }

    public List<SongDownloadListDTO> getSongDownloads(String userId) throws Exception{
        if(userId == null)  throw new AccessDeniedException("Invalid user");

        List<SongDownloadPOJO> dbList = songDownloadRepository.getSongDownloads(userId);

        List<SongDownloadListDTO> returnList = new ArrayList<>();
        for(SongDownloadPOJO songDownload : dbList){
            returnList.add(new SongDownloadListDTO(songDownload.toSongDownload()));
        }

        return returnList;
    }

    public StringResponse getSongDownloadUrl(String downloadId) throws Exception{
        SongDownloadPOJO songDownload = songDownloadRepository.getSongDownloadById(downloadId);

        if(songDownload == null){
            throw new Exception("download id not found " + downloadId);
        }

        StorageNode storageNode = this.searchStorageNode(songDownload.getStorageNodeName());
        if(storageNode == null){
            throw new Exception("Storage node not found " + songDownload.getStorageNodeName());
        }

        String downloadUrl = storageNode.getDownloadUrl(songDownload.getDownloadName() + ".mp3");
        if(downloadUrl == null){
            throw new Exception("Song name not found in storage node " + songDownload.getDownloadName());
        }

        this.songDownloadRepository.deleteByDownloadId(songDownload.getDownloadId());

        return new StringResponse(downloadUrl);
    }

    private StorageNode searchStorageNode(String nodeName){
        for(StorageNode storageNode : this.storageNodes){
            if(storageNode.getNodeInfo().getName().equalsIgnoreCase(nodeName)){
                return storageNode;
            }
        }
        return null;
    }
}
