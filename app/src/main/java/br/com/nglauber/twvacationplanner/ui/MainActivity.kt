package br.com.nglauber.twvacationplanner.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.nglauber.twvacationplanner.R
import br.com.nglauber.twvacationplanner.data.model.City
import br.com.nglauber.twvacationplanner.data.model.WeatherType
import br.com.nglauber.twvacationplanner.extension.hideKeyboard
import br.com.nglauber.twvacationplanner.presentation.VacationPlannerViewModel
import br.com.nglauber.twvacationplanner.presentation.ViewEvent
import br.com.nglauber.twvacationplanner.ui.adapter.CitySearchAdapter
import br.com.nglauber.twvacationplanner.ui.adapter.Divider
import br.com.nglauber.twvacationplanner.ui.adapter.VacationSuggestionsAdapter
import br.com.nglauber.twvacationplanner.ui.adapter.WeatherTypeAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_main_results.*
import kotlinx.android.synthetic.main.layout_main_top.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: VacationPlannerViewModel by viewModel()

    private var errorToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupCityField()
        setupDaysField()
        setupWeatherTypes()
        setupRecyclerResults()
    }

    private fun setupCityField() {
        val cityAdapter = CitySearchAdapter(this)
        actvCity.setAdapter(cityAdapter)
        actvCity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchCity(s.toString())
                viewModel.selectedCity = null
            }
        })
        actvCity.setOnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position) as City
            viewModel.selectedCity = selected
        }
        viewModel.citiesSearchResult.observe(this,
            Observer { viewEvent ->
                when (viewEvent.state) {
                    ViewEvent.STATE_SUCCESS ->
                        cityAdapter.updateItems(viewEvent.data!!)
                    ViewEvent.STATE_ERROR ->
                        showError(R.string.main_error_search_city)
                }
            }
        )
    }

    private fun setupDaysField() {
        val options =
            (VacationPlannerViewModel.MIN_VACATION_DAYS..VacationPlannerViewModel.MAX_VACATION_DAYS)
                .map { it.toString() }

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnDays.adapter = adapter
        spnDays.setSelection(options.indexOf(viewModel.numberOfDays.toString()))
        spnDays.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val days = parent?.adapter?.getItem(position)?.toString()?.toInt()
                    ?: VacationPlannerViewModel.MIN_VACATION_DAYS
                viewModel.numberOfDays = days
            }
        }
    }

    private fun setupWeatherTypes() {
        val weatherTypeAdapter = WeatherTypeAdapter(this)
        actvWeatherType.setAdapter(weatherTypeAdapter)
        actvWeatherType.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchWeatherType(s.toString())
            }
        })
        actvWeatherType.setOnItemClickListener { parent, _, position, _ ->
            actvWeatherType.text = null
            val selected = parent.getItemAtPosition(position) as WeatherType
            if (viewModel.selectWeatherType(selected)) {
                addChipToGroup(selected, cgWeatherType)
            }
        }
        viewModel.weatherTypesSearchResult.observe(this,
            Observer { viewEvent ->
                when (viewEvent.state) {
                    ViewEvent.STATE_SUCCESS ->
                        weatherTypeAdapter.updateItems(viewEvent.data!!)
                    ViewEvent.STATE_ERROR ->
                        showError(R.string.main_error_search_weather_type)
                }
            }
        )
        viewModel.selectedWeatherTypes.observe(this,
            Observer { selectedWeatherTypes ->
                selectedWeatherTypes.forEach {
                    addChipToGroup(it, cgWeatherType)
                }
            }
        )
    }

    private fun addChipToGroup(weatherType: WeatherType, chipGroup: ChipGroup) {
        val chip = Chip(this).apply {
            text = weatherType.name.capitalize()
            setChipBackgroundColorResource(br.com.nglauber.twvacationplanner.R.color.colorAccent)
            setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.white))
            isClickable = false
            isCheckable = false
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                if (viewModel.unselectWeatherType(weatherType)) {
                    chipGroup.removeView(it)
                }
            }
        }
        chipGroup.addView(chip)
    }

    private fun setupRecyclerResults() {
        btnFind.setOnClickListener {
            viewModel.searchVacationBestDates()
            hideKeyboard()
        }

        rvResults.layoutManager = LinearLayoutManager(this)
        rvResults.addItemDecoration(Divider(this, R.drawable.divider))

        viewModel.vacationSuggestionResult.observe(this,
            Observer { viewEvent ->
                when (viewEvent.state) {
                    ViewEvent.STATE_LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        vwEmpty.visibility = View.GONE
                    }
                    ViewEvent.STATE_SUCCESS -> {
                        val list = viewEvent.data!!

                        progressBar.visibility = View.GONE
                        vwEmpty.visibility = if (list.isNotEmpty()) View.GONE else View.VISIBLE

                        rvResults.adapter = VacationSuggestionsAdapter(list)
                    }
                    ViewEvent.STATE_ERROR -> {
                        progressBar.visibility = View.GONE
                        vwEmpty.visibility = View.GONE
                        showError(R.string.main_error_search_vacation_dates)
                    }
                }
            }
        )
    }

    private fun showError(@StringRes message: Int, len: Int = Toast.LENGTH_SHORT) {
        if (errorToast != null) {
            errorToast?.cancel()
        }
        errorToast = Toast.makeText(this, message, len).apply {
            show()
        }
    }
}
