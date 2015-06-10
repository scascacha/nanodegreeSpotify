package simoncr.com.spotifystreamer.acitivity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import simoncr.com.spotifystreamer.R;
import simoncr.com.spotifystreamer.adapter.TrackAdapter;
import simoncr.com.spotifystreamer.utils.SpotifyHandler;

/**
 * Created by scascacha on 6/9/15.
 */
public class TopTracksActivity extends AppCompatActivity {
    public static final String ARTIST_ID = "artistId";

    ListView listView;
    TrackAdapter trackAdapter;
    List<Track> trackList;
    String artistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_tracks_activity);

        artistId = getIntent().getStringExtra(ARTIST_ID);

        listView = (ListView)findViewById(R.id.listView);

        trackList = new ArrayList<Track>();
        trackAdapter = new TrackAdapter(this,trackList);
        listView.setAdapter(trackAdapter);

        getArtistTopTracks();
    }

    private void getArtistTopTracks() {
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
                    trackList = tracks.tracks;
                    Handler mHandler = new Handler(getMainLooper());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            trackAdapter.setTrackList(trackList);
                        }
                    });
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("Track error: ", error.getLocalizedMessage());
                }
            });
        }
    }
}
