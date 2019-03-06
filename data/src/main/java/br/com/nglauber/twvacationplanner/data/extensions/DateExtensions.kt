package br.com.nglauber.twvacationplanner.data.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.toSimpleString(locale: Locale = Locale.getDefault()) : String {
    val format = SimpleDateFormat("dd/MM/yyy", locale)
    return format.format(this)
}