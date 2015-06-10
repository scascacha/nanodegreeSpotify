package simoncr.com.spotifystreamer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by scascacha on 6/10/15.
 */
public class ArtistParcelable implements Parcelable {
    public String name;
    public String id;
    public ImageParcelable image;

    public ArtistParcelable(Artist artist) {
        name = artist.name;
        id = artist.id;
        if (artist.images != null && artist.images.size() > 0) {
            Image artistImage = artist.images.get(0);
            if (artistImage != null) {
                image = new ImageParcelable(artistImage);
            }
        }
    }

    public ArtistParcelable(Parcel in) {
        name = in.readString();
        id = in.readString();
        image = in.readParcelable(ImageParcelable.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(id);
        parcel.writeParcelable(image,PARCELABLE_WRITE_RETURN_VALUE);
    }

    public static ArrayList<ArtistParcelable> getParcelableList(List<Artist> list) {
        ArrayList<ArtistParcelable> artists = new ArrayList<ArtistParcelable>();
        for (Artist artist : list) {
            artists.add(new ArtistParcelable(artist));
        }

        return artists;
    }

    public static final Parcelable.Creator<ArtistParcelable> CREATOR = new Parcelable.Creator<ArtistParcelable>() {
        public ArtistParcelable createFromParcel(Parcel in) {
            return new ArtistParcelable(in);
        }

        public ArtistParcelable[] newArray(int size) {
            return new ArtistParcelable[size];
        }
    };
}
