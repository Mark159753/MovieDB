package com.example.moviedb.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.example.moviedb.MovieApp
import com.example.moviedb.R
import com.example.moviedb.data.local.FilmsDB
import com.example.moviedb.until.LocaleHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors
import javax.inject.Inject

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var navController:NavController
    @Inject lateinit var roomDB: FilmsDB

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase!!, LocaleHelper.getLanguage(newBase)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as MovieApp).appComponent.inject(this)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(bottom_navigation_menu, navController)

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Log.e("PREFERENCE", key ?: "no key")
        when(key){
            "lang" -> {
                LocaleHelper.setLocale(this,sharedPreferences?.getString(key, "en-US")!!)

                roomDB.transactionExecutor.execute {
                    roomDB.clearAllTables()
                    runOnUiThread{recreate()}
                }
            }
        }
    }

    override fun onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }
}
