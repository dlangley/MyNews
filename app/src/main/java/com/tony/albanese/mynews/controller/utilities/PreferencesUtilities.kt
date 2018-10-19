package com.tony.albanese.mynews.controller.utilities

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tony.albanese.mynews.model.Article

fun saveArrayListToSharedPreferences(preferences: SharedPreferences, key: String, array: ArrayList<Article>) {
    val gson = Gson()
    val stringedArrayList = gson.toJson(array)
    preferences.edit().putString(key, stringedArrayList).apply()
}

fun loadArrayListFromSharedPreferences(preferences: SharedPreferences, key: String): ArrayList<Article> {
    var list = ArrayList<Article>()
    val gson = Gson() //Create a Gson object instance.
    val articleListData = preferences.getString(key, "none")
    val type = object : TypeToken<ArrayList<Article>>() {
    }.type //Declare the type of object that should be restored.
    if (articleListData != "none") {
        list = gson.fromJson(articleListData, type) //Restore the object from Json
    }

    return list
}