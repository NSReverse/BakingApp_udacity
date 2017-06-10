package net.nsreverse.bakingapp.data.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * This class contains downloading logic.
 *
 * @author Robert
 * Created on 6/7/2017.
 */
public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String API_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    /**
     * This method gets a fresh download of the JSON containing Recipe information.
     *
     * @return A String representing the downloaded Recipe JSON.
     * @throws IOException Exception raised if there was an issue downloading the Recipe JSON.
     */
    @Nullable
    public static String getRecipesJsonFromUrl() throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        try {
            InputStream inputStream = conn.getInputStream();

            Scanner scn = new Scanner(inputStream);
            scn.useDelimiter("\\A");

            if (scn.hasNext()) {
                return scn.next();
            }
            else {
                if (ApplicationInstance.loggingEnabled) Log.e(TAG, "Error downloading JSON!");
                return null;
            }
        }
        finally {
            conn.disconnect();
        }
    }

    /**
     * This method downloads an image from the specified URL.
     *
     * @param address A String representing a remote image resource location.
     * @return A Bitmap from the specified source.
     * @throws IOException Exception raised if there was an issue downloading the Bitmap.
     */
    @SuppressWarnings("WeakerAccess")
    public static Bitmap getImageFromAddress(String address) throws IOException {
        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        try {
            InputStream inputStream = conn.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        }
        finally {
            conn.disconnect();
        }
    }
}
