package com.example.rxandroiddemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    var myDataToPass: String = "Sample Data to Observe"

    lateinit var myObservable: Observable<String>
    lateinit var myobserver: Observer<String>


    var disposableObserver: DisposableObserver<String>? = null
    var disposableObserverTwo: DisposableObserver<String>? = null

    var compositeDisposable: CompositeDisposable? = null

    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myObservable = Observable.just(myDataToPass)

        // this observable subscribe on IO thread
        myObservable.subscribeOn(Schedulers.io())

        // this observable observe on UI Thread to perform UI Operations
        myObservable.observeOn(AndroidSchedulers.mainThread())

        myobserver = object : Observer<String> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: String) {
                Toast.makeText(this@MainActivity,t, Toast.LENGTH_SHORT).show()
            }

            override fun onError(e: Throwable) {
            }

        }

        myObservable.subscribe(myobserver)

        disposableObserver = object : DisposableObserver<String>() {
            override fun onComplete() {
            }

            override fun onNext(t: String) {
                Toast.makeText(this@MainActivity,t, Toast.LENGTH_SHORT).show()

            }

            override fun onError(e: Throwable) {
            }

        }
        myObservable.subscribe(disposableObserver!!)

        disposableObserverTwo = object : DisposableObserver<String>() {
            override fun onComplete() {
            }

            override fun onNext(t: String) {
                Toast.makeText(this@MainActivity,t, Toast.LENGTH_SHORT).show()

            }

            override fun onError(e: Throwable) {
            }

        }
        myObservable.subscribe(disposableObserverTwo!!)

        compositeDisposable?.add(disposableObserver!!)
        compositeDisposable?.add(disposableObserverTwo!!)

    }

    override fun onDestroy() {
        super.onDestroy()
        // Dispose the resource, the operation should be idempotent.
        disposable?.dispose()

        disposableObserver?.dispose()

//       Atomically clears the container, then disposes all the previously contained Disposables.
        compositeDisposable?.clear()
    }

}
