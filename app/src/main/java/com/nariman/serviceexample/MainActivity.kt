package com.nariman.serviceexample

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var myService: MyService? = null
    var isBound = false

    private val myConneciton = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            Log.i("myservice", "MainActivity. In onServiceConnected() method.")
            val binder = service as MyService.MyServiceBinder
            myService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(className: ComponentName?) {
            Log.i("myservice", "MainActivity. In onServiceDisconnected() method. Service killed by the system")
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickBindService(v: View){
        if (!isBound){
            val myIntent = Intent(this, MyService::class.java)
            bindService(myIntent, myConneciton, Context.BIND_AUTO_CREATE)
        }
    }

    fun onClickUnBindService(v: View?){
        if (!isBound) return
        unbindService(myConneciton)
        isBound = false
    }

    fun getRandomNumber(v: View){
        if (!isBound) return
        textView2.text = myService!!.getRandomNumber().toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        onClickUnBindService(null)
    }
}
