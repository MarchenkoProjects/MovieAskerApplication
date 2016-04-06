package mos.edu.client.movieasker;

public final class Constants {

    public static final int NEW_TAB_POSITION = 0;
    public static final int FAVORITE_TAB_POSITION = 1;
    public static final int LOOKED_TAB_POSITION = 2;

    public static final class DATABASE {
        public static final String NAME = "movie_asker_db.db";
        public static final int VERSION = 1;
    }

    public static final class URI {
        private static final String HOST = "http://192.168.57.1:8080";

        public static final String POSTERS_URI = HOST + "/posters/";
    }

}
