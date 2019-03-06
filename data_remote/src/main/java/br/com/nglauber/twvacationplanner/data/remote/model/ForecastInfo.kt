package br.com.nglauber.twvacationplanner.data.remote.model

import br.com.nglauber.twvacationplanner.data.model.TemperatureInfo
import com.google.gson.annotations.SerializedName
import java.util.*

data class ForecastInfo(
    val date: Date,
    val temperature: TemperatureInfo,
    @SerializedName("woeid")
    val cityId: String,
    val weather: String
)