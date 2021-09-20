package lawer.ticketbot.backend;

import io.github.cdimascio.dotenv.Dotenv;
import lawer.ticketbot.database.MongoDB;
import lawer.ticketbot.utils.TicketManager;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.awt.*;

public class Config {

    private static final Dotenv dotenv = Dotenv.configure().filename("config.txt").load();
    public static String get(String key) {
        return dotenv.get(key.toUpperCase());
    }

    public static String tokenbot;
    public static String prefix;
    public static Color color = new Color(47, 49, 54);
    public static String mongodblink;
    public static String mongodbdatabase;
    public static Boolean logs;
    public static String idchatlogs;
    public static String ticketcatid;
    public static String ticketrole;
    public static String msgerror;
    public static String ticketdesc;

    public static void LoadConfig() {
        tokenbot = get("TOKENBOT");
        mongodbdatabase = get("MONGODBDATABASE");
        mongodblink = get("MONGODBLINK");
        MongoDB.start();
        Document doc = new Document("name", "TicketBot");
        MongoDB.col = MongoDB.database.getCollection("config");
        Document found = (Document) MongoDB.col.find(doc).first();
        if (found == null) {
            doc
                    .append("prefix", "/")
                    .append("logs", "false")
                    .append("chatidlogs", "0")
                    .append("ticketcatid", "0")
                    .append("ticketrole", "0")
                    .append("ticketnumber", 0)
                    .append("ticketmsgid", "0")
                    .append("ticketdesc", "0")
                    .append("msgerror", "Você não tem permissão para executar este comando!");
            MongoDB.col.insertOne(doc);
            configset();
        } else {
            configset();
        }
    }

    public static void setconfigstring(String config, String value) {
        Document playerdoc = new Document("name", "TicketBot");
        MongoDB.col = MongoDB.database.getCollection("config");
        Document found = (Document) MongoDB.col.find(playerdoc).first();
        assert found != null;
        Bson upvalue = new Document(config, value);
        Bson updanting = new Document("$set", upvalue);
        MongoDB.col.updateOne(found, updanting);
        configset();
    }

    public static String getconfigstring(String config) {
        Document doc = new Document("name", "TicketBot");
        MongoDB.col = MongoDB.database.getCollection("config");
        Document found = (Document) MongoDB.col.find(doc).first();
        assert found != null;
        return found.getString(config);
    }

    public static void setconfigint(String config, Integer value) {
        Document playerdoc = new Document("name", "TicketBot");
        MongoDB.col = MongoDB.database.getCollection("config");
        Document found = (Document) MongoDB.col.find(playerdoc).first();
        assert found != null;
        Bson upvalue = new Document(config, value);
        Bson updanting = new Document("$set", upvalue);
        MongoDB.col.updateOne(found, updanting);
        configset();
    }

    public static Integer getconfigint(String config) {
        Document doc = new Document("name", "TicketBot");
        MongoDB.col = MongoDB.database.getCollection("config");
        Document found = (Document) MongoDB.col.find(doc).first();
        assert found != null;
        return found.getInteger(config);
    }

    public static void configset() {

        prefix = getconfigstring("prefix");
        logs = Boolean.parseBoolean(getconfigstring("logs"));
        idchatlogs = getconfigstring("chatidlogs");
        ticketcatid = getconfigstring("ticketcatid");
        ticketrole = getconfigstring("ticketrole");
        msgerror = getconfigstring("msgerror");
        ticketdesc = getconfigstring("ticketdesc");

        TicketManager.ticketmsgid.put("ticket", getconfigstring("ticketmsgid"));
    }
}