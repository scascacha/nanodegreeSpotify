package simoncr.com.spotifystreamer.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import simoncr.com.spotifystreamer.R;
import simoncr.com.spotifystreamer.acitivity.PlayActivity;
import simoncr.com.spotifystreamer.adapter.TrackAdapter;
import simoncr.com.spotifystreamer.model.TrackParcelable;

/**
 * Created by scascacha on 6/10/15.
 */
public class TracksFragment extends Fragment {
    public static final String TRACK_LIST = "artists";
    public static final String ARTIST_ID = "artistId";
    public static final String ARTIST_NAME = "artistName";

    @InjectView(R.id.listView)
    ListView listView;

    TrackAdapter trackAdapter;
    ArrayList<TrackParcelable> trackList;

    public void setTrackList(ArrayList<TrackParcelable> trackList) {
        this.trackList = trackList;
        trackAdapter.setTrackList(this.trackList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.top_tracks_fragment,container,false);
        ButterKnife.inject(this, rootView);

        Bundle args = getArguments();
        trackList = args.getParcelableArrayList(TRACK_LIST);

        trackAdapter = new TrackAdapter(getActivity(), trackList);
        listView.setAdapter(trackAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), PlayActivity.class);
                intent.putExtra(PlayFragment.TRACK,trackList.get(i));
                intent.putExtra(PlayFragment.TRACK_LIST,trackList);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
