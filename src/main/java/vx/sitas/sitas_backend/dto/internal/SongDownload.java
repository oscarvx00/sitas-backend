package vx.sitas.sitas_backend.dto.internal;

public class SongDownload {

    private String downloadId;
    private String songName;
    private String status;
    private String storageNodeName;
    private String userId;
    private boolean stored;

    private String downloadName;

    public SongDownload(String downloadId, String songName, String status, String storageNodeName, String userId, boolean stored, String downloadName) {
        this.downloadId = downloadId;
        this.songName = songName;
        this.status = status;
        this.storageNodeName = storageNodeName;
        this.userId = userId;
        this.stored = stored;
        this.downloadName = downloadName;
    }

    public String getDownloadId() {
        return downloadId;
    }

    public String getSongName() {
        return songName;
    }

    public String getStatus() {
        return status;
    }

    public String getStorageNodeName() {
        return storageNodeName;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isStored() {
        return stored;
    }

    public String getDownloadName() {
        return downloadName;
    }
}
