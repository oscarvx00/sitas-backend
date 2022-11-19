package vx.sitas.sitas_backend.service.queue;

import org.springframework.stereotype.Service;
import vx.sitas.sitas_backend.dto.queue.QueueDownloadRequest;

@Service
public interface QueueService {

    void sendRequest(QueueDownloadRequest downloadRequest);

}
