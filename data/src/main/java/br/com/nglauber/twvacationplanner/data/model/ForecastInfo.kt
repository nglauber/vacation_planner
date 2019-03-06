package br.com.nglauber.twvacationplanner.data.model

import java.util.*

data class ForecastInfo(
    val date: Date,
    val temperature: TemperatureInfo,
    val cityId: String,
    val weather: String
)