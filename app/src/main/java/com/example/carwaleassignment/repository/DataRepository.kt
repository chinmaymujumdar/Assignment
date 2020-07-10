package com.example.carwaleassignment.repository

import com.example.carwaleassignment.model.Cases
import com.example.carwaleassignment.retrofit.GetData
import com.example.carwaleassignment.retrofit.RetrofitInstance
import io.reactivex.Observable

class DataRepository {

    var api: GetData?=null

    init {
        api= RetrofitInstance.getInstance()
    }

    fun getData():Observable<Cases>?{
        return api?.getCases()
    }
}