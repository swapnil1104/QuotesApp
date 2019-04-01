package com.broooapps.quotesapp.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.broooapps.quotesapp.R;
import com.broooapps.quotesapp.adapter.ViewPagerAdapter;
import com.broooapps.quotesapp.viewmodel.MainActivityViewModel;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    // View Members.
    private Button button_refresh;
    private ProgressBar progress_circular;
    private ViewPager viewPager;

    private ViewPagerAdapter adapter;
    private MainActivityViewModel viewModel;

    // Data Members
    // --- --- --- --- --- --- ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // view model init.
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        initializeViews();
        defaultConfigurations();
    }

    private void initializeViews() {
        button_refresh = findViewById(R.id.button_refresh);
        progress_circular = findViewById(R.id.progress_circular);
        viewPager = findViewById(R.id.view_pager);
    }


    private void defaultConfigurations() {

        button_refresh.setOnClickListener(v -> {
            progress_circular.setVisibility(View.VISIBLE);
            button_refresh.setVisibility(View.GONE);
            viewModel.refreshQuotes();
        });

        viewModel.fetchQuotes().observe(this, quotes -> {
            progress_circular.setVisibility(View.GONE);
            adapter = new ViewPagerAdapter(MainActivity.this, quotes);
            viewPager.setAdapter(adapter);
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
}
