package br.com.nglauber.twvacationplanner.data.model

import java.util.*

data class VacationSuggestion(
    val startDate: Date,
    val endDate: Date,
    val forecastInfo: List<ForecastInfo>?
)