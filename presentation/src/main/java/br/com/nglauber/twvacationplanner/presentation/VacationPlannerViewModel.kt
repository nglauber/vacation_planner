package br.com.nglauber.twvacationplanner.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.nglauber.twvacationplanner.data.model.City
import br.com.nglauber.twvacationplanner.data.model.VacationSuggestion
import br.com.nglauber.twvacationplanner.data.model.WeatherType
import br.com.nglauber.twvacationplanner.domain.city.SearchCityUseCase
import br.com.nglauber.twvacationplanner.domain.vacation.SearchVacationBestDatesUseCase
import br.com.nglauber.twvacationplanner.domain.weathertype.SearchWeatherTypeUseCase

class VacationPlannerViewModel(
    private val searchCityUseCase: SearchCityUseCase,
    private val searchWeatherTypeUseCase: SearchWeatherTypeUseCase,
    private val searchVacationUseCase: SearchVacationBestDatesUseCase
) : ViewModel() {

    private val selectedWeatherTypesList = mutableListOf<WeatherType>()

    private val _citiesSearchResult = MutableLiveData<ViewEvent<List<City>>>()
    private val _weatherTypesSearchResult = MutableLiveData<ViewEvent<List<WeatherType>>>()
    private val _selectedWeatherTypes = MutableLiveData<List<WeatherType>>().apply {
        value = selectedWeatherTypesList
    }
    private val _vacationSuggestionResult = MutableLiveData<ViewEvent<List<VacationSuggestion>>>()

    var selectedCity: City? = null
    var numberOfDays = 15
        set(value) {
            if (value in (MIN_VACATION_DAYS..MAX_VACATION_DAYS)){
                field = value
            }
        }
    val citiesSearchResult: LiveData<ViewEvent<List<City>>> = _citiesSearchResult
    val weatherTypesSearchResult: LiveData<ViewEvent<List<WeatherType>>> = _weatherTypesSearchResult
    val selectedWeatherTypes: LiveData<List<WeatherType>> = _selectedWeatherTypes
    val vacationSuggestionResult: LiveData<ViewEvent<List<VacationSuggestion>>> =
        _vacationSuggestionResult

    fun searchCity(q: String) {
        _citiesSearchResult.value = ViewEvent(ViewEvent.STATE_LOADING)
        searchCityUseCase.execute(q,
            { result ->
                _citiesSearchResult.value = ViewEvent(ViewEvent.STATE_SUCCESS, result)
            },
            { error ->
                _citiesSearchResult.value = ViewEvent(ViewEvent.STATE_ERROR, error)
            }
        )
    }

    fun searchWeatherType(q: String) {
        _weatherTypesSearchResult.value = ViewEvent(ViewEvent.STATE_LOADING)
        searchWeatherTypeUseCase.execute(q,
            { result ->
                _weatherTypesSearchResult.value = ViewEvent(ViewEvent.STATE_SUCCESS, result)
            },
            { error ->
                _weatherTypesSearchResult.value = ViewEvent(ViewEvent.STATE_ERROR, error)
            }
        )
    }

    fun selectWeatherType(selected: WeatherType): Boolean {
        return if (selectedWeatherTypesList.find { it.id == selected.id } == null) {
            selectedWeatherTypesList.add(selected)
        } else {
            false
        }
    }

    fun unselectWeatherType(victim: WeatherType): Boolean {
        val index = selectedWeatherTypesList.indexOfFirst { it.id == victim.id }
        return if (index > -1) {
            selectedWeatherTypesList.removeAt(index)
            true
        } else {
            false
        }
    }

    fun searchVacationBestDates() {
        val selectedCityId = selectedCity?.id
        if (selectedCityId != null && selectedWeatherTypesList.isNotEmpty()) {
            _vacationSuggestionResult.value = ViewEvent(ViewEvent.STATE_LOADING)
            val params = SearchVacationBestDatesUseCase.Params(
                selectedCityId,
                2019, // TODO
                selectedWeatherTypesList,
                numberOfDays
            )
            searchVacationUseCase.execute(params,
                { result ->
                    _vacationSuggestionResult.value = ViewEvent(ViewEvent.STATE_SUCCESS, result)
                },
                { error ->
                    _vacationSuggestionResult.value = ViewEvent(ViewEvent.STATE_ERROR, error)
                }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchCityUseCase.dispose()
    }

    companion object {
        const val MIN_VACATION_DAYS = 7
        const val MAX_VACATION_DAYS = 30
    }
}