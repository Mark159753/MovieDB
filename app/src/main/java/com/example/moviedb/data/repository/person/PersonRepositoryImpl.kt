package com.example.moviedb.data.repository.person

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.person.credits.KnownForCast
import com.example.moviedb.model.person.credits.KnownForResponse
import com.example.moviedb.model.person.detail.PersonDetailsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val server:TMDBserver
):PersonRepository {

    override fun getPersonDetail(id: Int, language: String): LiveData<PersonDetailsResponse> {
        val result = MutableLiveData<PersonDetailsResponse>()

        server.getPersonDetails(id, language).enqueue(object : Callback<PersonDetailsResponse>{
            override fun onFailure(call: Call<PersonDetailsResponse>, t: Throwable) {
                Log.e("LOAD_ERROR", t.message ?: "unknown error")
            }

            override fun onResponse(
                call: Call<PersonDetailsResponse>,
                response: Response<PersonDetailsResponse>
            ) {
                if (response.isSuccessful){
                    result.postValue(response.body())
                }
            }
        })

        return result
    }

    override fun getPeopleKnownFor(id: Int, language: String): LiveData<List<KnownForCast>> {
        val result = MutableLiveData<List<KnownForCast>>()

        server.getPersonKnownForCast(id, language)
            .enqueue(object :Callback<KnownForResponse>{
                override fun onFailure(call: Call<KnownForResponse>, t: Throwable) {
                    Log.e("LOAD_ERROR", t.message ?: "unknown error")
                }

                override fun onResponse(
                    call: Call<KnownForResponse>,
                    response: Response<KnownForResponse>
                ) {
                    if (response.isSuccessful){
                        result.postValue(response.body()!!.cast)
                    }
                }
            })

        return result
    }
}