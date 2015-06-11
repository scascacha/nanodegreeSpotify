package simoncr.com.spotifystreamer.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import simoncr.com.spotifystreamer.R;
import simoncr.com.spotifystreamer.acitivity.MainActivity;
import simoncr.com.spotifystreamer.acitivity.TopTracksActivity;
import simoncr.com.spotifystreamer.adapter.ArtistAdapter;
import simoncr.com.spotifystreamer.model.ArtistParcelable;
import simoncr.com.spotifystreamer.utils.SpotifyHandler;
import simoncr.com.spotifystreamer.utils.Utils;

/**
 * Created by scascacha on 6/10/15.
 */
public class ArtistsFragment extends Fragment {
    private ArrayList<ArtistParcelable> artistList;

    @InjectView(R.id.listView)
    ListView listView;

    private ArtistAdapter artistAdapter;

    public void setArtistList(ArrayList<ArtistParcelable> artistList) {
        this.artistList = artistList;
        artistAdapter.setArtists(artistList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.artists_fragment, container, false);
        ButterKnife.inject(this, rootView);

        Bundle args = getArguments();
        artistList = args.getParcelableArrayList(MainActivity.ARTIST_LIST);

        artistAdapter = new ArtistAdapter(getActivity(), artistList);
        listView.setAdapter(artistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showTopTracks(artistList.get(i));
            }
        });


        return rootView;
    }

    private void showTopTracks(ArtistParcelable artist) {
        Intent intent = new Intent(getActivity(), TopTracksActivity.class);
        intent.putExtra(TracksFragment.ARTIST_ID, artist.id);
        intent.putExtra(TracksFragment.ARTIST_NAME, artist.name);
        startActivity(intent);
    }
}
