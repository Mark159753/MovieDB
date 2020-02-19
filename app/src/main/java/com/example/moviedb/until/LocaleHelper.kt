package com.example.moviedb.until

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import java.lang.NullPointerException
import java.util.*

class LocaleHelper {
    companion object{

        private const val SAVED_LANGUAGE = "com.example.moviedb.until.SAVED_LANGUAGE"

        fun onAttach(context: Context, defaultLanguage:String? = null):Context{
            val lang = getPersistedData(context, defaultLanguage ?: Locale.getDefault().toLanguageTag())
            return setLocale(context, lang)
        }


        fun setLocale(context: Context, language:String):Context{
            persist(context, language)
            return updateResources(context, language)
        }

        fun getLanguage(context: Context):String{
            return getPersistedData(context, Locale.getDefault().toLanguageTag())
        }

        private fun updateResources(context: Context, language: String):Context{
            val locale = Locale.forLanguageTag(language)
            Locale.setDefault(locale)
            val config = context.resources.configuration
            config.setLocale(locale)
            return context.createConfigurationContext(config)
        }

        private fun persist(context: Context, language: String){
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()
            editor.putString(SAVED_LANGUAGE, language)
            editor.apply()
        }

        private fun getPersistedData(context:Context, defaultLanguage:String):String{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(SAVED_LANGUAGE, defaultLanguage)!!
        }
    }
}