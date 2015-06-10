package simoncr.com.spotifystreamer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;
import simoncr.com.spotifystreamer.R;
import simoncr.com.spotifystreamer.model.ArtistParcelable;

/**
 * Created by scascacha on 6/8/15.
 */
public class ArtistAdapter extends ArrayAdapter<ArtistParcelable> {
    private ArrayList<ArtistParcelable> artists;

    public ArtistAdapter(Context context, ArrayList<ArtistParcelable> artists) {
        super(context, R.layout.artist_item, artists);
        this.artists = artists;
    }

    public void setArtists(ArrayList<ArtistParcelable> artists) {
        this.artists = artists;
        notifyDataSetChanged();
    }

    @Override
    public ArtistParcelable getItem(int position) {
        return artists.get(position);
    }

    @Override
    public int getCount() {
        if (artists == null) {
            return 0;
        }
        return artists.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.artist_item,parent,false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        ArtistParcelable artist = getItem(position);
        viewHolder.txtArtistName.setText(artist.name);
        if (artist.image != null) {
            Picasso.with(getContext())
                    .load(artist.image.url)
                    .placeholder(R.drawable.spotify_logo)
                    .into(viewHolder.ivImage);
        } else {
            viewHolder.ivImage.setImageResource(R.drawable.spotify_logo);
        }

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.imageView) ImageView ivImage;
        @InjectView(R.id.textView) TextView txtArtistName;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
