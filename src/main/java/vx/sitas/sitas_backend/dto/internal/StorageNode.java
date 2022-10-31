package vx.sitas.sitas_backend.dto.internal;

public class StorageNode {
    private String name;
    private boolean stable;
    private String bucket;
    private StorageNodeType type;
    private String endpoint;
    private String credUser;
    private String credPass;

    public StorageNode(String name, boolean stable, String bucket, StorageNodeType type, String endpoint, String credUser, String credPass) {
        this.name = name;
        this.stable = stable;
        this.bucket = bucket;
        this.type = type;
        this.endpoint = endpoint;
        this.credUser = credUser;
        this.credPass = credPass;
    }

    public StorageNodeType getType() {
        return type;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getCredUser() {
        return credUser;
    }

    public String getCredPass() {
        return credPass;
    }

    public String getBucket() {
        return bucket;
    }

    public String getName() {
        return name;
    }
}
