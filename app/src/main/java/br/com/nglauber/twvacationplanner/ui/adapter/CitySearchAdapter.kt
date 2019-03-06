package br.com.nglauber.twvacationplanner.ui.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import br.com.nglauber.twvacationplanner.R
import br.com.nglauber.twvacationplanner.data.model.City
import kotlinx.android.synthetic.main.item_city_result.view.*
import java.lang.IllegalArgumentException


class CitySearchAdapter(context: Context) : AutoCompleteAdapter<City, CitySearchAdapter.ViewHolder>(
    context, R.layout.item_city_result) {

    override fun createViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

    override fun bindView(item: City, position: Int, vh: ViewHolder) {
        val city = getItem(position)
        vh.txtCity.text = city.district
        vh.txtStateCountry.text = context.getString(
            br.com.nglauber.twvacationplanner.R.string.state_country, city.province, city.country
        )
    }

    override fun convertResult(resultValue: City): String {
        return resultValue.district
    }

    class ViewHolder(view: View) {
        val txtCity: TextView = view.txtCity
        val txtStateCountry: TextView = view.txtStateCountry
    }
}