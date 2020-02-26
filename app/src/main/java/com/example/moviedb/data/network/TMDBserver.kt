package com.example.moviedb.data.network

import androidx.lifecycle.LiveData
import com.example.moviedb.model.cast.CastResponse
import com.example.moviedb.model.discover.DiscoverResponse
import com.example.moviedb.model.genre.GenreResponse
import com.example.moviedb.model.movieDetail.MovieDetailResponse
import com.example.moviedb.model.movieVideo.VideoResponse
import com.example.moviedb.model.popular.ResponsePopular
import com.example.moviedb.model.similarMovies.SimilarMoviesResponse
import com.example.moviedb.model.theathre.InTheaterResponse
import com.example.moviedb.model.trends.TrendsResponse
import com.example.moviedb.model.tv.OnTvResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("movie/popular")
    fun getMoviesPopular(
        @Query("language") language:String = "en-US", // uk-UK Українська
        @Query("page") page:Int = 1
    ):Call<ResponsePopular>

    @GET("trending/{media_type}/{time_window}")
    fun getMovieTrends(
        @Path("media_type") media_type:String = "movie",
        @Path("time_window") time_window:String = "day",
        @Query("language") language:String = "en-US", // uk-UK Українська
        @Query("page") page:Int = 1
    ):Call<TrendsResponse>

    @GET("discover/movie")
    fun getDiscoverMovie(
        @Query("language") language:String = "en-US", // uk-UK Українська
        @Query("page") page:Int = 1,
        @Query("sort_by") sort_by:String = "popularity.desc",
        @Query("with_genres") with_genres:List<Int>? = null,
        @Query("without_genres") without_genres:List<Int>? = null,
        @Query("primary_release_date.gte") primary_release_date_from:String? = null,
        @Query("primary_release_date.lte") primary_release_date_to:String? = null
    ):Call<DiscoverResponse>

    @GET("genre/movie/list")
    fun getGenreList(
        @Query("language") language:String = "en_US"
    ):Call<GenreResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") movie_id:Int,
        @Query("language") language:String = "en_US"
    ):Call<MovieDetailResponse>

    @GET("/3/movie/{movie_id}/credits")
    fun getMovieCast(
        @Path("movie_id") movie_id:Int,
        @Query("language") language:String = "en_US"
    ):Call<CastResponse>

    @GET("movie/{movie_id}/similar")
    fun getSimilarMovies(
        @Path("movie_id") movie_id:Int,
        @Query("language") language:String = "en_US",
        @Query("page") page:Int = 1
    ):Call<SimilarMoviesResponse>

    @GET("movie/{movie_id}/videos")
        fun getMovieVideos(
            @Path("movie_id") movie_id:Int,
            @Query("language") language:String = "en_US"
        ):Call<VideoResponse>


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