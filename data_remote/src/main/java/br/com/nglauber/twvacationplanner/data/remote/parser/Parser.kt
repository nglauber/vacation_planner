package br.com.nglauber.twvacationplanner.data.remote.parser

import br.com.nglauber.twvacationplanner.data.remote.model.City
import br.com.nglauber.twvacationplanner.data.remote.model.ForecastInfo
import br.com.nglauber.twvacationplanner.data.model.City as CityData
import br.com.nglauber.twvacationplanner.data.model.ForecastInfo as ForecastInfoData

object Parser {

    fun toData(city: City): CityData =
        CityData(
            city.id,
            city.district,
            city.province,
            city.state,
            city.country
        )

    fun toData(fi: ForecastInfo): ForecastInfoData =
        ForecastInfoData(
            fi.date,
            fi.temperature,
            fi.cityId,
            fi.weather
        )
}