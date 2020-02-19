package com.example.moviedb.ui.preferences


import android.os.Bundle
import android.preference.PreferenceFragment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.moviedb.R
import com.example.moviedb.until.LocaleHelper
import java.util.*


class Settings : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val langList = findPreference<ListPreference>("lang")
        langList?.setDefaultValue(LocaleHelper.getLanguage(activity!!.applicationContext))
    }

}
