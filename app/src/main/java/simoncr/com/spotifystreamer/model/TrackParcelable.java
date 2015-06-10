package simoncr.com.spotifystreamer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by scascacha on 6/10/15.
 */
public class TrackParcelable implements Parcelable {
    public String name;
    public AlbumParcelable album;

    public TrackParcelable(Track track) {
        name = track.name;
        album = new AlbumParcelable(track.album);
    }

    public TrackParcelable(Parcel in) {
        name = in.readString();
        album = in.readParcelable(AlbumParcelable.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(name);
        out.writeParcelable(album,PARCELABLE_WRITE_RETURN_VALUE);
    }

    public static ArrayList<TrackParcelable> getParcelableList(List<Track> list) {
        ArrayList<TrackParcelable> tracks = new ArrayList<TrackParcelable>();
        for (Track track : list) {
            tracks.add(new TrackParcelable(track));
        }

        return tracks;
    }

    public static final Parcelable.Creator<TrackParcelable> CREATOR = new Parcelable.Creator<TrackParcelable>() {
        public TrackParcelable createFromParcel(Parcel in) {
            return new TrackParcelable(in);
        }

        public TrackParcelable[] newArray(int size) {
            return new TrackParcelable[size];
        }
    };
}
