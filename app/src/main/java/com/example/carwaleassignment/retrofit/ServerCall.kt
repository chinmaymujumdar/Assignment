package com.example.carwaleassignment.retrofit

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ServerCall<T> {
    @SuppressLint("CheckResult")
    fun execute(observable: Observable<T>?, subscriber: DefaultSubscriber<T>){
        observable?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribeWith(subscriber)
    }
}