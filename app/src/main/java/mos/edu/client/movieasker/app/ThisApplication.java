package mos.edu.client.movieasker.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.springframework.web.client.RestTemplate;

import java.io.File;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.db.DaoMaster;
import mos.edu.client.movieasker.db.DaoSession;
import mos.edu.client.movieasker.db.User;
import mos.edu.client.movieasker.db.UserDao;

public final class ThisApplication extends Application {

    private static ThisApplication instance;

    public static ThisApplication getInstance() {
        return instance;
    }

    private DaoSession session;
    private RestTemplate template;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        initImageLoader();
        initDatabase();
        initRestTemplate();
    }

    @Override
    public void onTerminate() {
        ImageLoader.getInstance().destroy();
        super.onTerminate();
    }

    public DaoSession getSession() {
        return session;
    }

    public User getUser() {
        final UserDao userDao = session.getUserDao();
        return userDao.load(Constants.REGISTERED_USER_ID);
    }

    public RestTemplate getRestTemplate() {
        return template;
    }

    private void initImageLoader() {
        final File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(R.dimen.film_poster_width, R.dimen.film_poster_height)
                .threadPoolSize(3)
                .threadPriority(Thread.MIN_PRIORITY + 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheSize(5 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();

        ImageLoader.getInstance().init(config);
    }

    private void initDatabase() {
        final DaoMaster.DevOpenHelper helper =
                new DaoMaster.DevOpenHelper(this, Constants.DATABASE.NAME, null);
        final SQLiteDatabase db = helper.getWritableDatabase();
        final DaoMaster master = new DaoMaster(db);
        session = master.newSession();
    }

    private void initRestTemplate() {
        template = new RestTemplate();
    }

}
