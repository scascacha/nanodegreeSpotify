package simoncr.com.spotifystreamer.acitivity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.ButterKnife;
import simoncr.com.spotifystreamer.R;
import simoncr.com.spotifystreamer.fragments.PlayFragment;
import simoncr.com.spotifystreamer.model.TrackParcelable;

/**
 * Created by scascacha on 6/11/15.
 */
public class PlayActivity extends AppCompatActivity {
    PlayFragment playFragment;
    TrackParcelable track;
    ArrayList<TrackParcelable> trackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_track_activity);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            track = getIntent().getParcelableExtra(PlayFragment.TRACK);
            trackList = getIntent().getParcelableArrayListExtra(PlayFragment.TRACK_LIST);
        } else {
            track = savedInstanceState.getParcelable(PlayFragment.TRACK);
            trackList = savedInstanceState.getParcelableArrayList(PlayFragment.TRACK_LIST);
        }

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PlayFragment.TRACK_LIST,trackList);
        bundle.putParcelable(PlayFragment.TRACK, track);

        playFragment = new PlayFragment();
        playFragment.setArguments(bundle);
        playFragment.setPlayCallback(new PlayFragment.PlayCallback() {
            @Override
            public void trackChanged(TrackParcelable trackChanged) {
                track = trackChanged;
            }
        });
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.play_track,playFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(PlayFragment.TRACK,track);
        outState.putParcelableArrayList(PlayFragment.TRACK_LIST,trackList);
        super.onSaveInstanceState(outState);
    }
}
