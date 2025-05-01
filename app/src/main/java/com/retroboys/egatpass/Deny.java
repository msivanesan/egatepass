package com.retroboys.egatpass;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class Deny extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.deny_fragment,container,false);

    }
    @Override
    public void onStart() {
        super.onStart();
        if(getDialog()!= null && getDialog().getWindow()!= null){
            Window window =getDialog().getWindow();
            window.setLayout(getResources().getDimensionPixelSize(R.dimen.width),getResources().getDimensionPixelSize(R.dimen.height));
        }
    }
}
