package com.example.moviedb

import android.app.Application
import com.example.moviedb.di.components.DaggerApplicationComponent

class MovieApp: Application() {

    val appComponent = DaggerApplicationComponent.factory().create(this)
}