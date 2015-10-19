package iapps.cl.moviesproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RecyclerActivity extends Activity {
    public static final String TAG = RecyclerActivity.class.getSimpleName();

    private RecyclerView movieListRecycler;
    private MovieRecyclerAdapter movieRecyclerAdapter;
    private final OkHttpClient client = new OkHttpClient();
    private Movie[] _listMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        movieListRecycler = (RecyclerView) findViewById(R.id.movieListRecycler);
        movieListRecycler.setLayoutManager(new LinearLayoutManager(this));
        movieListRecycler.setHasFixedSize(true);
        getYifyMovies();
    }


    private void getYifyMovies() {
        Request request = new Request.Builder()
                .url(YifyConstants.LIST_MOVIES)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(TAG, "Failure: " + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String data = response.body().string();
                try {
                    JSONObject reponseJSON = new JSONObject(data);
                    JSONObject dataJSON = reponseJSON.getJSONObject("data");
                    JSONArray moviesArray = dataJSON.getJSONArray("movies");
                    parseMovieJSON(moviesArray);

                } catch (JSONException e) {
                    Log.e(TAG, "JSONException: " + e.getMessage());
                }
            }
        });
    }


    private void parseMovieJSON(JSONArray jsonArray) throws JSONException{
        Movie[] movies = new Movie[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject movieJSON = jsonArray.getJSONObject(i);

            Movie movie = new Movie();
            movie.setId(movieJSON.getInt("id"));
            movie.setTitle(movieJSON.getString("title"));
            movie.setTitle_long(movieJSON.getString("title_long"));
            movie.setBackgrounImage(movieJSON.getString("background_image"));
            movie.setSmallCover(movieJSON.getString("small_cover_image"));
            movie.setYear(movieJSON.getInt("year"));
            movie.setRating((float) movieJSON.getDouble("rating"));
            movie.setLanguage(movieJSON.getString("language"));
            movies[i] = movie;
        }

        _listMovies = movies;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                movieRecyclerAdapter = new MovieRecyclerAdapter(RecyclerActivity.this, _listMovies);
                movieListRecycler.setAdapter(movieRecyclerAdapter);
            }
        });
    }

}
