package com.broooapps.quotesapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.broooapps.quotesapp.model.Quote;
import com.broooapps.quotesapp.repository.QuotesRepository;

import java.util.List;

/**
 * Created by Swapnil Tiwari on 23/03/19.
 * swapnil.tiwari@box8.in
 */
public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Quote>> quoteMutableLiveData;

    private QuotesRepository repository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        repository = QuotesRepository.getInstance(application);
    }

    public MutableLiveData<List<Quote>> fetchQuotes() {
        return repository.fetchQuotes();
    }

    public void refreshQuotes() {
        quoteMutableLiveData =  repository.refreshQuotes();
    }
}
