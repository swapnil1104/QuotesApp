package com.broooapps.quotesapp.view;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.broooapps.quotesapp.R;
import com.broooapps.quotesapp.adapter.ViewPagerAdapter;
import com.broooapps.quotesapp.util.Navigator;
import com.broooapps.quotesapp.util.ViewUtils;
import com.broooapps.quotesapp.viewmodel.MainActivityViewModel;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int WRITE_REQ_CODE = 1000;
    // View Members.
    private Button button_refresh;
    private TextView text_share;
    private ProgressBar progress_circular;
    private ViewPager viewPager;

    private ViewPagerAdapter adapter;
    private MainActivityViewModel viewModel;

    // Data Members
    // --- --- --- --- --- --- ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // view model init.
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        initializeViews();
        defaultConfigurations();
    }

    private void initializeViews() {
        button_refresh = findViewById(R.id.button_refresh);
        text_share = findViewById(R.id.text_share);
        progress_circular = findViewById(R.id.progress_circular);
        viewPager = findViewById(R.id.view_pager);
    }


    private void defaultConfigurations() {
        viewModel.fetchQuotes().observe(this, quotes -> {
            progress_circular.setVisibility(View.GONE);
            text_share.setVisibility(View.VISIBLE);

            adapter = new ViewPagerAdapter(MainActivity.this, quotes);
            viewPager.setAdapter(adapter);
        });

        button_refresh.setOnClickListener(v -> {
            progress_circular.setVisibility(View.VISIBLE);
            button_refresh.setVisibility(View.GONE);
            text_share.setVisibility(View.GONE);

            viewModel.refreshQuotes();
        });

        text_share.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    shareImageWhatsapp();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_REQ_CODE );
                }
            } else {
                shareImageWhatsapp();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                if (i == adapter.getCount() - 1) {
                    button_refresh.setVisibility(View.VISIBLE);
                } else if (button_refresh.getVisibility() == View.VISIBLE) {
                    button_refresh.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    private void shareImageWhatsapp() {
        progress_circular.setVisibility(View.VISIBLE);
        text_share.setVisibility(View.INVISIBLE);
        boolean refreshVisible = false;

        if (button_refresh.getVisibility() == View.VISIBLE) {
            refreshVisible = true;
            button_refresh.setVisibility(View.GONE);
        }
        new Handler().postDelayed(() -> runOnUiThread(() -> {

            File image = ViewUtils.takeScreenshot(getWindow().getDecorView().getRootView());
            if (image != null) {
                ViewUtils.shareScreenshot(this, image);
                progress_circular.setVisibility(View.GONE);

            }
        }), 256);

        if (refreshVisible) {
            button_refresh.setVisibility(View.VISIBLE);
        }
        text_share.setVisibility(View.VISIBLE);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_REQ_CODE:
                text_share.performClick();
                break;
                /* end of case */
            default:
                break;
        }
    }
}
