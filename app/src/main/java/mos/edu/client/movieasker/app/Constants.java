package mos.edu.client.movieasker.app;

public final class Constants {

    public static final long REGISTERED_USER_ID = 1L;

    public static final class TAB {
        public static final int NEW_POSITION = 0;
        public static final int FAVORITE_POSITION = 1;
        public static final int LOOKED_POSITION = 2;
    }

    public static final int FIRST_PAGE_NUMBER = 0;
    public static final int ELEMENTS_ON_PAGE = 30;

    public static final class DATABASE {
        public static final String NAME = "movie_asker_db.db";
        public static final int VERSION = 1;
    }

    public static final class URI {
        private static final String HOST = "http://192.168.57.1:8080";
        private static final String API = "/fancinema";
        private static final String HOST_API = HOST + API;

        public static final String POSTERS = HOST;

        public static final String USERS = HOST_API + "/users";
        public static final String USER_BY_LOGIN = USERS + "?login={login}";
        public static final String USER_FAVORITE =
                USERS + "/{id_user}/favorite?page={page}&size={size}";
        public static final String USER_LOOKED =
                USERS + "/{id_user}/favorite?looked=1&page={page}&size={size}";

        private static final String SHORT_FILMS = HOST_API + "/films";
        public static final String FILMS = SHORT_FILMS + "?page={page}&size={size}";
        public static final String FILM_BY_ID = SHORT_FILMS + "/{id}";
    }

}
