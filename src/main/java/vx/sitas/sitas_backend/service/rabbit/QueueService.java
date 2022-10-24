package vx.sitas.sitas_backend.service.rabbit;

import org.springframework.stereotype.Service;
import vx.sitas.sitas_backend.dto.rabbit.RabbitDownloadRequest;

@Service
public interface QueueService {

    void sendRequest(RabbitDownloadRequest downloadRequest, String selectedModule);

}
