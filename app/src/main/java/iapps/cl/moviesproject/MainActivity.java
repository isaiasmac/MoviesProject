package iapps.cl.moviesproject;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private final OkHttpClient client = new OkHttpClient();

    private ListView moviesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesListView = (ListView) findViewById(R.id.moviesListView);

        if (!isNetworkAvailable()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("Verifica tu conexión a Internet");
            builder.setPositiveButton(android.R.string.ok, null);

            AlertDialog alert = builder.create();
            alert.show();
        }
        getYifyMoviesOkHttp();
    }

    private void getYifyMoviesOkHttp(){
        Request request = new Request.Builder()
                .url(YifyConstants.LIST_MOVIES)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String jsonResponse = response.body().string();
                try {
                    JSONObject responseJSON = new JSONObject(jsonResponse);
                    JSONObject dataJSON = responseJSON.getJSONObject("data");
                    JSONArray moviesArray = dataJSON.getJSONArray("movies");

                    setMoviesListAdapter(moviesArray);
                    Log.i(TAG, "MovieArray: " + moviesArray);
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException: " + e.getMessage());
                }
            }
        });

    }

//    private void getYifyMovies(){
//
//        JsonObjectRequest jsObjRequest = new JsonObjectRequest
//                (Request.Method.GET, YifyConstants.LIST_MOVIES, (String) null, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        try {
//                            JSONObject dataJSON = response.getJSONObject("data");
//                            JSONArray moviesArray = dataJSON.getJSONArray("movies");
//
//                            Movie[] movies = new Movie[moviesArray.length()];
//
//                            for (int i = 0; i<moviesArray.length(); i++){
//                                JSONObject movieJSON = moviesArray.getJSONObject(i);
//                                Movie movie = new Movie();
//                                movie.setId(movieJSON.getInt("id"));
//                                movie.setTitle(movieJSON.getString("title"));
//                                movie.setTitle_long(movieJSON.getString("title_long"));
//                                movie.setBackgrounImage(movieJSON.getString("background_image"));
//                                movie.setSmallCover(movieJSON.getString("small_cover_image"));
//                                movie.setYear(movieJSON.getInt("year"));
//                                movie.setRating((float) movieJSON.getDouble("rating"));
//                                movie.setLanguage(movieJSON.getString("language"));
//
//                                movies[i] = movie;
//                            }
//
//                            MovieListAdapter adapter = new MovieListAdapter(MainActivity.this, movies);
//                            moviesListView.setAdapter(adapter);
//
//                        } catch (JSONException e) {
//                            Log.e(TAG, "JSONException: "+e.getMessage());
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError e) {
//                        Log.e(TAG, "ErrorResponse: " + e.getMessage());
//                    }
//                });
//
//        Volley.newRequestQueue(this).add(jsObjRequest);
//    }

    private void setMoviesListAdapter(JSONArray moviesArray) throws JSONException{

        Movie[] movies = new Movie[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movieJSON = moviesArray.getJSONObject(i);

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

        final Movie[] _movies = movies;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MovieListAdapter adapter = new MovieListAdapter(MainActivity.this, _movies);
                moviesListView.setAdapter(adapter);
            }
        });

    }

    public void startRecyclerActivity(View v) {
        Intent i = new Intent(this, RecyclerActivity.class);
        startActivity(i);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
