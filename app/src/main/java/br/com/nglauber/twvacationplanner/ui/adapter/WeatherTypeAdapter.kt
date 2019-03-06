package br.com.nglauber.twvacationplanner.ui.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import br.com.nglauber.twvacationplanner.R
import br.com.nglauber.twvacationplanner.data.model.WeatherType

class WeatherTypeAdapter(context: Context) : AutoCompleteAdapter<WeatherType, WeatherTypeAdapter.ViewHolder>(
    context, R.layout.item_weather_type) {

    override fun createViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

    override fun bindView(item: WeatherType, position: Int, vh: ViewHolder) {
        val weatherType = getItem(position)
        vh.text.text = weatherType.name.capitalize()
    }

    override fun convertResult(resultValue: WeatherType): String {
        return resultValue.name
    }

    class ViewHolder(view: View) {
        val text: TextView = view as TextView
    }
}