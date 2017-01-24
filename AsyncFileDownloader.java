import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

//Todo Import BVCache here

/**
 * Created by Basanth on 10/8/2016.
 */

public class AsyncFileDownloader extends AsyncTask<String, String, String> {

    final int MAX_IMG_SIZE = 8192; //Max image size is assumed to be 8 MB
  
    ImageView imageView;
    String urlString;

     public AsyncFileDownloader() {}

     public AsyncFileDownloader(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {

        urlString = f_url[0];
            try {
                URL url = new URL(urlString);
                URLConnection conection = url.openConnection();
                conection.connect();
                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), MAX_IMG_SIZE);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                int nRead;
                byte[] data = new byte[MAX_IMG_SIZE]; 
                while ((nRead = input.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                input.close();

                Object object = buffer.toByteArray();
                //Caching the downloaded object
                if(BVCache.getInstance().get(urlString)==null)
                  BVCache.getInstance().put(urlString,object);
                else
                    Log.d("AsyncFileDownloader","Avoided duplicated cache");

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
        return null;
    }

    @Override
    protected void onPostExecute(String file_url) {

        if (imageView!=null)
        {
            byte[] bitmapdata =  (byte[]) BVCache.getInstance().get(urlString);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length));
        }

    }

}
