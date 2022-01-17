package com.example.examenandroidalbertobarcenas.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.examenandroidalbertobarcenas.R
import com.example.examenandroidalbertobarcenas.adapters.PagerAdapter
import com.example.examenandroidalbertobarcenas.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayout
import java.util.*
import android.provider.Settings
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var adapter: androidx.viewpager.widget.PagerAdapter? = null
    private lateinit var mFusedLocationView: FusedLocationProviderClient
    private val permissionId = 2
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mFusedLocationView = LocationServices.getFusedLocationProviderClient(this)

        setTabLayout()
        setViewPage()
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationView.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result

                    if (location != null) {
                        var latLang : HashMap<String, String>
                                = HashMap()
                        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                        val currentDateandTime: String = sdf.format(Date())

                        latLang.put("latitud", location.latitude.toString())
                        latLang.put("longitud", location.longitude.toString())
                        latLang.put("date", currentDateandTime)

                        mDatabase = FirebaseDatabase.getInstance().getReference("users")
                        mDatabase.push().setValue(latLang).addOnSuccessListener {
                        }.addOnFailureListener {
                        }

                    }
                }
            } else {
                Toast.makeText(this, "Activar ubicación", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun setTabLayout(){
        binding.tlPrincipal.addTab(binding.tlPrincipal.newTab().setText("Peliculas").setIcon(R.drawable.ic_movie));
        binding.tlPrincipal.addTab(binding.tlPrincipal.newTab().setText("Mapa").setIcon(R.drawable.ic_map));
        binding.tlPrincipal.addTab(binding.tlPrincipal.newTab().setText("Galería").setIcon(R.drawable.ic_galery));
    }

    private fun setViewPage(){
        adapter = PagerAdapter(
            supportFragmentManager,
            binding.tlPrincipal.getTabCount()
        )

        binding.vpPrincipal!!.adapter = adapter
        binding.vpPrincipal!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tlPrincipal))
        binding.tlPrincipal!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.vpPrincipal!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }
}