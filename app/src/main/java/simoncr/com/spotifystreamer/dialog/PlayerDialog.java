package simoncr.com.spotifystreamer.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.PlayerStateCallback;
import com.spotify.sdk.android.player.Spotify;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import simoncr.com.spotifystreamer.R;
import simoncr.com.spotifystreamer.model.TrackParcelable;
import simoncr.com.spotifystreamer.utils.SpotifyHandler;

/**
 * Created by scascacha on 6/12/15.
 */
public class PlayerDialog extends DialogFragment implements ConnectionStateCallback,PlayerNotificationCallback {

    public static final String TRACK = "track";
    public static final String TRACK_LIST = "trackList";

    @InjectView(R.id.albumName) TextView albumNameLabel;
    @InjectView(R.id.trackName) TextView trackNameLabel;
    @InjectView(R.id.artistLabel) TextView artistNameLabel;
    @InjectView(R.id.startTime) TextView startTimeLabel;
    @InjectView(R.id.endTime) TextView endTimeLabel;
    @InjectView(R.id.seekBar) SeekBar seekBar;
    @InjectView(R.id.albumImage) ImageView albumImage;
    @InjectView(R.id.playButton) Button playButton;
    @InjectView(R.id.pauseButton) Button pauseButton;
    @InjectView(R.id.previousButton) Button prevButton;
    @InjectView(R.id.nextButton) Button nextButton;

    TrackParcelable currentTrack;
    ArrayList<TrackParcelable> trackList;
    Boolean playing;
    int currentPosition;

    Player player;

    private Handler mHandler = new Handler();

    private PlayCallback playCallback;

    public interface PlayCallback {
        public void trackChanged(TrackParcelable track);
    }

    public void setPlayCallback(PlayCallback playCallback) {
        this.playCallback = playCallback;
    }

    public void setCurrentTrack(TrackParcelable currentTrack) {
        this.currentTrack = currentTrack;
    }

    public void setTrackList(ArrayList<TrackParcelable> trackList) {
        this.trackList = trackList;
    }

    public PlayerDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.play_track_fragment, null);
        ButterKnife.inject(this,dialogView);

        if (savedInstanceState != null) {
            trackList = savedInstanceState.getParcelableArrayList(TRACK_LIST);
            currentTrack = savedInstanceState.getParcelable(TRACK);
        }

        for (int i=0;i<trackList.size();i++) {
            TrackParcelable track = trackList.get(i);
            if (track.uri.equals(currentTrack.uri)) {
                currentPosition = i;
                break;
            }
        }


        updatePlayback(false);

        SpotifyHandler handler = SpotifyHandler.getInstance();
        Config config = new Config(getActivity(),handler.getToken(),SpotifyHandler.CLIENT_ID);
        player = Spotify.getPlayer(config, getActivity(), new Player.InitializationObserver() {
            @Override
            public void onInitialized(Player player) {
                player.addConnectionStateCallback(PlayerDialog.this);
                player.addPlayerNotificationCallback(PlayerDialog.this);
                player.play(currentTrack.uri);
                updatePlayback(true);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("PlayerFragment", "Could not initialize player: " + throwable.getMessage());
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //player.seekToPosition(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (player != null) {
                    player.getPlayerState(new PlayerStateCallback() {
                        @Override
                        public void onPlayerState(PlayerState playerState) {
                            if (seekBar.getMax() != playerState.durationInMs) {
                                seekBar.setMax(playerState.durationInMs);
                            }
                            seekBar.setProgress(playerState.positionInMs);
                            startTimeLabel.setText(msToTime(playerState.positionInMs));
                            endTimeLabel.setText(msToTime(playerState.durationInMs - playerState.positionInMs));
                        }
                    });
                }
                mHandler.postDelayed(this, 1000);
            }
        });

        builder.setView(dialogView);

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(TRACK_LIST,trackList);
        outState.putParcelable(TRACK,currentTrack);
        super.onSaveInstanceState(outState);
    }

    private String msToTime(int millis) {
        if (TimeUnit.MILLISECONDS.toHours(millis) > 0) {
            return String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        } else {
            return String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        }
    }

    @OnClick(R.id.nextButton)
    public void nextTrack() {
        player.skipToNext();
    }

    @OnClick(R.id.previousButton)
    public void previousTrack() {
        player.skipToPrevious();
    }

    @OnClick(R.id.pauseButton)
    public void pauseTrack() {
        player.pause();
    }

    @OnClick(R.id.playButton)
    public void playTrack() {
        player.resume();
    }

    private void updatePlayback(Boolean isPlaying) {
        playing = isPlaying;

        if (playing) {
            playButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);
        } else {
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
        }

        if (currentPosition <= 0) {
            currentPosition = 0;
            prevButton.setEnabled(false);
            nextButton.setEnabled(true);
        } else if (currentPosition >= trackList.size()) {
            currentPosition = trackList.size()-1;
            prevButton.setEnabled(true);
            nextButton.setEnabled(false);
        } else {
            prevButton.setEnabled(true);
            nextButton.setEnabled(true);
        }

        currentTrack = trackList.get(currentPosition);
        if (playCallback != null) {
            playCallback.trackChanged(currentTrack);
        }

        if (currentTrack.album.image != null) {
            Picasso.with(getActivity())
                    .load(currentTrack.album.image.url)
                    .placeholder(R.drawable.spotify_logo)
                    .into(albumImage);
        } else {
            albumImage.setImageResource(R.drawable.spotify_logo);
        }
        albumNameLabel.setText(currentTrack.album.name);
        trackNameLabel.setText(currentTrack.name);
        artistNameLabel.setText(currentTrack.artistName);

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
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        seekBar.setMax(playerState.durationInMs);
        seekBar.setProgress(playerState.positionInMs);
        switch (eventType){
            case PLAY:
                updatePlayback(true);
                break;
            case PAUSE:
                updatePlayback(false);
                break;
            case END_OF_CONTEXT:
            case SKIP_NEXT:
                currentPosition++;
                updatePlayback(playerState.playing);
                break;
            case SKIP_PREV:
                currentPosition--;
                updatePlayback(playerState.playing);
                break;
        }
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String s) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        player.pause();
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroy() {
        Spotify.destroyPlayer(player);
        super.onDestroy();
    }
}
