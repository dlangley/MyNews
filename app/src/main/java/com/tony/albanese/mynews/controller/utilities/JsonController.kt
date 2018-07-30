package com.tony.albanese.mynews.controller.utilities

import android.util.Log
import org.json.JSONException
import org.json.JSONObject

/*This is one function that gets called when the user clicks the "Search" button.
It will accept the search terms, dates, and news desk settings and packages them as a JSON Object.
This JSON Object will get passed to the generate search URL.
 */
//TODO: write test cases for this function by itself.
fun createSearchParametersJson(terms: String, startDate: String = "", endDate: String = "", desks: String = ""): JSONObject {
    var json = JSONObject()
    try {
        json.put("search_terms", terms)
        json.put("start_date", startDate)
        json.put("end_date", endDate)
        json.put("news_desks", desks)
    } catch (e: JSONException) {
        e.printStackTrace()
    }
    return json
}

//Need a function to return the values from the JSON object.
fun getSearchParametersFromJson(key: String, json: JSONObject): String {
    try {
        return json.getString(key)

    } catch (e: JSONException) {
        Log.e("getJsonValue()", "Error parsing JSON.")
    }
    return "Nothing."
}

fun parseServerResponseJson() {
    
}