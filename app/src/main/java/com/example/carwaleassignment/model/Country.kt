package com.example.carwaleassignment.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import android.graphics.Typeface
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.carwaleassignment.viewmodel.CasesViewModel


class Country : BaseObservable() {

    @SerializedName("Country")
    @Expose
    var country: String? = null
    @Bindable get() {
        return field
    }
    @SerializedName("CountryCode")
    @Expose
    var countryCode: String? = null
        @Bindable get() {
            return field
        }

    @SerializedName("Slug")
    @Expose
    var slug: String? = null
        @Bindable get() {
            return field
        }

    @SerializedName("NewConfirmed")
    @Expose
    var newConfirmed: Int? = null
        @Bindable get() {
            return field
        }

    @SerializedName("TotalConfirmed")
    @Expose
    var totalConfirmed: Int? = null
        @Bindable get() {
            return field
        }

    @SerializedName("NewDeaths")
    @Expose
    var newDeaths: Int? = null
        @Bindable get() {
            return field
        }

    @SerializedName("TotalDeaths")
    @Expose
    var totalDeaths: Int? = null
        @Bindable get() {
            return field
        }

    @SerializedName("NewRecovered")
    @Expose
    var newRecovered: Int? = null
        @Bindable get() {
            return field
        }

    @SerializedName("TotalRecovered")
    @Expose
    var totalRecovered: Int? = null
        @Bindable get() {
            return field
        }

    @SerializedName("Date")
    @Expose
    var date: String? = null
        @Bindable get() {
            return field
        }

    @SerializedName("Premium")
    @Expose
    var premium: Premium? = null
        @Bindable get() {
            return field
        }

    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as Country
        if (!(countryCode==other.countryCode)){
            return false
        }
        return true
    }

    companion object {
        @BindingAdapter(value = ["bind:isBold","bind:code","bind:data"], requireAll = false)
        @JvmStatic
        fun setBold(view: TextView, position: Int,code:String,viewModel:CasesViewModel) {
            if (position == 0&&code.equals(viewModel.getCountryCode())) {
                view.setTypeface(null, Typeface.BOLD)
            } else {
                view.setTypeface(null, Typeface.NORMAL)
            }
        }
    }

}
