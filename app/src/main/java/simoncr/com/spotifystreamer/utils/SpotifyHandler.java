package simoncr.com.spotifystreamer.utils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import simoncr.com.spotifystreamer.R;
import simoncr.com.spotifystreamer.model.ArtistParcelable;
import simoncr.com.spotifystreamer.model.TrackParcelable;

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

    public interface SpotifyCallback<T> {
        void success(ArrayList<T> list);
        void error(RetrofitError error);
    }

    public void getArtists(String searchText, final Context context, final SpotifyCallback<ArtistParcelable> callback) {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(getToken());
        SpotifyService service = api.getService();
        service.searchArtists(searchText, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {
                if (artistsPager.artists.items != null && artistsPager.artists.items.size() > 0) {
                    callback.success(ArtistParcelable.getParcelableList(artistsPager.artists.items));
                } else {
                    callback.success(null);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Artists Search Error: ", error.getLocalizedMessage());
                callback.error(error);
            }
        });
    }

    public void getArtistTopTracks(String artistId, final Context context, final SpotifyCallback<TrackParcelable> callback) {
        if (artistId != null) {

            SpotifyHandler handler = SpotifyHandler.getInstance();

            Map<String,Object> params = new HashMap<String,Object>();
            params.put("country", "US");

            SpotifyApi api = new SpotifyApi();
            api.setAccessToken(handler.getToken());
            SpotifyService service = api.getService();

            service.getArtistTopTrack(artistId, params, new Callback<Tracks>() {
                @Override
                public void success(Tracks tracks, Response response) {
                    callback.success(TrackParcelable.getParcelableList(tracks.tracks));
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("Track error: ", error.getLocalizedMessage());
                   callback.error(error);
                }
            });
        }
    }

}
