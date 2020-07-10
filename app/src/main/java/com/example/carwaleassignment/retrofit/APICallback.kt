package com.example.carwaleassignment.retrofit

interface APICallback<T> {
    fun onSuccess(t:T)
    fun onError(e: Throwable)
}