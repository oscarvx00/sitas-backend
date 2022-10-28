package vx.sitas.sitas_backend.dto.internal;

public class SongDownloadListDTO {

    private String name;
    private String downloadId;

    public SongDownloadListDTO(SongDownload songDownload){
        this.name = songDownload.getSongName();
        this.downloadId = songDownload.getDownloadId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(String downloadId) {
        this.downloadId = downloadId;
    }
}