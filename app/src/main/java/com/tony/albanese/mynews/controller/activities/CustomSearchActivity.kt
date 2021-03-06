package com.tony.albanese.mynews.controller.activities

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.tony.albanese.mynews.R
import com.tony.albanese.mynews.controller.fragments.DatePickerFragment
import com.tony.albanese.mynews.controller.utilities.*
import kotlinx.android.synthetic.main.search_parameters_layout.*
import java.util.*
import kotlin.collections.HashMap

class CustomSearchActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    lateinit var searchButton: Button
    lateinit var searchEditText: EditText
    lateinit var url: String
    lateinit var searchStartDate: String
    lateinit var searchEndDate: String
    lateinit var calendar: Calendar
    lateinit var newsDesksHashMap: HashMap<Int, String>
    var selectedTextViewId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_parameters_layout)

        searchButton = btn_search
        searchEditText = text_view_search_terms
        calendar = Calendar.getInstance()
        //Search dates are optional and can be empty if the user doesn't select them.
        searchStartDate = ""
        searchEndDate = ""
        newsDesksHashMap = HashMap()

        setupTextChangeListener()

        //Set the listener for the button.
        btn_search.setOnClickListener { view ->
            if (networkIsAvailable(this)) {
                launchSearchActivity()
            } else {
                val toast = Toast.makeText(this, "There is no network connection", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    //Function that generates the search paramaters and the search url and saves it to shared preferences
    //Before the activity is launched.
    fun launchSearchActivity() {
        var searchTerms = searchEditText.text.toString()
        var newsDesks = generateNewsDeskParameter(newsDesksHashMap)

        val jsonParameters = createSearchParametersJson(searchTerms, searchStartDate, searchEndDate, newsDesks)
        url = generateSearchUrl(applicationContext, CUSTOM_SEARCH_SEARCH, jsonParameters)

        val preferences = applicationContext.getSharedPreferences(URL_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        preferences.edit().putString(ACTIVITY_CUSTOM_SEARCH_URL, url).apply()
        launchActivity()
    }

    //Launches the MainActivity and puts the custom search tab number in the intent.
    fun launchActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(TAB, CUSTOM_SEARCH_TAB)
        startActivity(intent)
        finish()
    }

    //Creates the DatePicker dialog.
    fun createDatePicker(view: View) {
        selectedTextViewId = view.id
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }

    //Function that sets the date for the search terms when the user clicks on the text boxes.
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        val date: Date = calendar.time

        when (selectedTextViewId) {
            R.id.tv_start_date -> {
                tv_start_date.text = convertDate(date)
                searchStartDate = convertDate(date, SEARCH_DATE_FORMAT)
            }

            R.id.tv_end_date -> {
                tv_end_date.text = convertDate(date)
                searchEndDate = convertDate(date, SEARCH_DATE_FORMAT)
            }
        }
    }

    //Set up the checkbox on click listener that adds news desks to the hashmap
    fun checkboxOnClickListener(view: View) {
        when (view.id) {
            R.id.check_box_arts -> {
                if (check_box_arts.isChecked) {
                    newsDesksHashMap.put(R.id.check_box_arts, "Arts")
                } else {
                    newsDesksHashMap.remove(R.id.check_box_arts)
                }
            }

            R.id.check_box_business -> {
                if (check_box_business.isChecked) {
                    newsDesksHashMap.put(R.id.check_box_business, "Business")
                } else {
                    newsDesksHashMap.remove(R.id.check_box_business)
                }
            }

            R.id.check_box_editorial -> {
                if (check_box_editorial.isChecked) {
                    newsDesksHashMap.put(R.id.check_box_editorial, "Editorial")
                } else {
                    newsDesksHashMap.remove(R.id.check_box_editorial)
                }
            }

            R.id.check_box_financial -> {
                if (check_box_financial.isChecked) {
                    newsDesksHashMap.put(R.id.check_box_financial, "Financial")
                } else {
                    newsDesksHashMap.remove(R.id.check_box_financial)
                }
            }

            R.id.check_box_politics -> {
                if (check_box_politics.isChecked) {
                    newsDesksHashMap.put(R.id.check_box_politics, "Politics")
                } else {
                    newsDesksHashMap.remove(R.id.check_box_politics)
                }
            }

            R.id.check_box_science -> {
                if (check_box_science.isChecked) {
                    newsDesksHashMap.put(R.id.check_box_science, "Science")
                } else {
                    newsDesksHashMap.remove(R.id.check_box_science)
                }
            }
        }
        setSearchButtonClickable()
    }

    //Set up the text change listener.
    fun setupTextChangeListener() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setSearchButtonClickable()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    //Set the clickability of the search button.
    fun setSearchButtonClickable() {
        if (searchEditText.text.isNotEmpty() && searchEditText.text.isNotBlank() && newsDesksHashMap.isNotEmpty()) {
            btn_search.isEnabled = true
            btn_search.text = getString(R.string.btn_search)
        } else {
            btn_search.isEnabled = false
            btn_search.text = getString(R.string.btn_enter_terms)
        }
    }
}
