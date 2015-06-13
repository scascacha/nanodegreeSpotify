package simoncr.com.spotifystreamer.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;
import simoncr.com.spotifystreamer.R;
import simoncr.com.spotifystreamer.adapter.TrackAdapter;
import simoncr.com.spotifystreamer.dialog.PlayerDialog;
import simoncr.com.spotifystreamer.model.TrackParcelable;
import simoncr.com.spotifystreamer.utils.SpotifyHandler;
import simoncr.com.spotifystreamer.utils.Utils;

/**
 * Created by scascacha on 6/10/15.
 */
public class TracksFragment extends Fragment {
    public static final String TRACK_LIST = "tracks";
    public static final String ARTIST_ID = "artistId";
    public static final String ARTIST_NAME = "artistName";

    @InjectView(R.id.listView)
    ListView listView;

    TrackAdapter trackAdapter;
    ArrayList<TrackParcelable> trackList;

    private TracksCallbak tracksCallbak;

    public void setTracksCallbak(TracksCallbak tracksCallbak) {
        this.tracksCallbak = tracksCallbak;
    }

    public interface TracksCallbak {
        public void didGetTracks(ArrayList<TrackParcelable> tracks);
    }

    public void setTrackList(ArrayList<TrackParcelable> tracks) {
        trackList = tracks;
        if (trackAdapter != null) {
            trackAdapter.setTrackList(trackList);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.top_tracks_fragment,container,false);
        ButterKnife.inject(this, rootView);

        Bundle args = getArguments();
        if (args != null) {
            trackList = args.getParcelableArrayList(TRACK_LIST);
        }

        trackAdapter = new TrackAdapter(getActivity(), trackList);
        listView.setAdapter(trackAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PlayerDialog playerDialog = new PlayerDialog();
                playerDialog.setTrackList(trackList);
                playerDialog.setCurrentTrack(trackList.get(i));
                playerDialog.show(getFragmentManager(),"player");
            }
        });

        return rootView;
    }

    public void getArtistTopTracks(String artistId, final Context context) {
        SpotifyHandler handler = SpotifyHandler.getInstance();
        handler.getArtistTopTracks(artistId, context, new SpotifyHandler.SpotifyCallback<TrackParcelable>() {
            @Override
            public void success(ArrayList<TrackParcelable> list) {
                trackList = list;
                Handler mHandler = new Handler(context.getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (tracksCallbak != null) {
                            tracksCallbak.didGetTracks(trackList);
                        }
                    }
                });
                if (trackList == null || trackList.size() <= 0) {
                    Utils.showMessage(getString(R.string.no_tracks_found), context);
                }
            }

            @Override
            public void error(RetrofitError error) {
                Utils.showMessage(getString(R.string.api_error), context);
            }
        });
    }
}
