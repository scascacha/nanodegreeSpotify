package simoncr.com.spotifystreamer.model;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by scascacha on 6/10/15.
 */
public class ImageParcelable implements Parcelable {
    public String url;

    public ImageParcelable(Image image) {
        url = image.url;
    }

    public ImageParcelable(Parcel in) {
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(url);
    }

    public static final Parcelable.Creator<ImageParcelable> CREATOR = new Parcelable.Creator<ImageParcelable>() {
        public ImageParcelable createFromParcel(Parcel in) {
            return new ImageParcelable(in);
        }

        public ImageParcelable[] newArray(int size) {
            return new ImageParcelable[size];
        }
    };
}
