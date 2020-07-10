package com.example.carwaleassignment.retrofit

import io.reactivex.observers.DisposableObserver

class DefaultSubscriber<T>(val callback: APICallback<T>): DisposableObserver<T>() {
    override fun onComplete() {
    }

    override fun onNext(t: T) {
        callback.onSuccess(t)
    }

    override fun onError(e: Throwable) {
        callback.onError(e)
    }
}