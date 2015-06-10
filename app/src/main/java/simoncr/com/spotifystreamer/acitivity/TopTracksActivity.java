package simoncr.com.spotifystreamer.acitivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import simoncr.com.spotifystreamer.R;
import simoncr.com.spotifystreamer.adapter.TrackAdapter;
import simoncr.com.spotifystreamer.model.TrackParcelable;
import simoncr.com.spotifystreamer.utils.SpotifyHandler;
import simoncr.com.spotifystreamer.utils.Utils;

/**
 * Created by scascacha on 6/9/15.
 */
public class TopTracksActivity extends AppCompatActivity {
    public static final String TRACK_LIST = "artists";
    public static final String ARTIST_ID = "artistId";
    public static final String ARTIST_NAME = "artistName";

    @InjectView(R.id.listView) ListView listView;

    TrackAdapter trackAdapter;
    ArrayList<TrackParcelable> trackList;
    String artistId;
    String artistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_tracks_activity);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {

            artistId = getIntent().getStringExtra(ARTIST_ID);
            artistName = getIntent().getStringExtra(ARTIST_NAME);

            trackList = new ArrayList<TrackParcelable>();
            getArtistTopTracks();
        } else {
            artistId = savedInstanceState.getString(ARTIST_ID);
            artistName = savedInstanceState.getString(ARTIST_NAME);

            trackList = savedInstanceState.getParcelableArrayList(TRACK_LIST);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle(artistName);
        }

        trackAdapter = new TrackAdapter(this, trackList);
        listView.setAdapter(trackAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(TRACK_LIST, trackList);
        outState.putString(ARTIST_ID,artistId);
        outState.putString(ARTIST_NAME,artistName);
        super.onSaveInstanceState(outState);
    }

    private void getArtistTopTracks() {
        if (artistId != null) {
            final Context context = this;

            SpotifyHandler handler = SpotifyHandler.getInstance();

            Map<String,Object> params = new HashMap<String,Object>();
            params.put("country", "US");

            SpotifyApi api = new SpotifyApi();
            api.setAccessToken(handler.getToken());
            SpotifyService service = api.getService();

            service.getArtistTopTrack(artistId, params, new Callback<Tracks>() {
                @Override
                public void success(Tracks tracks, Response response) {
                    if (tracks.tracks != null && tracks.tracks.size() > 0) {
                        trackList = TrackParcelable.getParcelableList(tracks.tracks);
                        Handler mHandler = new Handler(getMainLooper());
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                trackAdapter.setTrackList(trackList);
                            }
                        });
                    } else {
                        Utils.showMessage(getString(R.string.no_tracks_found),context);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("Track error: ", error.getLocalizedMessage());
                    Utils.showMessage(getString(R.string.api_error), context);
                }
            });
        }
    }
}
