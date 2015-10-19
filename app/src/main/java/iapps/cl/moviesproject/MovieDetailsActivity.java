package iapps.cl.moviesproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private final OkHttpClient client = new OkHttpClient();
    private int movieId;

    private ImageView largeCoverImageView;
    private TextView largeTitleTextView;
    private TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Bundle extras = getIntent().getExtras();
        movieId = extras.getInt("movieSelected");

        largeCoverImageView = (ImageView) findViewById(R.id.largeCoverImageView);
        largeTitleTextView = (TextView) findViewById(R.id.largeTitleTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);

        Log.i(TAG, "MovieSelected = " + movieId);

        getMovieDetails();
    }

    private void getMovieDetails(){
        String url = YifyConstants.MOVIE_DETAILS+"?movie_id="+movieId+"&with_images=true";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String data = response.body().string();
                try {
                    final JSONObject dataJSON = new JSONObject(data);
                    JSONObject _dataJSON = dataJSON.getJSONObject("data");
                    JSONObject imagesJSON = _dataJSON.getJSONObject("images");

                    final String title = _dataJSON.getString("title_long");
                    final String urlImage = imagesJSON.getString("large_cover_image");
                    final String description = _dataJSON.getString("description_full");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setUI(title, description, urlImage);
                        }
                    });
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException: " + e.getMessage());
                }

            }
        });
    }

    private void setUI(String title, String description, String urlImage){
        largeTitleTextView.setText(title);
        descriptionTextView.setText(description);

        Picasso.with(MovieDetailsActivity.this)
                .load(urlImage)
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder)
                .into(largeCoverImageView);
    }
}
