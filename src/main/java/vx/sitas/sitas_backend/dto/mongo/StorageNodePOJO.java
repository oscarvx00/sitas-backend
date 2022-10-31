package vx.sitas.sitas_backend.dto.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import vx.sitas.sitas_backend.dto.internal.StorageNode;
import vx.sitas.sitas_backend.dto.internal.StorageNodeType;

@Document("StorageNode")
public class StorageNodePOJO {

    @Id
    private String _id;
    private String name;
    private boolean stable;
    private String bucket;
    private String type;
    private String endpoint;
    private String credUser;
    private String credPass;


    public StorageNode toStorageNode(){
        return new StorageNode(
                this.name,
                this.stable,
                this.bucket,
                StorageNodeType.valueOf(this.type),
                this.endpoint,
                this.credUser,
                this.credPass
        );
    }
}
