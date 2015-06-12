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
    public String uri;
    public String preview_url;
    public String type;
    public Boolean explicit;
    public long duration_ms;
    public Boolean is_playable;
    public AlbumParcelable album;
    public String artistName;

    public TrackParcelable(Track track) {
        name = track.name;
        uri = track.uri;
        preview_url = track.preview_url;
        type = track.type;
//        explicit = track.explicit;
        duration_ms = track.duration_ms;
//        is_playable = track.is_playable;
        album = new AlbumParcelable(track.album);
        artistName = track.artists.get(0).name;
    }

    public TrackParcelable(Parcel in) {
        name = in.readString();
        uri = in.readString();
        preview_url = in.readString();
        type = in.readString();
        duration_ms = in.readLong();
//        explicit = in.readByte() != 0;
//        is_playable = in.readByte() != 0;
        album = in.readParcelable(AlbumParcelable.class.getClassLoader());
        artistName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(name);
        out.writeString(uri);
        out.writeString(preview_url);
        out.writeString(type);
        out.writeLong(duration_ms);
//        out.writeByte((byte) (explicit ? 1 : 0));
//        out.writeByte((byte) (is_playable ? 1 : 0));
        out.writeParcelable(album,PARCELABLE_WRITE_RETURN_VALUE);
        out.writeString(artistName);
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
