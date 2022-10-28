package vx.sitas.sitas_backend.dto.internal;

public class SongDownloadListDTO {

    private String name;
    private String downloadId;

    public SongDownloadListDTO(SongDownload songDownload){
        this.name = songDownload.getSongName();
        this.downloadId = songDownload.getDownloadId();
    }

}
