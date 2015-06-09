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

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;
import simoncr.com.spotifystreamer.R;

/**
 * Created by scascacha on 6/8/15.
 */
public class ArtistAdapter extends ArrayAdapter<Artist> {
    private List<Artist> artists;

    public ArtistAdapter(Context context, List<Artist> artists) {
        super(context, R.layout.artist_item, artists);
        this.artists = artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
        if (artists != null || artists.size() > 0 ) {
            notifyDataSetChanged();
        }
    }

    @Override
    public Artist getItem(int position) {
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

            viewHolder = new ViewHolder();
            viewHolder.ivImage = (ImageView)convertView.findViewById(R.id.imageView);
            viewHolder.txtArtistName = (TextView)convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Artist artist = getItem(position);
        viewHolder.txtArtistName.setText(artist.name);
        if (artist.images != null && artist.images.size() > 0) {
            Image image = artist.images.get(0);
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
        TextView txtArtistName;
    }
}
