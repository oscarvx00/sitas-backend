package vx.sitas.sitas_backend.dto.rabbit;

import java.util.UUID;

public class RabbitDownloadRequest {

    private String userId;
    private String downloadId;
    private String songName;
    private boolean spotify = false;
    private boolean soundcloud = false;
    private boolean youtube = false;
    private boolean direct;

    public RabbitDownloadRequest(String songName, String userId){
        this.songName = songName;
        this.userId = userId;
        this.downloadId = UUID.randomUUID().toString();
    }

    public void setSpotify(boolean spotify) {
        this.spotify = spotify;
    }

    public void setSoundcloud(boolean soundcloud) {
        this.soundcloud = soundcloud;
    }

    public void setYoutube(boolean youtube) {
        this.youtube = youtube;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

    public String getUserId() {
        return userId;
    }

    public String getDownloadId() {
        return downloadId;
    }

    public String getSongName() {
        return songName;
    }

    public boolean isSpotify() {
        return spotify;
    }

    public boolean isSoundcloud() {
        return soundcloud;
    }

    public boolean isYoutube() {
        return youtube;
    }

    public boolean isDirect() {
        return direct;
    }
}
