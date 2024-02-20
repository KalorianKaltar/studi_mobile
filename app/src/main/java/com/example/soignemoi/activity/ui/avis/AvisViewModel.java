package com.example.soignemoi.activity.ui.avis;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AvisViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AvisViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Avis");
    }

    public LiveData<String> getText() {
        return mText;
    }
}