package com.example.moviedb.ui.person

import androidx.lifecycle.ViewModel
import com.example.moviedb.data.repository.person.PersonRepository
import javax.inject.Inject

class PersonViewModel @Inject constructor(
    private val repository: PersonRepository
): ViewModel() {

    fun getPersonDetail(id:Int, language:String) = repository.getPersonDetail(id, language)

    fun getPersonKnowForCast(id:Int, language:String) = repository.getPeopleKnownFor(id, language)
}