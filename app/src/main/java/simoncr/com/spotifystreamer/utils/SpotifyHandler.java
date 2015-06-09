package simoncr.com.spotifystreamer.utils;

import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;

/**
 * Created by scascacha on 6/4/15.
 */
public class SpotifyHandler {
    public final static String CLIENT_ID = "50e7022ef10d41fabf1f0b9e1f8252ff";
    public final static String REDIRECT_URI = "simoncr.com.spotifystreamer://callback";

    private static SpotifyHandler instance = null;
    private String token;

    public SpotifyHandler() {
    }

    public static SpotifyHandler getInstance() {
        if (instance == null) {
            instance = new SpotifyHandler();
        }
        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
