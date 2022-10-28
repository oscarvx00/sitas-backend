package vx.sitas.sitas_backend.service.database;

import com.mongodb.client.*;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vx.sitas.sitas_backend.dto.internal.SongDownload;
import vx.sitas.sitas_backend.dto.mongo.SongDownloadPOJO;

import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

@Service
public class MongoManager implements DAO{

    private MongoClient client;
    private MongoDatabase database;

    @Autowired
    public MongoManager (){
        /*CodecProvider codecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry codecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(codecProvider));
        this.client = MongoClients.create(System.getenv("MONGODB_ENDPOINT"));
        this.database = client.getDatabase("MONGODB_DATABASE").withCodecRegistry(codecRegistry);*/
    }
    @Override
    public List<SongDownload> getSongDownloads(String userId) {
        MongoCollection<SongDownloadPOJO> collection = database.getCollection("SongDownload", SongDownloadPOJO.class);
        MongoCursor<SongDownloadPOJO> cursor = collection.find(eq("userId", userId)).iterator();
        List<SongDownload> songDownloads = new ArrayList<>();
        while(cursor.hasNext()){
            SongDownload songDownload = cursor.next().toSongDownload();
            if(songDownload != null){
                songDownloads.add(songDownload);
            }
        }
        return songDownloads;
    }
}
