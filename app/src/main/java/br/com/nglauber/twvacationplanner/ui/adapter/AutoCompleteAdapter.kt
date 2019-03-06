package br.com.nglauber.twvacationplanner.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes

abstract class AutoCompleteAdapter<T, VH>(
    protected val context: Context,
    @LayoutRes val layout: Int): BaseAdapter(), Filterable {

    private val noFilter = NoFilter()
    private val items = mutableListOf<T>()

    override fun getItem(position: Int): T = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = items.size

    override fun getFilter(): Filter = noFilter

    abstract fun bindView(item: T, position: Int, vh: VH)

    abstract fun createViewHolder(view: View): VH

    abstract fun convertResult(resultValue: T): String

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val context = parent.context
        val obj = getItem(position)
        val vh: VH
        val filledView = if (convertView == null) {
            val view = LayoutInflater.from(context)
                .inflate(layout, parent, false)
            vh = createViewHolder(view)
            view.tag = vh
            view
        } else {
            @Suppress("UNCHECKED_CAST")
            vh = convertView.tag as VH
            convertView
        }
        bindView(obj, position, vh)
        return filledView
    }

    fun updateItems(list: List<T>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    private inner class NoFilter : Filter() {

        override fun performFiltering(q: CharSequence?): Filter.FilterResults {
            return Filter.FilterResults().apply {
                values = items
                count = items.size
            }
        }

        override fun publishResults(c: CharSequence?, results: Filter.FilterResults?) {
            notifyDataSetChanged()
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            @Suppress("UNCHECKED_CAST")
            return convertResult(resultValue as T)
        }
    }
}