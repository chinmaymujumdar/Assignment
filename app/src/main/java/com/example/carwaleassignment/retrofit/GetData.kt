package com.example.carwaleassignment.retrofit

import com.example.carwaleassignment.model.Cases
import io.reactivex.Observable
import retrofit2.http.GET

interface GetData {
    @GET("summary")
    fun getCases():Observable<Cases>
}