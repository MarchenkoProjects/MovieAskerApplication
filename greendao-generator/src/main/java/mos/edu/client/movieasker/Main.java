package mos.edu.client.movieasker;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Main {
    private static final int DB_VERSION = 1;
    private static final String DEFAULT_PACKAGE = "mos.edu.client.movieasker.db";
    private static final String OUT_DIRECTORY = "./app/src/main/java";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(DB_VERSION, DEFAULT_PACKAGE);
        createDatabase(schema);

        new DaoGenerator().generateAll(schema, OUT_DIRECTORY);
    }

    private static void createDatabase(Schema schema) {
        Entity user = schema.addEntity("User");
        user.addIdProperty();
        user.addIntProperty("globalId").notNull();
        user.addStringProperty("login").notNull();
        user.addStringProperty("password").notNull();
        user.addStringProperty("email").notNull();
    }

}
