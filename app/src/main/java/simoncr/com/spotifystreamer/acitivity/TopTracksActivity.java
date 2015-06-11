package simoncr.com.spotifystreamer.acitivity;

import android.app.FragmentManager;
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
import simoncr.com.spotifystreamer.fragments.TracksFragment;
import simoncr.com.spotifystreamer.model.TrackParcelable;
import simoncr.com.spotifystreamer.utils.SpotifyHandler;
import simoncr.com.spotifystreamer.utils.Utils;

/**
 * Created by scascacha on 6/9/15.
 */
public class TopTracksActivity extends AppCompatActivity {

    TracksFragment tracksFragment;
    ArrayList<TrackParcelable> trackList;
    String artistId;
    String artistName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_tracks_activity);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {

            artistId = getIntent().getStringExtra(TracksFragment.ARTIST_ID);
            artistName = getIntent().getStringExtra(TracksFragment.ARTIST_NAME);

            trackList = new ArrayList<TrackParcelable>();
            getArtistTopTracks();
        } else {
            artistId = savedInstanceState.getString(TracksFragment.ARTIST_ID);
            artistName = savedInstanceState.getString(TracksFragment.ARTIST_NAME);

            trackList = savedInstanceState.getParcelableArrayList(TracksFragment.TRACK_LIST);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle(artistName);
        }

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(TracksFragment.TRACK_LIST,trackList);

        tracksFragment = new TracksFragment();
        tracksFragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.tracks_list, tracksFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(TracksFragment.TRACK_LIST, trackList);
        outState.putString(TracksFragment.ARTIST_ID,artistId);
        outState.putString(TracksFragment.ARTIST_NAME,artistName);
        super.onSaveInstanceState(outState);
    }

    private void getArtistTopTracks() {
        final Context context = getApplication();
        SpotifyHandler handler = SpotifyHandler.getInstance();
        handler.getArtistTopTracks(artistId, context, new SpotifyHandler.SpotifyCallback<TrackParcelable>() {
            @Override
            public void success(ArrayList<TrackParcelable> list) {
                trackList = list;
                Handler mHandler = new Handler(getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tracksFragment.setTrackList(trackList);
                    }
                });
                if (trackList == null || trackList.size() <= 0) {
                    Utils.showMessage(getString(R.string.no_tracks_found),context);
                }
            }

            @Override
            public void error(RetrofitError error) {
                Utils.showMessage(getString(R.string.api_error), context);
            }
        });
    }
}
