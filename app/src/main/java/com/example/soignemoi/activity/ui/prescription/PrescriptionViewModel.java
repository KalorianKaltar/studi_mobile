package com.example.soignemoi.activity.ui.prescription;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PrescriptionViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PrescriptionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Prescription");
    }

    public LiveData<String> getText() {
        return mText;
    }
}