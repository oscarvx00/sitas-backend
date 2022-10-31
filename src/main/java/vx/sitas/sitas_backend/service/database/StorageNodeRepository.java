package vx.sitas.sitas_backend.service.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import vx.sitas.sitas_backend.dto.mongo.StorageNodePOJO;

public interface StorageNodeRepository extends MongoRepository<StorageNodePOJO, String> {
}
