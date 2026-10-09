package com.example.listycity

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.jvm.java
import com.example.listycity.City

class CityRepository {

    private val db = Firebase.firestore // first we create a db object
    // now we create a collection called cities and
    // cities is jsut a pointer to the cities
    private val citiesRef = db.collection("cities")



    private val _cities = mutableStateListOf(
        City("Edmonton", "AB"),
        City("Vancouver", "BC"),
        City("Toronto", "ON")
    )

    val cities: List<City>
        get() = _cities


    // this is the database part
    init{
        citiesRef.addSnapshotListener { snapshot, error ->
            if(error != null ){// if there is an error
                return@addSnapshotListener// exit this listener callback
            } // if database changes the listener does its work

            _cities.clear() // we clear the local copy of city cuz we want
            // the databse copy

            //snapshot.documents gives is the document
            snapshot?.documents?.forEach{document ->
                val city = document.toObject(City::class.java)
                if (city!=null){
                    _cities.add(city)
                }
            }
        }
    }

    fun addCity(city: City) {
        //_cities.add(city)
        citiesRef.document(city.name).set(city)
    }

    fun updateCity(oldCity: City, updatedCity: City) {
//        val index = _cities.indexOf(oldCity)
//        if (index != -1) {
//            _cities[index] = updatedCity
//        }
        citiesRef.document(oldCity.name).set(updatedCity)
     }

    fun deleteCity(deletedCity:City){
        citiesRef.document(deletedCity.name).delete()
    }

}