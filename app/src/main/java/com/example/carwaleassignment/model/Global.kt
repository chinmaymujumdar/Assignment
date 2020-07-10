package com.example.carwaleassignment.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Global {

    @SerializedName("NewConfirmed")
    @Expose
    var newConfirmed: Int? = null
    @SerializedName("TotalConfirmed")
    @Expose
    var totalConfirmed: Int? = null
    @SerializedName("NewDeaths")
    @Expose
    var newDeaths: Int? = null
    @SerializedName("TotalDeaths")
    @Expose
    var totalDeaths: Int? = null
    @SerializedName("NewRecovered")
    @Expose
    var newRecovered: Int? = null
    @SerializedName("TotalRecovered")
    @Expose
    var totalRecovered: Int? = null

}
