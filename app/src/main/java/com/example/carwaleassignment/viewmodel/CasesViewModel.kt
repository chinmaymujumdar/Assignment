package com.example.carwaleassignment.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.carwaleassignment.Type
import com.example.carwaleassignment.model.Cases
import com.example.carwaleassignment.model.Country
import com.example.carwaleassignment.model.FilterModel
import com.example.carwaleassignment.model.Global
import com.example.carwaleassignment.repository.DataRepository
import com.example.carwaleassignment.retrofit.APICallback
import com.example.carwaleassignment.retrofit.DefaultSubscriber
import com.example.carwaleassignment.retrofit.ServerCall

class CasesViewModel(application: Application) : AndroidViewModel(application) {

    var countryList=MutableLiveData<MutableList<Country>>()
    var globalCases=MutableLiveData<Global>()
    var repository:DataRepository?=null
    var originalCountryList= mutableListOf<Country>()
    var map=HashMap<Type,FilterModel>()
    var list= mutableListOf<FilterModel>()
    var filterList=MutableLiveData<MutableList<FilterModel>>()
    var exception=MutableLiveData<String>()

    init {
        repository= DataRepository()
    }

    fun getTotalCases(){
        val serverCall= ServerCall<Cases>()
        serverCall.execute(repository?.getData(), DefaultSubscriber(object : APICallback<Cases> {
            override fun onSuccess(t: Cases) {
                val list= mutableListOf<Country>()
                t.countries?.let {
                    list.addAll(it)
                    list.sortByDescending { it.totalConfirmed }
                }
                originalCountryList.addAll(list)
                countryList.postValue(list)
                globalCases.postValue(t.global)
                putCurrentOnTop(list)
            }

            override fun onError(e: Throwable) {
                Log.d("onError",e.message)
                exception.postValue(e.message)
            }

        }))
    }

    fun sortAesc(type: Type){
        when(type){
            Type.COUNTRY-> countryList.value?.sortBy { it.country }
            Type.TOTAL_CONFIRMED->countryList.value?.sortBy { it.totalConfirmed }
            Type.TOTAL_DEATH->countryList.value?.sortBy { it.totalDeaths }
            Type.TOTAL_RECOVERED->countryList.value?.sortBy { it.totalDeaths }
        }
        countryList.postValue(countryList.value)
    }

    fun sortDesc(type: Type){
        when(type){
            Type.COUNTRY-> countryList.value?.sortByDescending { it.country }
            Type.TOTAL_CONFIRMED->countryList.value?.sortByDescending { it.totalConfirmed }
            Type.TOTAL_DEATH->countryList.value?.sortByDescending { it.totalDeaths }
            Type.TOTAL_RECOVERED->countryList.value?.sortByDescending { it.totalDeaths }
        }
        countryList.postValue(countryList.value)
    }

    fun manipulateLessThan(type: Type,value:Int){
        var list:MutableList<Country>?=null
        when(type){
            Type.TOTAL_RECOVERED-> {list=countryList.value?.filter { it.totalRecovered!!<=value } as MutableList<Country>}
            Type.TOTAL_DEATH-> {list=countryList.value?.filter { it.totalDeaths!!<=value } as MutableList<Country>}
            Type.TOTAL_CONFIRMED-> {list=countryList.value?.filter { it.totalConfirmed!!<=value }as MutableList<Country>}
        }
        countryList.postValue(list)
    }

    fun manipulateGreaterThan(type: Type,value: Int){
        var list:MutableList<Country>?=null
        when(type){
            Type.TOTAL_RECOVERED-> {list=countryList.value?.filter { it.totalRecovered!!>=value } as MutableList<Country>}
            Type.TOTAL_DEATH-> {list=countryList.value?.filter { it.totalDeaths!!>=value } as MutableList<Country>}
            Type.TOTAL_CONFIRMED-> {list=countryList.value?.filter { it.totalConfirmed!!>=value }as MutableList<Country>}
        }
        countryList.postValue(list)
    }

    fun addLessToMap(type: Type,value:Int){
        val mapVal=FilterModel(Type.LESS,type,value)
        map.put(type,mapVal)
        if (list.contains(FilterModel(Type.LESS,type,0))){
            val index=list.indexOf(FilterModel(Type.LESS,type,0))
            var model=list.get(index)
            model.operator=Type.LESS
            model.value=value
        }else{
            list.add(mapVal)
        }
        filterList.postValue(list)
        manipulateList()
    }

    fun addGreaterToMap(type: Type,value:Int){
        val mapVal=FilterModel(Type.GREATER,type,value)
        map.put(type,mapVal)
        if (list.contains(FilterModel(Type.GREATER,type,0))){
            val index=list.indexOf(FilterModel(Type.GREATER,type,0))
            var model=list.get(index)
            model.operator=Type.GREATER
            model.value=value
        }else{
            list.add(mapVal)
        }
        filterList.postValue(list)
        manipulateList()
    }

    fun removeFilter(index:Int){
        list.removeAt(index)
        filterList.postValue(list)
        manipulateList()
    }

    fun manipulateList(){
        var tempList= mutableListOf<Country>()
        tempList.addAll(originalCountryList)
        for (value in list){
            when(value.type){
                Type.TOTAL_RECOVERED->{
                    if (value.operator==Type.LESS){
                        val list=tempList.filter { it.totalRecovered!!<=value.value }
                        tempList.clear()
                        tempList.addAll(list)
                    }else{
                        val list=tempList.filter { it.totalRecovered!!>=value.value }
                        tempList.clear()
                        tempList.addAll(list)
                    }
                }
                    Type.TOTAL_DEATH->{
                        if (value.operator==Type.LESS){
                            val list=tempList.filter { it.totalDeaths!!<=value.value }
                            tempList.clear()
                            tempList.addAll(list)
                        }else{
                            val list=tempList.filter { it.totalDeaths!!>=value.value }
                            tempList.clear()
                            tempList.addAll(list)
                        }
                    }
                Type.TOTAL_CONFIRMED->{
                    if (value.operator==Type.LESS){
                        val list=tempList.filter { it.totalConfirmed!!<=value.value }
                        tempList.clear()
                        tempList.addAll(list)
                    }else{
                        val list=tempList.filter { it.totalConfirmed!!>=value.value }
                        tempList.clear()
                        tempList.addAll(list)
                    }
                }
            }
        }
        countryList.postValue(tempList)
    }

    fun updateCountryOnTop(country: String){
        val list=countryList.value
        if (list!=null) {
            var coun = Country()
            coun.countryCode = country
            val index = list?.indexOf(coun)
            if (index!=-1) {
                coun = index?.let { list.get(it) }!!
                list.removeAt(index)
                list.add(0, coun)
                countryList.postValue(list)
            }
        }
    }

    fun checkIfLocationStored():Boolean{
        val sharedPref=getApplication<Application>().getSharedPreferences("",0)
        return sharedPref.contains("Country_code")
    }

    fun getCountryCode():String{
        if (checkIfLocationStored()){
            val sharedPref=getApplication<Application>().getSharedPreferences("",0)
            val code=sharedPref.getString("Country_code","")
            if (code!=null && !code.isEmpty()) {
                return code
            }
        }
        return ""
    }

    private fun putCurrentOnTop(list: MutableList<Country>){
        if (checkIfLocationStored()){
            var coun = Country()
            coun.countryCode = getCountryCode()
            val index = list.indexOf(coun)
            if (index!=-1) {
                coun = index.let { list.get(it) }
                list.removeAt(index)
                list.add(0, coun)
                countryList.postValue(list)
            }
        }
    }
}