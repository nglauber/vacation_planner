package br.com.nglauber.twvacationplanner.data.remote.model

import com.google.gson.annotations.SerializedName

data class City (
    @SerializedName("woeid")
    val id: String,
    val district: String,
    val province: String,
    @SerializedName("state_acronym")
    val state: String,
    val country: String
) {
    override fun toString(): String {
        return district
    }
}