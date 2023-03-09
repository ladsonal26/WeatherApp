package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


class SplashScreen : AppCompatActivity() {
    lateinit var mfusedlocation:FusedLocationProviderClient
    private var  myrequstcode:Int=1011
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mfusedlocation=LocationServices.getFusedLocationProviderClient(this)
            getLastLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if(checkpermissions()){
            if(locationEnable()){
                mfusedlocation.lastLocation.addOnCompleteListener {
                    task->
                    var location:Location?=task.result
                    if(location==null){
                        newLocation()
                    }else{
                        Handler(Looper.getMainLooper()).postDelayed({
                        var intent= Intent(this,MainActivity::class.java)
                        intent.putExtra("lat",location.latitude.toString())
                        intent.putExtra("long",location.longitude.toString())
                        startActivity(intent)
                        finish()
                        },2000)
                    }
                }
            }else{
                Toast.makeText(this,"Please Turn on your GPS location",Toast.LENGTH_LONG).show()
            }
        }
        else{
            requestpermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun newLocation() {
      var locationRequest= com.google.android.gms.location.LocationRequest()
        locationRequest.priority=LocationRequest.QUALITY_HIGH_ACCURACY
        locationRequest.interval=0
        locationRequest.fastestInterval=0
        locationRequest.numUpdates=1
        mfusedlocation=LocationServices.getFusedLocationProviderClient(this)
        mfusedlocation.requestLocationUpdates(locationRequest,locationcallback,Looper.myLooper())

    }
    private val locationcallback=object :LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation: Location? =p0.lastLocation
        }
    }

    private fun requestpermission() {
       ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest
           .permission.ACCESS_FINE_LOCATION),myrequstcode)
    }

    private fun locationEnable(): Boolean {
        var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkpermissions(): Boolean {
     if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED||
             ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
     ){
         return true

     }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==myrequstcode){
           if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
               getLastLocation()
           }
       }
    }
}




