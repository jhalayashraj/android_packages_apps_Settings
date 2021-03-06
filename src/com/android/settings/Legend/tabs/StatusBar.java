package com.android.settings.Legend.tabs;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.content.ContentResolver;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.RemoteException;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;


public class StatusBar extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String TAG = "StatusBar";
    private static final String PREF_SMART_PULLDOWN = "smart_pulldown";
    private static final String FORCE_EXPANDED_NOTIFICATIONS = "force_expanded_notifications";
    private static final String DISABLE_IMMERSIVE_MESSAGE = "disable_immersive_message";

    private ListPreference mSmartPulldown;
    private SwitchPreference mForceExpanded;
    private SwitchPreference mDisableIM;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.statusbar);

	PreferenceScreen prefSet = getPreferenceScreen();
  		ContentResolver resolver = getActivity().getContentResolver();

	mSmartPulldown = (ListPreference) findPreference(PREF_SMART_PULLDOWN);
	mSmartPulldown.setOnPreferenceChangeListener(this);
	int smartPulldown = Settings.System.getInt(resolver,
                Settings.System.QS_SMART_PULLDOWN, 0);
        mSmartPulldown.setValue(String.valueOf(smartPulldown));
        updateSmartPulldownSummary(smartPulldown);

	mForceExpanded = (SwitchPreference) findPreference(FORCE_EXPANDED_NOTIFICATIONS);
        mForceExpanded.setOnPreferenceChangeListener(this);
        int ForceExpanded = Settings.System.getInt(getContentResolver(),
                FORCE_EXPANDED_NOTIFICATIONS, 0);
        mForceExpanded.setChecked(ForceExpanded != 0);

	mDisableIM = (SwitchPreference) findPreference(DISABLE_IMMERSIVE_MESSAGE);
        mDisableIM.setOnPreferenceChangeListener(this);
        int DisableIM = Settings.System.getInt(getContentResolver(),
                DISABLE_IMMERSIVE_MESSAGE, 0);
        mDisableIM.setChecked(DisableIM != 0);

    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
  	ContentResolver resolver = getActivity().getContentResolver();
	if (preference == mSmartPulldown) {
            int smartPulldown = Integer.valueOf((String) newValue);
            Settings.System.putInt(resolver, Settings.System.QS_SMART_PULLDOWN, smartPulldown);
            updateSmartPulldownSummary(smartPulldown);
            return true;
	} else if (preference == mForceExpanded) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(), FORCE_EXPANDED_NOTIFICATIONS,
                    value ? 1 : 0);
            return true;
	} else if (preference == mDisableIM) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(), DISABLE_IMMERSIVE_MESSAGE,
                    value ? 1 : 0);
            return true;
	}
	return false;
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.LEGENDROM;
    }

    private void updateSmartPulldownSummary(int value) {
         Resources res = getResources();
 
         if (value == 0) {
             // Smart pulldown deactivated
             mSmartPulldown.setSummary(res.getString(R.string.smart_pulldown_off));
         } else if (value == 3) {
             mSmartPulldown.setSummary(res.getString(R.string.smart_pulldown_none_summary));
         } else {
             String type = res.getString(value == 1
                     ? R.string.smart_pulldown_dismissable
                     : R.string.smart_pulldown_ongoing);
             mSmartPulldown.setSummary(res.getString(R.string.smart_pulldown_summary, type));
         }
     }

   /*@Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        return true;
    }*/
}
