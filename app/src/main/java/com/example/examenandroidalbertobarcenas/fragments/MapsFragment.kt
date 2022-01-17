package com.example.examenandroidalbertobarcenas.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.examenandroidalbertobarcenas.R
import com.example.examenandroidalbertobarcenas.fragments.viewmodel.MapsViewModel
import com.example.examenandroidalbertobarcenas.models.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map:GoogleMap
    private lateinit var mFusedLocationView: FusedLocationProviderClient
    private lateinit var mDatabase: DatabaseReference
    private var tmpRealTimeMarker: ArrayList<Marker> = ArrayList()
    private var realTimeMarkers: ArrayList<Marker> = ArrayList()
    private lateinit var camaraZoom: CameraPosition

    companion object {
        fun newInstance() = MapsFragment()
    }

    private lateinit var viewModel: MapsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.maps_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapsViewModel::class.java)

        var mapFragment = childFragmentManager.findFragmentById(R.id.fMaps) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mFusedLocationView = LocationServices.getFusedLocationProviderClient(this.requireContext())

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        mDatabase = FirebaseDatabase.getInstance().getReference("users")

        mDatabase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                realTimeMarkers.forEach{ marker: Marker->
                    marker.remove()
                }

                dataSnapshot?.children?.forEach { child: DataSnapshot ->
                    val dataLocation = child.getValue(Location::class.java)
                    var latitud: Double = dataLocation!!.latitud.toDouble()
                    var longitud: Double = dataLocation!!.longitud.toDouble()
                    var date: String = dataLocation.date
                    var markerOptions: MarkerOptions = MarkerOptions()
                    markerOptions.position(LatLng(latitud, longitud)).title("Fecha: " + date)
                    map.addMarker(markerOptions)?.let { tmpRealTimeMarker.add(it) }
                    camaraZoom = CameraPosition.Builder()
                        .target(LatLng(latitud, longitud))
                        .zoom(15F)
                        .bearing(0F)
                        .tilt(30F)
                        .build()
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(camaraZoom))
                }

                realTimeMarkers.clear()
                realTimeMarkers.addAll(tmpRealTimeMarker)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun countDownTimer(){
        object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                onMapReady(map)
            }

            override fun onFinish() {
            }
        }.start()
    }
}