package com.example.moviedb.data.network

import com.example.moviedb.model.InTheaterResponse
import com.example.moviedb.model.OnTvResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/"
private const val API_KEY = "5fef28f1dfa50d13b3dd0ccd57ee7ef5"

interface TMDBserver {

    @GET("movie/upcoming")
    fun getInTheatre(
        @Query("language") language:String = "en-US", // uk-UK Українська
        @Query("page") page:Int = 1
    ):Call<InTheaterResponse>

    @GET("tv/on_the_air")
    fun getOnTv(
        @Query("language") language:String = "en-US", // uk-UK Українська
        @Query("page") page:Int = 1
    ):Call<OnTvResponse>

    companion object{
        operator fun invoke():TMDBserver{
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val apiInterceptor = object :Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    val oldUrl = chain.request()
                        .url
                        .newBuilder()
                        .addQueryParameter("api_key", API_KEY)
                        .build()
                    val newUrl = chain.request()
                        .newBuilder()
                        .url(oldUrl)
                        .build()
                    return chain.proceed(newUrl)
                }
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(apiInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(TMDBserver::class.java)
        }
    }
}