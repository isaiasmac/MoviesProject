package iapps.cl.moviesproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by iSaias on 10/17/15.
 */
public class MovieListAdapter extends BaseAdapter {

    private Context _context;
    private Movie[] _movies;
    private ImageLoader mImageLoader;

    public MovieListAdapter(Context context, Movie[] movies){
        _context = context;
        _movies = movies;
    }

    @Override
    public int getCount() {
        return _movies.length;
    }

    @Override
    public Object getItem(int position) {
        return _movies[position];
    }

    @Override
    public long getItemId(int position) {
        return 0; // not used
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null){
            convertView = LayoutInflater.from(_context).inflate(R.layout.movie_list_item, null);
            holder = new ViewHolder();

            holder.coverImageView = (ImageView) convertView.findViewById(R.id.coverImageView);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
            holder.ratingTextView = (TextView) convertView.findViewById(R.id.ratingTextView);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        Movie movie = _movies[position];
        holder.titleTextView.setText(movie.getTitle());
        holder.ratingTextView.setText(movie.getRating()+"");

        // Picasso
        Picasso.with(_context)
                .load(movie.getSmallCover())
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder)
                .into(holder.coverImageView);

        // Volley
//        mImageLoader = VolleySingleton.getInstance().getImageLoader();
//        NetworkImageView cover = (NetworkImageView) holder.coverImageView;
//        cover.setImageUrl(movie.getSmallCover(), mImageLoader);

        return convertView;
    }


    private static class ViewHolder{
        ImageView coverImageView; // public by default
        TextView titleTextView;
        TextView ratingTextView;
    }
}
