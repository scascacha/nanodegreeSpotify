package simoncr.com.spotifystreamer.acitivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
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
import simoncr.com.spotifystreamer.utils.SpotifyHandler;
import simoncr.com.spotifystreamer.utils.Utils;


public class MainActivity extends AppCompatActivity implements ConnectionStateCallback, SearchView.OnQueryTextListener,SearchView.OnCloseListener {
    private static final int REQUEST_CODE = 1209;
    private List<Artist> artistList;
    private ListView listView;
    private ArtistAdapter artistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);
        artistList = new ArrayList<Artist>();
        artistAdapter = new ArtistAdapter(this,artistList);
        listView.setAdapter(artistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showTopTracks(artistList.get(i));
            }
        });

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(SpotifyHandler.CLIENT_ID, AuthenticationResponse.Type.TOKEN,SpotifyHandler.REDIRECT_URI);
        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    private void showTopTracks(Artist artist) {
        Intent intent = new Intent(this, TopTracksActivity.class);
        intent.putExtra(TopTracksActivity.ARTIST_ID, artist.id);
        intent.putExtra(TopTracksActivity.ARTIST_NAME, artist.name);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                SpotifyHandler handler = SpotifyHandler.getInstance();
                handler.setToken(response.getAccessToken());
            }
        }
    }

    private void getArtists(String searchText) {
        final Context context = this;
        SpotifyHandler handler = SpotifyHandler.getInstance();

        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(handler.getToken());
        SpotifyService service = api.getService();
        service.searchArtists(searchText, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {
                if (artistsPager.artists.items != null && artistsPager.artists.items.size() > 0) {
                    artistList = artistsPager.artists.items;
                    Handler mHandler = new Handler(getMainLooper());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            artistAdapter.setArtists(artistList);
                        }
                    });
                } else {
                    Utils.showMessage(getString(R.string.no_artist_found), context);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Artists Search Error: ", error.getLocalizedMessage());
                Utils.showMessage(getString(R.string.api_error),context);
            }
        });
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
}
