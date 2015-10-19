package iapps.cl.moviesproject;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by iSaias on 10/18/15.
 */
public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {

    private Context _context;
    private Movie[] _movies;

    public MovieRecyclerAdapter(Context context, Movie[] movies){
        _context = context;
        _movies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_item, parent, false);

        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bindMovie(_movies[position]);
    }

    @Override
    public int getItemCount() {
        return _movies.length;
    }



    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView coverImageView; // public by default
        public TextView titleTextView;
        public TextView ratingTextView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            coverImageView = (ImageView) itemView.findViewById(R.id.coverImageView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            ratingTextView = (TextView) itemView.findViewById(R.id.ratingTextView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(_context, MovieDetailsActivity.class);
            i.putExtra("movieSelected", (int) titleTextView.getTag());
            _context.startActivity(i);
        }

        public void bindMovie(Movie movie){
            titleTextView.setTag(movie.getId());
            titleTextView.setText(movie.getTitle());
            ratingTextView.setText(movie.getRating()+"");

            Picasso.with(_context)
                    .load(movie.getSmallCover())
                    .placeholder(R.drawable.movie_placeholder)
                    .error(R.drawable.movie_placeholder)
                    .into(coverImageView);
        }


    }
}
