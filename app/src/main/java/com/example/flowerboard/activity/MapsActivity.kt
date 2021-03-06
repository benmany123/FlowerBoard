package com.example.flowerboard.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.flowerboard.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient : FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        //Get fragment when the map is ready to be used.
        val fragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        fragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                 {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location->
                if(location != null){

                    //Current location
                    lastLocation = location
                    val currentLatLong = LatLng(location.latitude, location.longitude)

                    //Define the coordinate of the shop
                    val shop1 = LatLng(22.3179438,114.1691614) //Mong Kok
                    val shop2 = LatLng(22.3905233,114.0050124) //Tuen Mun
                    val shop3 = LatLng(22.3282854,114.1585399) //Sham Shui Po
                    val shop4 = LatLng(22.3082466,114.1709209) //Jordan Station
                    val shop5 = LatLng(22.2773499,114.1696255) //Wan Chai
                    val shop6 = LatLng(22.3334816,114.1926925) //Diamond Hill

                    //Place the shop marker on the map
                    placeMarkerShop(shop1)
                    placeMarkerShop(shop2)
                    placeMarkerShop(shop3)
                    placeMarkerShop(shop4)
                    placeMarkerShop(shop5)
                    placeMarkerShop(shop6)

                    //Animation
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 13f))
                }else{
                    //Failed to get current location
                    Toast.makeText(this, "Failed to get the current location", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //Function of place marker on the map
    private fun placeMarkerShop(shop: LatLng) {
        val markerOptions2 = MarkerOptions().position(shop)
        markerOptions2.title("$shop")
        mMap.addMarker(markerOptions2)
    }
    override fun onMarkerClick(p0: Marker)=false
}