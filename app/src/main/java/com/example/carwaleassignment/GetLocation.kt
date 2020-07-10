package com.example.carwaleassignment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.util.*

class GetLocation(private val context: Context,private val listener: CountryListener): LocationListener{

    private var sharePref:SharedPreferences?= null

    init {
        sharePref=context.getSharedPreferences("",0)
    }

    fun getLatitude(){
        Dexter.withContext(context).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    handleGPS()
                }

                override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
                    p1?.continuePermissionRequest()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                }

            }).check()
    }

    @SuppressLint("MissingPermission")
    fun handleGPS(){
        if (checkIfLocationPermissionAvailable()){
            val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000*60*1,10F,this)
                getCodeFromLocation(manager.getLastKnownLocation(LocationManager.GPS_PROVIDER))
            }else{
                Toast.makeText(context,"Please turn on location",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkIfLocationPermissionAvailable():Boolean{
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
    }

    override fun onLocationChanged(location: Location?) {
        getCodeFromLocation(location)
    }

    override fun onProviderDisabled(p0: String?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    private fun getCodeFromLocation(location: Location?){
        if (location!= null){
            val geocoder = Geocoder(context, Locale.getDefault())
            val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (address!=null && address.size>0){
                storeCountryCodeInPref(address[0].countryCode)
                listener.getCountryCode(address[0].countryCode)
            }
        }
    }

    private fun storeCountryCodeInPref(code:String){
        val editor=sharePref?.edit()
        editor?.putString("Country_code",code)
        editor?.apply()
    }
}