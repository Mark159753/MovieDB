package com.example.moviedb.data.repository.person

import androidx.lifecycle.LiveData
import com.example.moviedb.model.person.credits.KnownForCast
import com.example.moviedb.model.person.credits.KnownForResponse
import com.example.moviedb.model.person.detail.PersonDetailsResponse

interface PersonRepository {

    fun getPersonDetail(id:Int, language:String):LiveData<PersonDetailsResponse>

    fun getPeopleKnownFor(id:Int, language:String):LiveData<List<KnownForCast>>
}