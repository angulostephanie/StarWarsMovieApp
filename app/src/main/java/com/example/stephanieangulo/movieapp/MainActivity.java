package com.example.stephanieangulo.movieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private Context mContext;
    private MovieAdapter adapter;
    ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        movies = Movie.getMoviesFromJSON("movies.json", mContext);
        adapter = new MovieAdapter(mContext, movies);
        mListView = findViewById(R.id.movie_list_view);
        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie selectedMovie = movies.get(i);
                launchActivity(selectedMovie, i);

            }
        });
    }

    private void launchActivity(Movie selectedMovie, int i) {
        Intent detailIntent = new Intent(mContext, MovieDetailActivity.class);
        detailIntent.putExtra("title", selectedMovie.title);
        detailIntent.putExtra("poster", selectedMovie.poster);
        detailIntent.putExtra("description", selectedMovie.description);
        detailIntent.putExtra("position", i);
        startActivityForResult(detailIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                boolean alreadySeen = data.getBooleanExtra("alreadySeen", false);
                boolean wantToSee = data.getBooleanExtra("wantToSee", false);
                boolean doNotLike = data.getBooleanExtra("doNotLike", false);
                int i = data.getIntExtra("position", 0);

                if(alreadySeen) {
                    movies.get(i).dynamicText = "Already Seen";
                } else if(wantToSee) {
                    movies.get(i).dynamicText = "Want to See";
                } else if (doNotLike) {
                    movies.get(i).dynamicText = "Don't like";
                }

                adapter.notifyDataSetChanged();
            }
        }
    }
}
