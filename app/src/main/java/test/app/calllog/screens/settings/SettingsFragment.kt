package test.app.calllog.screens.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import test.app.calllog.R
import test.app.calllog.data.local.KeyValueStorage.Companion.KEY_SERVER_IP
import test.app.calllog.data.local.KeyValueStorage.Companion.KEY_SERVER_PORT
import test.app.calllog.data.local.KeyValueStorage.Companion.PREF_FILE_NAME


class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        preferenceManager.sharedPreferencesName = PREF_FILE_NAME
        sharedPreferences = preferenceManager.sharedPreferences;

        setSummary(KEY_SERVER_IP)
        setSummary(KEY_SERVER_PORT)
    }

    override fun onResume() {
        super.onResume()
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        setSummary(key!!)
    }

    private fun setSummary(key: String) {

        val pref = findPreference<Preference>(key)
        pref?.summary = sharedPreferences.getString(key, "")
    }
}