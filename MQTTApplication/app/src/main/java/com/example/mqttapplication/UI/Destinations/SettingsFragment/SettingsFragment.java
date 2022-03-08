package com.example.mqttapplication.UI.Destinations.SettingsFragment;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import com.example.mqttapplication.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}