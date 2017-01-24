import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

//Import AsyncFileDownloader here

public class BVCache extends LruCache<String, Object> implements ComponentCallbacks2{

    private static BVCache instance = null;
    private static int MAX_CACHE_SIZE = 1024 * 64 ; // I have set the Size to 64 MB

    public static synchronized BVCache getInstance() {
        if (instance == null) {
            instance = new BVCache(MAX_CACHE_SIZE);
        }
        return instance;
    }

    private BVCache(int maxSize) {
        super(maxSize);
    }

   /**
     * Sets the Image from URL to imageView
     * @param URL Source url for image
     * @param imageView placeholder for image
     */

    public static void setImageFromURL(String URL, ImageView imageView)
    {
        byte[] imageData = (byte[]) instance.get(URL);
        if (imageData != null) {
            //Image already exists in cache
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            imageView.setImageBitmap(bitmap);
        }
        else {
            //Image isn't cached, let's download and cache it
            new AsyncFileDownloader(imageView).execute(URL);
        }
    }

    public static Object getFileFromURL(String URL)
    {
        Object cachedObject = instance.get(URL);
        if (cachedObject != null)
        {
            //Object exists in cache
            return cachedObject;
        }
        else
        {
            //Object doesn't exist in cache, let's download and cache it
            new AsyncFileDownloader().execute(URL);
            return null;
        }
    }

    @Override
    public void onTrimMemory(int level) {
        if (level >= TRIM_MEMORY_MODERATE) {
            instance.evictAll();
        }
        else if (level >= TRIM_MEMORY_BACKGROUND) {
            instance.trimToSize(instance.size() / 2);
        }
    }

    @Override
    public void onLowMemory() {
        instance.onLowMemory();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

}
