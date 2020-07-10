package com.example.carwaleassignment.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Cases {

    @SerializedName("Global")
    @Expose
    var global: Global? = null
    @SerializedName("Countries")
    @Expose
    var countries: List<Country>? = null
    @SerializedName("Date")
    @Expose
    var date: String? = null

}
