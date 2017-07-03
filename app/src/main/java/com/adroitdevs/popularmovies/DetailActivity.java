package com.adroitdevs.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;

public class DetailActivity extends AppCompatActivity implements Serializable {
    ImageView imageViewDisplayTop;
    ImageView imageViewDisplayImage;
    TextView textViewDisplayTitle;
    TextView textViewDisplayRating;
    TextView textViewDisplaySynopsis;
    TextView textViewDisplayReleaseDate;
    String preference, page;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("preference", preference);
        intent.putExtra("page", page);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imageViewDisplayTop = (ImageView) findViewById(R.id.image_display_top);
        imageViewDisplayImage = (ImageView) findViewById(R.id.iv_display_poster);
        textViewDisplayRating = (TextView) findViewById(R.id.tv_rating_content_display);
        textViewDisplayTitle = (TextView) findViewById(R.id.tv_title_content_display);
        textViewDisplaySynopsis = (TextView) findViewById(R.id.tv_synopsis_content_display);
        textViewDisplayReleaseDate = (TextView) findViewById(R.id.tv_release_content_display);

        Intent intent = getIntent();

        String[] movieDetail = intent.getStringArrayExtra("detail");

        String urlImage = "http://image.tmdb.org/t/p/w185/" + movieDetail[1];
        Context context = getApplicationContext();

        setTitle(movieDetail[0]);
        Glide.with(context).load(urlImage).into(imageViewDisplayTop);
        Glide.with(context).load(urlImage).into(imageViewDisplayImage);
        textViewDisplayRating.setText(movieDetail[3]);
        textViewDisplayTitle.setText(movieDetail[0]);
        textViewDisplaySynopsis.setText(movieDetail[2]);
        textViewDisplayReleaseDate.setText(movieDetail[4]);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        preference = movieDetail[5];
        page = movieDetail[6];
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("preference", preference);
                intent.putExtra("page", page);
                startActivity(intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
