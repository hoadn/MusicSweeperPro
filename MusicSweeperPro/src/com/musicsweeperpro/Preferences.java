package com.musicsweeperpro;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        //Get the custom preference
//        Preference customPref = (Preference) findPreference("customPref");
//        customPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//
//        	public boolean onPreferenceClick(Preference preference) {
//                Toast.makeText(getBaseContext(),
//                                "customPref preference has been clicked",
//                                Toast.LENGTH_LONG).show();
//                return true;
//            }
//
//        });
    }    
}
