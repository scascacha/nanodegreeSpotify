package simoncr.com.spotifystreamer.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by scascacha on 6/9/15.
 */
public class Utils {
    public static void showMessage(final String message, final Context context) {
        Handler mHandler = new Handler(context.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
