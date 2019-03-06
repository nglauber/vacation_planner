package br.com.nglauber.twvacationplanner.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.nglauber.tests.DataMockFactory
import br.com.nglauber.twvacationplanner.data.model.City
import br.com.nglauber.twvacationplanner.data.model.ForecastInfo
import br.com.nglauber.twvacationplanner.data.model.VacationSuggestion
import br.com.nglauber.twvacationplanner.data.model.WeatherType
import br.com.nglauber.twvacationplanner.domain.city.SearchCityUseCase
import br.com.nglauber.twvacationplanner.domain.vacation.SearchVacationBestDatesUseCase
import br.com.nglauber.twvacationplanner.domain.weathertype.SearchWeatherTypeUseCase
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class VacationPlannerViewModelTest {

    private lateinit var viewModel: VacationPlannerViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var searchCities: SearchCityUseCase
    private lateinit var searchWeatherTypes: SearchWeatherTypeUseCase
    private lateinit var searchVacationBestDates: SearchVacationBestDatesUseCase

    private lateinit var captorCities : KArgumentCaptor<((List<City>) -> Unit)>
    private lateinit var captorWeatherTypes : KArgumentCaptor<((List<WeatherType>) -> Unit)>
    private lateinit var captorVacationSuggestion : KArgumentCaptor<((List<VacationSuggestion>) -> Unit)>

    @Before
    fun setup() {
        captorCities = argumentCaptor()
        captorWeatherTypes = argumentCaptor()
        captorVacationSuggestion = argumentCaptor()

        searchCities = mock()
        searchWeatherTypes = mock()
        searchVacationBestDates = mock()

        viewModel = VacationPlannerViewModel(searchCities, searchWeatherTypes, searchVacationBestDates)
    }

    @Test
    fun searchCitiesExecutesUseCase() {
        val city = "Recife"
        viewModel.searchCity(city)
        verify(searchCities, times(1)).execute(eq(city), any(), any(), eq(null))
    }

    @Test
    fun searchCitiesReturnsData() {
        val city = "Recife"
        val cities = DataMockFactory.makeCitiesList(2)

        viewModel.searchCity(city)

        verify(searchCities).execute(eq(city), captorCities.capture(), any(), eq(null))
        captorCities.firstValue.invoke(cities)

        val citySearchResult = viewModel.citiesSearchResult.value
        assertNotNull(citySearchResult)
        assertEquals(citySearchResult?.state, ViewEvent.STATE_SUCCESS)
        assertEquals(citySearchResult?.data, cities)
        assertNull(citySearchResult?.error)
    }

    @Test
    fun searchWeatherTypeExecutesUseCase() {
        val weatherType = "Clear"
        viewModel.searchWeatherType(weatherType)
        verify(searchWeatherTypes, times(1)).execute(eq(weatherType), any(), any(), eq(null))
    }

    @Test
    fun searchWeatherTypesReturnsData() {
        val weatherType = "Clear"
        val weatherTypes = DataMockFactory.makeWeatherTypeList(2)

        viewModel.searchWeatherType(weatherType)

        verify(searchWeatherTypes).execute(eq(weatherType), captorWeatherTypes.capture(), any(), eq(null))
        captorWeatherTypes.firstValue.invoke(weatherTypes)

        val weatherTypesResult = viewModel.weatherTypesSearchResult.value
        assertNotNull(weatherTypesResult)
        assertEquals(weatherTypesResult?.state, ViewEvent.STATE_SUCCESS)
        assertEquals(weatherTypesResult?.data, weatherTypes)
        assertNull(weatherTypesResult?.error)
    }

    @Test
    fun selectCityChangesSelection() {
        val selectedCities = DataMockFactory.makeCitiesList(2)
        selectedCities.forEach {
            viewModel.selectedCity = it
            assertEquals(viewModel.selectedCity, it)
        }
    }

    @Test
    fun setNumberOfVacationDaysChanges() {
        viewModel.numberOfDays = VacationPlannerViewModel.MIN_VACATION_DAYS
        assertEquals(viewModel.numberOfDays, VacationPlannerViewModel.MIN_VACATION_DAYS)
        viewModel.numberOfDays = VacationPlannerViewModel.MAX_VACATION_DAYS
        assertEquals(viewModel.numberOfDays, VacationPlannerViewModel.MAX_VACATION_DAYS)
        val days = 15
        viewModel.numberOfDays = days
        assertEquals(viewModel.numberOfDays, days)
        viewModel.numberOfDays = VacationPlannerViewModel.MIN_VACATION_DAYS - 1
        assertEquals(viewModel.numberOfDays, days)
        viewModel.numberOfDays = VacationPlannerViewModel.MAX_VACATION_DAYS + 1
        assertEquals(viewModel.numberOfDays, days)
    }

    @Test
    fun selectAndUnselectWeather() {
        fun selectedWeatherTypes() = viewModel.selectedWeatherTypes.value
        val weatherTypes = DataMockFactory.makeWeatherTypeList(3)

        assert(selectedWeatherTypes()?.isEmpty() ?: false)

        viewModel.selectWeatherType(weatherTypes[0])
        assert(selectedWeatherTypes()?.size == 1)
        viewModel.selectWeatherType(weatherTypes[0]) // trying to add the same weather type again
        assert(selectedWeatherTypes()?.size == 1)
        viewModel.selectWeatherType(weatherTypes[1])
        assert(selectedWeatherTypes()?.size == 2)
        viewModel.selectWeatherType(weatherTypes[2])
        assert(selectedWeatherTypes()?.size == 3)
    }

    @Test
    fun searchVacationDatesWithoutParamsNotExecutesUseCase() {
        viewModel.searchVacationBestDates()
        verify(searchWeatherTypes, times(0)).execute(any(), any(), any(), eq(null))
    }

    @Test
    fun searchVacationDatesExecutesUseCase() {
        viewModel.selectedCity = DataMockFactory.makeCity()
        viewModel.selectWeatherType(DataMockFactory.makeWeatherType())
        viewModel.searchVacationBestDates()
        verify(searchWeatherTypes, times(0)).execute(any(), any(), any(), eq(null))
    }

    @Test
    fun searchVacationDatesReturnsData() {
        val selectedCity = DataMockFactory.makeCity()
        val weatherTypes = DataMockFactory.makeWeatherTypeList(2)
        val params = SearchVacationBestDatesUseCase.Params(
            selectedCity.id, 2019, weatherTypes
        )
        viewModel.selectedCity = selectedCity
        weatherTypes.forEach { viewModel.selectWeatherType(it) }
        viewModel.searchVacationBestDates()

        val vacationResult = buildVacationResult()

        verify(searchVacationBestDates).execute(eq(params), captorVacationSuggestion.capture(), any(), eq(null))
        captorVacationSuggestion.firstValue.invoke(vacationResult)

        val vacationSuggestionsResults = viewModel.vacationSuggestionResult.value
        assertNotNull(vacationSuggestionsResults)
        assertEquals(vacationSuggestionsResults?.state, ViewEvent.STATE_SUCCESS)
        assertEquals(vacationSuggestionsResults?.data, vacationResult)
        assertNull(vacationSuggestionsResults?.error)
    }

    private fun buildVacationResult(): List<VacationSuggestion> {
        val cityId = "001"
        val weatherList = listOf(
            "Sunny",
            "Cloud"
        )
        val forecastInfoList = weatherList.map { weather ->
            ForecastInfo(Date(), DataMockFactory.makeTemperatureInfo(), cityId, weather)
        }
        return listOf(VacationSuggestion(Date(), Date(), forecastInfoList))
    }
}