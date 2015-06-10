package simoncr.com.spotifystreamer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import simoncr.com.spotifystreamer.R;
import simoncr.com.spotifystreamer.model.TrackParcelable;

/**
 * Created by scascacha on 6/9/15.
 */
public class TrackAdapter extends ArrayAdapter<TrackParcelable> {
    private List<TrackParcelable> trackList;

    public TrackAdapter(Context context, List<TrackParcelable> trackList) {
        super(context, R.layout.track_item, trackList);
        this.trackList = trackList;
    }

    public void setTrackList(List<TrackParcelable> trackList) {
        this.trackList = trackList;
        notifyDataSetChanged();
    }

    @Override
    public TrackParcelable getItem(int position) {
        return trackList.get(position);
    }

    @Override
    public int getCount() {
        if (trackList == null) {
            return 0;
        }
        return trackList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.track_item,parent,false);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        TrackParcelable track = trackList.get(position);

        viewHolder.txtAlbumName.setText(track.album.name);
        viewHolder.txtTrackName.setText(track.name);

        if (track.album.image != null) {
            Picasso.with(getContext())
                    .load(track.album.image.url)
                    .placeholder(R.drawable.spotify_logo)
                    .into(viewHolder.ivImage);
        } else {
            viewHolder.ivImage.setImageResource(R.drawable.spotify_logo);
        }

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.imageView) ImageView ivImage;
        @InjectView(R.id.albumName) TextView txtAlbumName;
        @InjectView(R.id.trackName) TextView txtTrackName;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
