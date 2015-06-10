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

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import simoncr.com.spotifystreamer.R;

/**
 * Created by scascacha on 6/9/15.
 */
public class TrackAdapter extends ArrayAdapter<Track> {
    private List<Track> trackList;

    public TrackAdapter(Context context, List<Track> trackList) {
        super(context, R.layout.track_item, trackList);
        this.trackList = trackList;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
        notifyDataSetChanged();
    }

    @Override
    public Track getItem(int position) {
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

            viewHolder = new ViewHolder();
            viewHolder.ivImage = (ImageView)convertView.findViewById(R.id.imageView);

            viewHolder.txtAlbumName = (TextView)convertView.findViewById(R.id.albumName);
            viewHolder.txtTrackName = (TextView)convertView.findViewById(R.id.trackName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Track track = trackList.get(position);

        viewHolder.txtAlbumName.setText(track.album.name);
        viewHolder.txtTrackName.setText(track.name);

        if (track.album.images != null && track.album.images.size() > 0) {
            Image image = track.album.images.get(0);
            if (image != null) {
                Picasso.with(getContext())
                        .load(image.url)
                        .placeholder(R.drawable.spotify_logo)
                        .into(viewHolder.ivImage);
            }
        } else {
            viewHolder.ivImage.setImageResource(R.drawable.spotify_logo);
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView ivImage;
        TextView txtAlbumName;
        TextView txtTrackName;
    }
}
