package lawer.ticketbot.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lawer.ticketbot.backend.Config;
import org.bson.Document;

public class MongoDB {

    public static MongoCollection<Document> col;
    public static MongoDatabase database;

    public static void start() {
        MongoClient mongoClient = MongoClients.create(Config.mongodblink);
        database = mongoClient.getDatabase(Config.mongodbdatabase);
    }
}