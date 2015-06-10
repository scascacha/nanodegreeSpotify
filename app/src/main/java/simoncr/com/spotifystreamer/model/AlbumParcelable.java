package simoncr.com.spotifystreamer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.picasso.Picasso;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by scascacha on 6/10/15.
 */
public class AlbumParcelable implements Parcelable {
    public String name;
    public ImageParcelable image;

    public AlbumParcelable(AlbumSimple album) {
        name = album.name;
        if (album.images != null && album.images.size() > 0) {
            Image albumImage = album.images.get(0);
            if (albumImage != null) {
                image = new ImageParcelable(albumImage);
            }
        }
    }

    public AlbumParcelable(Parcel in) {
        name = in.readString();
        image = in.readParcelable(ImageParcelable.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeParcelable(image,PARCELABLE_WRITE_RETURN_VALUE);
    }

    public static final Parcelable.Creator<AlbumParcelable> CREATOR = new Parcelable.Creator<AlbumParcelable>() {
        public AlbumParcelable createFromParcel(Parcel in) {
            return new AlbumParcelable(in);
        }

        public AlbumParcelable[] newArray(int size) {
            return new AlbumParcelable[size];
        }
    };
}
