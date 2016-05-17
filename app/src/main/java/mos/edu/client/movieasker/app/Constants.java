package mos.edu.client.movieasker.app;

public final class Constants {

    public static final long REGISTERED_USER_ID = 1L;

    public static final class TAB {
        public static final int NEW_ITEM = 0;
        public static final int FAVORITE_ITEM = 1;
        public static final int LOOKED_ITEM = 2;
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

        private static final String FILMS_PATH = "/films";
        private static final String FILM_PARAM = "/{id_film}";
        private static final String USERS_PATH = "/users";
        private static final String USER_PARAM = "/{id_user}";
        private static final String LOGIN_PARAM = "login={login}";
        private static final String FAVORITE_PATH = "/favorite";
        private static final String LOOKED_PARAM = "looked={looked}";
        private static final String REVIEW_PATH = "/review";


        private static final String START_PARAM_DELIMITER = "?";
        private static final String PARAM_DELIMITER = "&";
        private static final String PAGE_SIZE_PARAM = "page={page}" + PARAM_DELIMITER + "size={size}";

        public static final String POSTERS = HOST;


        public static final String FILMS = HOST + API + FILMS_PATH + START_PARAM_DELIMITER + PAGE_SIZE_PARAM;
        public static final String FILM_BY_ID = HOST + API + FILMS_PATH + FILM_PARAM;

        public static final String USERS = HOST + API + USERS_PATH;
        public static final String USER_BY_LOGIN = USERS + START_PARAM_DELIMITER + LOGIN_PARAM;
        public static final String USER_FAVORITE = USERS + USER_PARAM + FAVORITE_PATH + START_PARAM_DELIMITER + PAGE_SIZE_PARAM;
        public static final String USER_LOOKED = USERS + USER_PARAM + FAVORITE_PATH + START_PARAM_DELIMITER + LOOKED_PARAM + PARAM_DELIMITER + PAGE_SIZE_PARAM;

        public static final String POST_USER_FAVORITE = USERS + USER_PARAM + FILMS_PATH + FILM_PARAM + FAVORITE_PATH;
        public static final String POST_USER_LOOKED = POST_USER_FAVORITE + START_PARAM_DELIMITER + LOOKED_PARAM;
        public static final String POST_USER_REVIEW = USERS + USER_PARAM + FILMS_PATH + FILM_PARAM + REVIEW_PATH;
    }

}
