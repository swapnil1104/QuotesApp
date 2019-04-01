package com.broooapps.quotesapp.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.broooapps.quotesapp.R;
import com.broooapps.quotesapp.network.OkHttpTask;
import com.broooapps.quotesapp.repository.QuotesRepository;
import com.broooapps.quotesapp.util.Navigator;
import com.broooapps.quotesapp.viewmodel.SplashActivityViewModel;
import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;
import com.squareup.picasso.Picasso;

public class SplashActivity extends AppCompatActivity {

    private ImageView bg_image;
    private RotatingTextWrapper rotating_text_wrapper;

    SplashActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        OkHttpTask.getInstance(true);

        viewModel = ViewModelProviders.of(this).get(SplashActivityViewModel.class);

        if (viewModel.isNetworkAvailable()) {
            initializeViews();
            defaultConfigurations();
        } else {
            Toast.makeText(this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        bg_image = findViewById(R.id.bg_image);

        rotating_text_wrapper = findViewById(R.id.rotating_text_wrapper);
        rotating_text_wrapper.setSize(35);
    }

    private void defaultConfigurations() {
        viewModel.getBgUrl().observe(this, s -> {
            Picasso.get().load(s).fit().centerCrop().into(bg_image);
        });

        viewModel.getRotatableText().observe(this, rotatable1 -> {
            Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/opensans-bold.ttf");
            Rotatable rotatable = new Rotatable(Color.parseColor("#FFA036"),
                    1000,
                    rotatable1);
            rotatable.setSize(35);
            rotatable.setCenter(true);
            rotatable.setTypeface(typeface);
            rotatable.setInterpolator(new BounceInterpolator());
            rotatable.setAnimationDuration(900);

            rotating_text_wrapper.setContent("?", rotatable);
        });

        LoadTask task = new LoadTask();
        task.execute();
    }

    class LoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            QuotesRepository repository = QuotesRepository.getInstance(getApplication());
            repository.fetchQuotes();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Navigator.mainActivity(SplashActivity.this);
        }
    }


}
