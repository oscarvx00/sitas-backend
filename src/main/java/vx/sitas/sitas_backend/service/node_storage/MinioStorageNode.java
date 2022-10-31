package vx.sitas.sitas_backend.service.node_storage;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;

import java.util.concurrent.TimeUnit;


public class MinioStorageNode extends StorageNode {

    private String bucket;
    private MinioClient minioClient;

    MinioStorageNode(vx.sitas.sitas_backend.dto.internal.StorageNode storageNode){
        super(storageNode);
        this.bucket = storageNode.getBucket();
        this.minioClient = MinioClient.builder()
                .endpoint(storageNode.getEndpoint())
                .credentials(storageNode.getCredUser(), storageNode.getCredPass())
                .build();
    }


    @Override
    public String getDownloadUrl(String songName) {
        try{
            String url = this.minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(this.bucket)
                            .object(songName).
                            expiry(2, TimeUnit.HOURS)
                            .build()
            );
            return url;
        } catch (Exception ex){
            System.err.println("Error getting download url: " + ex.getMessage());
            return null;
        }
    }
}
