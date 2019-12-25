package com.nariman.serviceexample

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.util.*

class MyService : Service() {

    private var mRandomNumber: Int = 0
    private var isRandomGeneratorOn: Boolean = false

    private val MAX = 100
    private val MIN = 0

    private val myBinder = MyServiceBinder()

    inner class MyServiceBinder : Binder() {
        fun getService(): MyService {
            return this@MyService
        }
    }

    override fun onBind(intent: Intent): IBinder {
        isRandomGeneratorOn = true
        Log.i("myservice", "In onBind(). Thread id: ${Thread.currentThread().id}")
        Thread(object : Runnable{
            override fun run() {
                startRandomNumberGenerator()
            }
        }).start()
        return myBinder
    }

    override fun onDestroy() {
        Log.i("myservice", "Service Is Destroyed")
        stopRandomGenerator()
        super.onDestroy()
    }

    private fun startRandomNumberGenerator(){
        while (isRandomGeneratorOn){
            try {
                Thread.sleep(1000)
                mRandomNumber = Random().nextInt(MAX) + MIN
                Log.i("myservice", "Thread id: ${Thread.currentThread().id}, generated number: $mRandomNumber")
            } catch (e: InterruptedException){
                Log.i("myservice", "Thread interrupted")
            }
        }
    }

    private fun stopRandomGenerator(){
        isRandomGeneratorOn = false
    }

    fun getRandomNumber() = mRandomNumber
}
