package com.example.soignemoi.activity.ui.patient;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PatientViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PatientViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Patient du Jour");
    }

    public LiveData<String> getText() {
        return mText;
    }
}