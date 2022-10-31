package vx.sitas.sitas_backend.service.node_storage;

public abstract class StorageNode {

    vx.sitas.sitas_backend.dto.internal.StorageNode nodeInfo;

    public StorageNode(vx.sitas.sitas_backend.dto.internal.StorageNode storageNodeInfo){
        this.nodeInfo = storageNodeInfo;
    }

    public vx.sitas.sitas_backend.dto.internal.StorageNode getNodeInfo() {
        return nodeInfo;
    }

    public abstract String getDownloadUrl(String songName);

    public static StorageNode build(vx.sitas.sitas_backend.dto.internal.StorageNode storageNode){
        switch (storageNode.getType()){
            case MINIO:
                return new MinioStorageNode(storageNode);
        }
        return null;
    }
}
