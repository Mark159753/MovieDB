package com.example.moviedb

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.moviedb.di.components.DaggerApplicationComponent
import com.example.moviedb.until.LocaleHelper

class MovieApp: Application() {

    val appComponent = DaggerApplicationComponent.factory().create(this)

}