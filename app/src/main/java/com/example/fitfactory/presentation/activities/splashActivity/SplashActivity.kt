package com.example.fitfactory.presentation.activities.splashActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.fitfactory.R
import com.example.fitfactory.app.App
import com.example.fitfactory.data.database.exercise.ExerciseRepository
import com.example.fitfactory.data.models.app.Exercise
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.activities.mainActivity.MainActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashActivity: AppCompatActivity() {

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    @Inject
    lateinit var exerciseRepository: ExerciseRepository

    private val callResult = MutableLiveData<Any>()

    private val rxDisposer = CompositeDisposable()

    private fun insertToDb(data: List<Exercise>?) = GlobalScope.launch {
        exerciseRepository.insert(data)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        callResult.observe(this, Observer {
            startMainActivity()
        })

        val db = FirebaseDatabase.getInstance()
        db.reference.child("server").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                val baseUrl = "http://192.168.0.1:8080/"
                Injector.init(application as App, baseUrl)
                Injector.component.inject(this@SplashActivity)
                fetch()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val baseUrl = "http://" + (dataSnapshot.value as HashMap<*, *>)["host"].toString() + ":8080/"
                Injector.init(application as App, baseUrl)
                Injector.component.inject(this@SplashActivity)
                fetch()
            }
        })
    }

    private fun startMainActivity() {
        val mainActivity = Intent(this, MainActivity::class.java)
        mainActivity.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        mainActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        mainActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        mainActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(mainActivity)
    }

    override fun onDestroy() {
        rxDisposer.dispose()
        super.onDestroy()
    }

    private fun fetch(){
        rxDisposer.add(retrofitRepository.getAllExercises()
            .subscribeBy(
                onSuccess = {
                    if (it.status){
                        insertToDb(it.data)
                        callResult.postValue(true)
                    }else{
                        callResult.postValue(false)
                    }

                },
                onError = {
                    Log.e("SplashViewModel", it.message)
                    callResult.postValue(false)
                }
            ))
    }
}