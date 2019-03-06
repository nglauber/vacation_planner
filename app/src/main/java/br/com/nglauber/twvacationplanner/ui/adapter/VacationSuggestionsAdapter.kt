package br.com.nglauber.twvacationplanner.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.nglauber.twvacationplanner.R
import br.com.nglauber.twvacationplanner.data.extensions.toSimpleString
import br.com.nglauber.twvacationplanner.data.model.VacationSuggestion
import kotlinx.android.synthetic.main.item_dates_suggestion.view.*

class VacationSuggestionsAdapter(
    private val results: List<VacationSuggestion>
): RecyclerView.Adapter<VacationSuggestionsAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dates_suggestion, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = results.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val suggestion = results[position]
        holder.txtDateFrom.text = suggestion.startDate.toSimpleString()
        holder.txtDateTo.text = suggestion.endDate.toSimpleString()
        holder.txtDaysCount.text = suggestion.forecastInfo?.size.toString()
    }

    class VH(view: View): RecyclerView.ViewHolder(view) {
        val txtDateFrom: TextView = view.txtDateFrom
        val txtDateTo: TextView = view.txtDateTo
        val txtDaysCount: TextView = view.txtDaysCount
    }
}