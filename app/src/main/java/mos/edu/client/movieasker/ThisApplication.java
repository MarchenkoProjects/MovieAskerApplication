package mos.edu.client.movieasker;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import mos.edu.client.movieasker.db.DaoMaster;
import mos.edu.client.movieasker.db.DaoSession;

public final class ThisApplication extends Application {

    private static ThisApplication instance;

    public static ThisApplication getInstance() {
        return instance;
    }

    private DaoSession session;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        initImageLoader();
        initDatabase();
    }

    @Override
    public void onTerminate() {
        ImageLoader.getInstance().destroy();
        super.onTerminate();
    }

    public DaoSession getSession() {
        return session;
    }

    private void initImageLoader() {
        final File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());

        final DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        final ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(R.dimen.poster_width, R.dimen.poster_height)
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
                .defaultDisplayImageOptions(options)
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

}
