/*
 * Copyright (C) 2020 DerpFest
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.derpquest.settings.fragments;

import android.content.Context;
import android.os.Bundle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.ListPreference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;
import com.android.settings.SettingsPreferenceFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SearchIndexable
public class GestureSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_TORCH_LONG_PRESS_POWER_TIMEOUT =
            "torch_long_press_power_timeout";

    private ListPreference mTorchLongPressPowerTimeout;

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.DERP;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.gesture);

        mTorchLongPressPowerTimeout =
                    (ListPreference) findPreference(KEY_TORCH_LONG_PRESS_POWER_TIMEOUT);

        mTorchLongPressPowerTimeout.setOnPreferenceChangeListener(this);
        int TorchTimeout = Settings.System.getInt(getContentResolver(),
                        Settings.System.TORCH_LONG_PRESS_POWER_TIMEOUT, 0);
        mTorchLongPressPowerTimeout.setValue(Integer.toString(TorchTimeout));
        mTorchLongPressPowerTimeout.setSummary(mTorchLongPressPowerTimeout.getEntry());
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
         if (preference == mTorchLongPressPowerTimeout) {
            String TorchTimeout = (String) newValue;
            int TorchTimeoutValue = Integer.parseInt(TorchTimeout);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.TORCH_LONG_PRESS_POWER_TIMEOUT, TorchTimeoutValue);
            int TorchTimeoutIndex = mTorchLongPressPowerTimeout
                    .findIndexOfValue(TorchTimeout);
            mTorchLongPressPowerTimeout
                    .setSummary(mTorchLongPressPowerTimeout.getEntries()[TorchTimeoutIndex]);
            return true;
        }
        return false;
    }

    /**
     * For Search.
     */
    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
        new BaseSearchIndexProvider() {

            @Override
            public List<SearchIndexableResource> getXmlResourcesToIndex(
                    Context context, boolean enabled) {
                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.gesture;
                    return Arrays.asList(sir);
            }

            @Override
            public List<String> getNonIndexableKeys(Context context) {
                ArrayList<String> result = new ArrayList<String>();
                return result;
            }
        };
}
