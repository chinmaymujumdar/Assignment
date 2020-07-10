package com.example.carwaleassignment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carwaleassignment.adapter.CountryAdapter
import com.example.carwaleassignment.databinding.ActivityMainBinding
import com.example.carwaleassignment.model.Country
import com.example.carwaleassignment.model.FilterModel
import com.example.carwaleassignment.model.Global
import com.example.carwaleassignment.viewmodel.CasesViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.*
import androidx.core.app.ActivityCompat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var viewModel:CasesViewModel?=null
    private var adapter:CountryAdapter?=null
    private var binding:ActivityMainBinding?=null
    private var isFilterOn=false
    private var getLocation:GetLocation?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)

        viewModel=ViewModelProviders.of(this).get(CasesViewModel::class.java)
        viewModel?.getTotalCases()
        viewModel?.globalCases?.observe(this, Observer<Global> {
            binding?.totalCasesCount?.text=it.totalConfirmed.toString()
            binding?.deathCount?.text=it.totalDeaths.toString()
            binding?.recoveredCount?.text=it.totalRecovered.toString()
        })

        viewModel?.countryList?.observe(this, Observer<MutableList<Country>> {
            handleRecycler(it)
//            if (!isFilterOn){
//                viewModel?.getCountryCode()
//            }
        })

        viewModel?.filterList?.observe(this, Observer {
            addViews(it)
        })

        getLocation=GetLocation(this,object :CountryListener{
            override fun getCountryCode(countryCode: String) {
                if (!isFilterOn) {
                    viewModel?.updateCountryOnTop(countryCode)
                }
            }
        })

        getLocation?.getLatitude()

        viewModel?.exception?.observe(this, Observer {
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })

    }

    override fun onStart() {
        super.onStart()
        registerBroadcast()
    }

    private fun handleRecycler(list: MutableList<Country>){
        if (adapter==null){
            adapter= CountryAdapter(viewModel)
            binding?.recyclerView?.layoutManager=LinearLayoutManager(this)
            binding?.recyclerView?.adapter=adapter
            adapter?.submitList(list)
        }else {
            adapter?.submitList(list)
            adapter?.notifyDataSetChanged()
        }
    }

    fun sortAsc(view: View){
        when(view.id){
            R.id.country_asc -> viewModel?.sortAesc(Type.COUNTRY)
            R.id.total_country_asc ->viewModel?.sortAesc(Type.TOTAL_CONFIRMED)
            R.id.death_country_asc ->viewModel?.sortAesc(Type.TOTAL_DEATH)
            R.id.recovered_country_asc ->viewModel?.sortAesc(Type.TOTAL_RECOVERED)
        }
    }

    fun sortDesc(view: View){
        when(view.id){
            R.id.country_dsc -> viewModel?.sortDesc(Type.COUNTRY)
            R.id.total_country_dsc ->viewModel?.sortDesc(Type.TOTAL_CONFIRMED)
            R.id.death_country_dsc ->viewModel?.sortDesc(Type.TOTAL_DEATH)
            R.id.recovered_country_dsc ->viewModel?.sortDesc(Type.TOTAL_RECOVERED)
        }
    }

    fun showDialog(view: View){
        when(view.id){
            R.id.total_filter-> FilterDialog.showAlertDialog(this,viewModel,Type.TOTAL_CONFIRMED)
            R.id.filter_death-> FilterDialog.showAlertDialog(this,viewModel,Type.TOTAL_DEATH)
            R.id.recovered_filter->FilterDialog.showAlertDialog(this,viewModel,Type.TOTAL_RECOVERED)
        }
    }

    fun addViews(list: MutableList<FilterModel>){
        isFilterOn = list.size != 0
        binding?.flexbox?.removeAllViews()
        for ((index,value) in list.withIndex()) {
            var view = layoutInflater.inflate(R.layout.filter_item, null)
            val text=view.findViewById<TextView>(R.id.filter_txt)
            val clear=view.findViewById<ImageView>(R.id.clear)
            clear.id=index
            clear.setOnClickListener {
                viewModel?.removeFilter(it.id)
            }
            when(value.type){
                Type.TOTAL_RECOVERED->{
                    if (value.operator==Type.LESS){
                        text.text="Recovered < "+value.value.toString()
                    }else{
                        text.text="Recovered > "+value.value.toString()
                    }
                }
                Type.TOTAL_DEATH->{
                    if (value.operator==Type.LESS){
                        text.text="Death < "+value.value.toString()
                    }else{
                        text.text="Death > "+value.value.toString()
                    }
                }
                Type.TOTAL_CONFIRMED->{
                    if (value.operator==Type.LESS){
                        text.text="Confirmed < "+value.value.toString()
                    }else{
                        text.text="Confirmed > "+value.value.toString()
                    }
                }
            }
            binding?.flexbox?.addView(view)
        }
    }

    private val gpsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action!!.matches(LocationManager.PROVIDERS_CHANGED_ACTION.toRegex())) {
                getLocation?.getLatitude()
            }
        }
    }

    private fun registerBroadcast(){
        registerReceiver(gpsReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
    }

    private fun unRegisterReceiver(){
        unregisterReceiver(gpsReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterReceiver()
    }
}
