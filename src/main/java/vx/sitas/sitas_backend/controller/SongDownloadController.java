package vx.sitas.sitas_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import vx.sitas.sitas_backend.dto.internal.CustomOAuth2User;
import vx.sitas.sitas_backend.dto.internal.SongDownloadListDTO;
import vx.sitas.sitas_backend.dto.internal.StringResponse;
import vx.sitas.sitas_backend.service.SongDownloadService;

import java.util.List;

@RestController
public class SongDownloadController {

    @Autowired
    SongDownloadService songDownloadService;

    @GetMapping("/download")
    public List<SongDownloadListDTO> getSongDownloads(@AuthenticationPrincipal DefaultOAuth2User principal) throws Exception{
        return songDownloadService.getSongDownloads(new CustomOAuth2User(principal.getAttributes(), principal.getName()).getIdentifier());
    }

    @GetMapping("/download/{downloadId}")
    public StringResponse getDownloadUrl(@PathVariable(value = "downloadId") String downloadId) throws Exception{
        return songDownloadService.getSongDownloadUrl(downloadId);
    }
}
