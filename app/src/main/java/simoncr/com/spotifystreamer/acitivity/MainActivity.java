package simoncr.com.spotifystreamer.acitivity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Artists;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import simoncr.com.spotifystreamer.R;
import simoncr.com.spotifystreamer.adapter.ArtistAdapter;
import simoncr.com.spotifystreamer.fragments.ArtistsFragment;
import simoncr.com.spotifystreamer.model.ArtistParcelable;
import simoncr.com.spotifystreamer.utils.SpotifyHandler;
import simoncr.com.spotifystreamer.utils.Utils;


public class MainActivity extends AppCompatActivity implements ConnectionStateCallback, SearchView.OnQueryTextListener,SearchView.OnCloseListener {

    public static final String ARTIST_LIST = "artists";
    private static final int REQUEST_CODE = 1209;

    private ArrayList<ArtistParcelable> artistList;
    ArtistsFragment artistsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            artistList = new ArrayList<ArtistParcelable>();


            SpotifyHandler handler = SpotifyHandler.getInstance();
            if (handler.getToken() == null) {
                AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(SpotifyHandler.CLIENT_ID, AuthenticationResponse.Type.TOKEN, SpotifyHandler.REDIRECT_URI);
                builder.setScopes(new String[]{"streaming"});
                AuthenticationRequest request = builder.build();
                AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
            }
        } else {
            artistList = savedInstanceState.getParcelableArrayList(ARTIST_LIST);
        }

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARTIST_LIST,artistList);

        artistsFragment = new ArtistsFragment();
        artistsFragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.artists_list, artistsFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(ARTIST_LIST, artistList);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                SpotifyHandler handler = SpotifyHandler.getInstance();
                handler.setToken(response.getAccessToken());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);;

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoggedIn() {

    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(Throwable throwable) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public boolean onClose() {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        getArtists(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        //getArtists(s);
        return false;
    }

    private void getArtists(String searchText) {
        final Context context = this;
        SpotifyHandler handler = SpotifyHandler.getInstance();

        handler.getArtists(searchText, context, new SpotifyHandler.SpotifyCallback<ArtistParcelable>() {
            @Override
            public void success(ArrayList<ArtistParcelable> list) {
                artistList = list;
                Handler mHandler = new Handler(context.getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        artistsFragment.setArtistList(artistList);
                    }
                });
                if (list == null || list.size() <= 0) {

                    Utils.showMessage(getString(R.string.no_artist_found), context);
                }
            }

            @Override
            public void error(RetrofitError error) {
                Utils.showMessage(getString(R.string.api_error), context);
            }
        });
    }
}
